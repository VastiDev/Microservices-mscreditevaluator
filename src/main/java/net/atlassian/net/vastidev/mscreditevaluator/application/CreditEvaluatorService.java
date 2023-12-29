package net.atlassian.net.vastidev.mscreditevaluator.application;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import net.atlassian.net.vastidev.mscreditevaluator.application.ex.ClientDataNotFoundException;
import net.atlassian.net.vastidev.mscreditevaluator.application.ex.ErrorComunicationMicroservicesException;
import net.atlassian.net.vastidev.mscreditevaluator.application.ex.ErrorSolicitCardException;
import net.atlassian.net.vastidev.mscreditevaluator.domain.model.*;
import net.atlassian.net.vastidev.mscreditevaluator.infra.clients.CardsResourceFeignClient;
import net.atlassian.net.vastidev.mscreditevaluator.infra.clients.ClientResourceFeignClient;
import net.atlassian.net.vastidev.mscreditevaluator.infra.mqueue.EmissionCardPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class CreditEvaluatorService {

    private final ClientResourceFeignClient clientsClient;
    private final CardsResourceFeignClient cardsClient;
    private final EmissionCardPublisher emissionCardPublisher;

    private static final Logger LOGGER = LoggerFactory.getLogger(CreditEvaluatorService.class);
    public SituationClient getSituationClient(String cpf)
            throws ClientDataNotFoundException, ErrorComunicationMicroservicesException {
        try {
            ResponseEntity<ClientData> clientDataResponse = clientsClient.clientData(cpf);
            ResponseEntity<List<ClientCard>> cardsResponse = cardsClient.getCardsByClient(cpf);

            return SituationClient.builder()
                    .client(clientDataResponse.getBody())
                    .cards((cardsResponse.getBody()))
                    .build();
        } catch (FeignException.FeignClientException e) {
            int status = e.status();
            if (HttpStatus.NOT_FOUND.value() == status) {
                throw new ClientDataNotFoundException("Client data not found for CPF: " + cpf);
            }
            throw new ErrorComunicationMicroservicesException(e.getMessage(), status);
        }
    }


    public ReturnClientReview makeReview(String cpf, Long income)
            throws ClientDataNotFoundException, ErrorComunicationMicroservicesException {
        try {
            ResponseEntity<ClientData> clientDataResponse = clientsClient.clientData(cpf);
            if (!clientDataResponse.getStatusCode().is2xxSuccessful() || clientDataResponse.getBody() == null) {
                LOGGER.warn("Client data not found for CPF: {}", cpf);
                throw new ClientDataNotFoundException("Client data not found for CPF: " + cpf);
            }

            ResponseEntity<List<Card>> cardResponse = cardsClient.getCardsWithIncomeTill(income);
            if (!cardResponse.getStatusCode().is2xxSuccessful() || cardResponse.getBody() == null) {
                LOGGER.warn("No cards found for income: {}", income);
                return new ReturnClientReview(Collections.emptyList());
            }

            ClientData clientData = clientDataResponse.getBody();
            List<Card> cards = cardResponse.getBody();

            var listCardsApproved = cards.stream().flatMap(card -> {
                BigDecimal basicLimit = card.getBasicLimit();
                if (basicLimit == null) {
                    LOGGER.warn("Missing limit basic for card: {}", card.getName());
                    return Stream.empty();
                }

                Integer age = clientData.getAge();
                if (age == null) {
                    LOGGER.warn("Missing age for client with CPF: {}", cpf);
                    return Stream.empty();
                }

                BigDecimal ageBD = BigDecimal.valueOf(age);
                var fator = ageBD.divide(BigDecimal.valueOf(10), RoundingMode.HALF_UP);
                BigDecimal limitApproved = fator.multiply(basicLimit);

                ApprovedCards approved = new ApprovedCards();
                approved.setCard(card.getName());
                approved.setLabel(card.getLabel());
                approved.setLimitApproved(limitApproved);

                return Stream.of(approved);
            }).collect(Collectors.toList());

            return new ReturnClientReview(listCardsApproved);

        } catch (FeignException.FeignClientException e) {
            LOGGER.error("Communication error with microservices: {}", e.getMessage());
            int status = e.status();
            if (HttpStatus.NOT_FOUND.value() == status) {
                throw new ClientDataNotFoundException("Client data not found for CPF: " + cpf);
            }
            throw new ErrorComunicationMicroservicesException(e.getMessage(), status);
        }
    }

    public ProtocolSolicitCard solicitEmissionCard(DatasSolicitEmissionCards datas){
        try{
            emissionCardPublisher.solicitCard(datas);
            var protocol = UUID.randomUUID().toString();
            return new ProtocolSolicitCard(protocol);
        }catch (Exception e){
            throw new ErrorSolicitCardException(e.getMessage());
        }
    }

}