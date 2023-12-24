package net.atlassian.net.vastidev.mscreditevaluator.infra.clients;

import net.atlassian.net.vastidev.mscreditevaluator.domain.model.Card;
import net.atlassian.net.vastidev.mscreditevaluator.domain.model.ClientCard;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "mscards", path = "/cards")
public interface CardsResourceFeignClient {
    @GetMapping(params = "cpf")
    ResponseEntity<List<ClientCard>> getCardsByClient(@RequestParam("cpf") String cpf);

    @GetMapping(params = "income")
    ResponseEntity<List<Card>> getCardsWithIncomeTill(@RequestParam("income") Long income);
}
