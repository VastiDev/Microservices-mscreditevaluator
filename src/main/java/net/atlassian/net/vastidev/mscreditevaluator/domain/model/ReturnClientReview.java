package net.atlassian.net.vastidev.mscreditevaluator.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReturnClientReview {
    private List<ApprovedCards> approvedCards;
}
