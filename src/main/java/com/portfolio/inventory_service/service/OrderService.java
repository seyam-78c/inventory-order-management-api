package com.portfolio.inventory_service.service;

import com.portfolio.inventory_service.dto.OrderRequest;
import com.portfolio.inventory_service.dto.OrderResponse;
import com.portfolio.inventory_service.model.Order;
import com.portfolio.inventory_service.model.OrderStatus;
import com.portfolio.inventory_service.model.Product;
import com.portfolio.inventory_service.repository.OrderRepository;
import com.portfolio.inventory_service.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {
        Product product = productRepository.findByIdWithLock(request.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + request.productId()));

        if (product.getStockQuantity() < request.quantity()) {
            throw new IllegalStateException("Insufficient stock. Available: " + product.getStockQuantity());
        }

        // Deduct inventory
        product.setStockQuantity(product.getStockQuantity() - request.quantity());
        productRepository.save(product);

        // Calculate total and save order
        BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(request.quantity()));

        Order order = new Order();
        order.setProductId(product.getId());
        order.setQuantity(request.quantity());
        order.setTotalPrice(total);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setCreatedAt(Instant.now());

        Order saved = orderRepository.save(order);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    private OrderResponse mapToResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getProductId(),
                order.getQuantity(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }
}