package net.atlassian.net.vastidev.mscreditevaluator.application;

import lombok.RequiredArgsConstructor;
import net.atlassian.net.vastidev.mscreditevaluator.application.ex.ClientDataNotFoundException;
import net.atlassian.net.vastidev.mscreditevaluator.application.ex.ErrorComunicationMicroservicesException;
import net.atlassian.net.vastidev.mscreditevaluator.application.ex.ErrorSolicitCardException;
import net.atlassian.net.vastidev.mscreditevaluator.domain.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/credit-evaluator")
@RequiredArgsConstructor
public class CreditEvaluatorController {

    private final CreditEvaluatorService creditEvaluatorService;

    @GetMapping
    public String status() {
        return "ok";
    }

    @GetMapping(value = "/situation-client", params = "cpf")
    public ResponseEntity querySituationClient(@RequestParam String cpf) {
        try {
            SituationClient situationClient = creditEvaluatorService.getSituationClient(cpf);
            return ResponseEntity.ok(situationClient);
        } catch (ClientDataNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ErrorComunicationMicroservicesException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity clientReview(@RequestBody DataReview data) {
        try {
            ReturnClientReview returnClientReview = creditEvaluatorService.makeReview(data.getCpf(), data.getIncome());
            return ResponseEntity.ok(returnClientReview);
        } catch (ClientDataNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ErrorComunicationMicroservicesException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
    }

    @PostMapping("solicit-card")
    public ResponseEntity solicitCard(@RequestBody DatasSolicitEmissionCards datas) {
        try {
            ProtocolSolicitCard protocolSolicitCard = creditEvaluatorService.solicitEmissionCard(datas);
            return ResponseEntity.ok(protocolSolicitCard);
        } catch (ErrorSolicitCardException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }
}