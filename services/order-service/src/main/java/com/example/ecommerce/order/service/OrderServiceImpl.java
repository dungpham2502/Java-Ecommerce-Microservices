package com.example.ecommerce.order.service;

import com.example.ecommerce.cart.client.CartClient;
import com.example.ecommerce.cart.dto.CartItemResponse;
import com.example.ecommerce.cart.dto.CartResponse;
import com.example.ecommerce.customer.client.CustomerClient;
import com.example.ecommerce.customer.dto.CustomerResponse;
import com.example.ecommerce.exception.BusinessException;
import com.example.ecommerce.kafka.dto.OrderConfirmation;
import com.example.ecommerce.kafka.producer.OrderProducer;
import com.example.ecommerce.order.entity.Order;
import com.example.ecommerce.order.entity.PaymentMethod;
import com.example.ecommerce.order.mapper.OrderMapper;
import com.example.ecommerce.order.dto.OrderRequest;
import com.example.ecommerce.order.dto.OrderResponse;
import com.example.ecommerce.order.repository.OrderRepository;
import com.example.ecommerce.orderline.dto.OrderLineRequest;
import com.example.ecommerce.orderline.entity.OrderLine;
import com.example.ecommerce.orderline.mapper.OrderLineMapper;
import com.example.ecommerce.orderline.repository.OrderLineRepository;
import com.example.ecommerce.orderline.service.OrderLineService;
import com.example.ecommerce.payment.client.PaymentClient;
import com.example.ecommerce.payment.dto.PaymentRequest;
import com.example.ecommerce.product.client.ProductClient;
import com.example.ecommerce.product.dto.ProductAvailabilityResponse;
import com.example.ecommerce.product.dto.PurchaseRequest;
import com.example.ecommerce.product.dto.PurchaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderMapper orderMapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;
    private final CartClient cartClient;
    private final OrderLineMapper orderLineMapper;
    private final OrderLineRepository orderLineRepository;

    @Override
    @Transactional
    public Integer createOrder(OrderRequest orderRequest) {
        Integer customerId = orderRequest.customerId();
        PaymentMethod paymentMethod = orderRequest.paymentMethod();

        // Check if customer exists
        CustomerResponse customer = customerClient.findCustomerById(customerId)
                .orElseThrow(() -> new BusinessException("Cannot create order:: No customer exists"));

        // Fetch cart details
        CartResponse cart = cartClient.getCart(customerId);
        if (cart == null || cart.products().isEmpty()) {
            throw new BusinessException("Cannot create order:: Cart is empty");
        }

        // Check product quantities
        List<Integer> productIds = cart.products().stream().map(CartItemResponse::productId).collect(Collectors.toList());
        List<Double> quantities = cart.products().stream().map(CartItemResponse::quantity).collect(Collectors.toList());
        List<ProductAvailabilityResponse> availableProducts = productClient.checkAvailability(productIds, quantities);

        if (availableProducts.isEmpty() || availableProducts.size() != cart.products().size()) {
            throw new BusinessException("Cannot create order:: One or more products are not available");
        }

        // Calculate total amount
        BigDecimal totalAmount = calculateTotalAmount(availableProducts, cart);

        // Map cart items to order lines
        List<OrderLineRequest> orderLineRequests = cart.products().stream()
                .map(product -> new OrderLineRequest(null, product.productId(), product.quantity()))
                .toList();

        // Create the order
        Order order = Order.builder()
                .reference(UUID.randomUUID().toString())
                .totalAmount(totalAmount)
                .paymentMethod(paymentMethod)
                .customerId(customerId)
                .orderLines(orderLineRequests.stream().map(orderLineMapper::toOrderLine).collect(Collectors.toList()))
                .build();

        // Save the order and update order lines
        order = orderRepository.save(order);
        Order finalOrder = order;
        List<OrderLine> orderLines = new ArrayList<>();
        for (OrderLineRequest orderLineRequest : orderLineRequests) {
            OrderLine orderLine = orderLineMapper.toOrderLine(orderLineRequest);
            orderLine.setOrder(finalOrder);
            orderLines.add(orderLineRepository.save(orderLine));
        }

        // Purchase order
        List<PurchaseResponse> purchasedProducts = purchaseProducts(orderLines);

        // Request payment
        requestPayment(totalAmount, paymentMethod, order, customer);

        // Send order confirmation
        sendOrderConfirmation(totalAmount, paymentMethod, order, customer, purchasedProducts);

        // Clear cart
        cartClient.clearCart(customerId);

        return order.getId();
    }

    private void sendOrderConfirmation(BigDecimal totalAmount, PaymentMethod paymentMethod, Order order, CustomerResponse customer, List<PurchaseResponse> purchasedProducts) {
        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        order.getReference(),
                        totalAmount,
                        paymentMethod,
                        customer.firstName(),
                        customer.lastName(),
                        customer.email(),
                        purchasedProducts
                )
        );
    }

    private void requestPayment(BigDecimal totalAmount, PaymentMethod paymentMethod, Order order, CustomerResponse customer) {
        System.out.println("Customer details" + customer.toString() + customer.lastName());
        if (paymentMethod == PaymentMethod.PAYPAL) {
            PaymentRequest paymentRequest = new PaymentRequest(
                    totalAmount,
                    paymentMethod,
                    order.getId(),
                    order.getReference(),
                    customer
            );
            paymentClient.requestOrderPayment(paymentRequest);
            return;
        }

        throw new UnsupportedOperationException("Payment method not supported");
    }

    private BigDecimal calculateTotalAmount(List<ProductAvailabilityResponse> availableProducts, CartResponse cartResponse) {
        return availableProducts.stream()
                .map(product -> product.price().multiply(BigDecimal.valueOf(
                        cartResponse.products().stream()
                                .filter(item -> item.productId().equals(product.productId()))
                                .findFirst()
                                .orElseThrow(() -> new BusinessException("Product not found in cart"))
                                .quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<PurchaseResponse> purchaseProducts(List<OrderLine> orderLines) {
        List<PurchaseRequest> purchaseRequests = orderLines.stream()
                .map(orderLine -> new PurchaseRequest(orderLine.getProductId(), orderLine.getQuantity()))
                .collect(Collectors.toList());
        return productClient.purchaseProducts(purchaseRequests);
    }

    @Override
    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse findById(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cannot find order:: No order exists"));
        return orderMapper.toOrderResponse(order);
    }

    @Override
    public List<OrderResponse> findOrdersByCustomerId(Integer customerId) {
        return orderRepository.findOrdersByCustomerId(customerId).stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }
}
