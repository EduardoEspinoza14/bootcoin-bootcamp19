package com.nttdata.bootcoin.configuration;

import com.nttdata.bootcoin.model.dto.Product;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerializer;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;
import reactor.kafka.sender.SenderResult;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Class KafkaProducerConfiguration.
 */
@Configuration
public class KafkaProducerConfiguration {

  public static final String TOPIC_INSERT = "wallet.insert";
  public static final String TOPIC_UPDATE = "wallet.update";
  public static final String TOPIC_DELETE = "wallet.delete";

  @Value(value = "${kafka.bootstrapAddress:}")
  private String bootstrapAddress;

  public static Flux<SenderResult<Long>> senderCreate(SenderOptions<String,
          Product> options, SenderRecord<String, Product, Long> record) {
    return KafkaSender.create(options).send(Flux.just(record));
  }

  /**
   * Method senderOptions.
   */
  @Bean
  public SenderOptions<String, Product> senderOptions() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    return SenderOptions.create(props);
  }

  private static SenderRecord<String, Product, Long> senderRecord(String id,
                                                                       Product productMongo,
                                                                       String topic,
                                                                       Long correlation) {
    return SenderRecord.create(new ProducerRecord<>(topic, id, productMongo), correlation);
  }

  private static SenderRecord<String, Product, Long> senderRecord(String id,
                                                                  Product productMongo,
                                                                       String topic) {
    return senderRecord(id, productMongo, topic, new Date().getTime());
  }

  private static SenderRecord<String, Product, Long> senderRecord(Product productMongo,
                                                                       String topic) {
    return senderRecord(productMongo.getId(), productMongo, topic);
  }

  private static SenderRecord<String, Product, Long> senderRecord(String id, String topic) {
    return senderRecord(id, null, topic);
  }

  public static SenderRecord<String, Product, Long> insertRecord(Product productMongo) {
    return senderRecord(productMongo, TOPIC_INSERT);
  }

  public static SenderRecord<String, Product, Long> updateRecord(Product productMongo) {
    return senderRecord(productMongo, TOPIC_UPDATE);
  }

  public static SenderRecord<String, Product, Long> deleteRecord(String id) {
    return senderRecord(id, TOPIC_DELETE);
  }

}
