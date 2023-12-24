package net.atlassian.net.vastidev.mscreditevaluator.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SituationClient {
    private ClientData client;
    private List<ClientCard> cards;
}
