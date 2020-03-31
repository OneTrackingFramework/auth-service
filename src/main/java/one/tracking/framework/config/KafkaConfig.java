package one.tracking.framework.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import one.tracking.framework.event.UserCredentials;

@Configuration
@EnableKafka
public class KafkaConfig extends KafkaConfigs {

  @Bean(name = "kafka.consumer.UserCredentials")
  @ConditionalOnMissingBean(name = "kafkaListenerContainerFactory")
  public ConsumerFactory<String, UserCredentials> consumerFactory() {
    return new DefaultKafkaConsumerFactory<>(
        consumerConfig(),
        new StringDeserializer(),
        new JsonDeserializer<>(UserCredentials.class));
  }

  @Bean(name = "kafka.listener.UserCredentials")
  public ConcurrentKafkaListenerContainerFactory<String, UserCredentials> listenerContainerFactory() {

    final ConcurrentKafkaListenerContainerFactory<String, UserCredentials> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    return factory;
  }



}
