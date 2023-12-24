package net.atlassian.net.vastidev.mscreditevaluator.domain.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Card {
    private Long id;
    private String name;
    private String label;
    private BigDecimal basicLimit;
}
