package com.charitan.statistics.kafka.consumer;

import com.charitan.statistics.jwt.external.JwtExternalAPI;
import com.charitan.statistics.kafka.enums.KeyConsumerTopic;
import io.jsonwebtoken.security.Jwks;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AbstractConsumerSeekAware;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.PublicKey;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class KafkaConsumer extends AbstractConsumerSeekAware {

    private final JwtExternalAPI jwtExternalAPI;

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = KeyConsumerTopic.PUBLIC_KEY_CHANGE, groupId = "profile")
    public void handlePublicKeyChange(String message) {
        try {

            // Remove the outer quotes
            if (message.startsWith("\"") && message.endsWith("\"")) {
                message = message.substring(1, message.length() - 1).replace("\\", "");
            }

            System.out.println(message);

            Key jwk = Jwks.parser()
                    .build()
                    .parse(message)
                    .toKey();
            if (jwk instanceof PublicKey) {
                jwtExternalAPI.setSigPublicKey((PublicKey) jwk);
                logger.info("Signature {} public key updated", ((PublicKey) jwk).getFormat());
            }

        } catch (Exception e) {
            logger.error("Failed to process public key created event", e);
            throw new RuntimeException("Failed to process public key created event: " + e.getMessage());
        }
    }

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
        for (TopicPartition topicPartition : assignments.keySet()) {
            if ("key.signature.public.change".equals(topicPartition.topic())) {
                callback.seekRelative(topicPartition.topic(), topicPartition.partition(), -1, false);
            }
        }
    }
}
