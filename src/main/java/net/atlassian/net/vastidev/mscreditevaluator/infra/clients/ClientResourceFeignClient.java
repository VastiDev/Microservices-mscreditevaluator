package net.atlassian.net.vastidev.mscreditevaluator.infra.clients;

import net.atlassian.net.vastidev.mscreditevaluator.domain.model.Card;
import net.atlassian.net.vastidev.mscreditevaluator.domain.model.ClientData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "msclients", path = "/clients" )
public interface ClientResourceFeignClient {

    @GetMapping(params = "cpf")
    ResponseEntity<ClientData> clientData(@RequestParam("cpf") String cpf);

}
