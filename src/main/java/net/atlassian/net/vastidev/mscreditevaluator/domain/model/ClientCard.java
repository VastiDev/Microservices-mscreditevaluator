package net.atlassian.net.vastidev.mscreditevaluator.domain.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClientCard {
    private String name;
    private String label;
    private BigDecimal limitReleased;
}
