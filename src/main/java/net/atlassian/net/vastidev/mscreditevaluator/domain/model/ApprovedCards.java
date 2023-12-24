package net.atlassian.net.vastidev.mscreditevaluator.domain.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ApprovedCards {
    private String card;
    private String label;
    private BigDecimal limitApproved;
}
