package net.atlassian.net.vastidev.mscreditevaluator.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    @Value("${mq.queues.emissao-cards")
    private String emissionCardsQueue;

    @Bean
    public Queue queueEmissionCards(){
        return new Queue(emissionCardsQueue,true);
    }
}
