package net.atlassian.net.vastidev.mscreditevaluator.infra.mqueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.atlassian.net.vastidev.mscreditevaluator.domain.model.DatasSolicitEmissionCards;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmissionCardPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final Queue queueEmissionCards;

    public void solicitCard(DatasSolicitEmissionCards datas) throws JsonProcessingException{
        var json = convertIntoJson(datas);
        rabbitTemplate.convertAndSend(queueEmissionCards.getName(), json);
    }

    private String convertIntoJson(DatasSolicitEmissionCards datas) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        var json = mapper.writeValueAsString(datas);
        return json;
    }
}
