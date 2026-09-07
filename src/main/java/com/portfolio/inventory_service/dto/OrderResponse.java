package com.portfolio.inventory_service.dto;

import com.portfolio.inventory_service.model.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;

public record OrderResponse(
        Long id,
        Long productId,
        Integer quantity,
        BigDecimal totalPrice,
        OrderStatus status,
        Instant createdAt
) {}