package com.game.shadowcircle.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Конфігурація ObjectMapper для серіалізації/десеріалізації
 */
@Configuration
public class ObjectMapperConfig {

  /**
   * Створює налаштований ObjectMapper для роботи з JSON
   */
  @Bean
  @Primary
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();

    // Реєструємо модуль для підтримки Java 8 Date/Time API (LocalDateTime)
    mapper.registerModule(new JavaTimeModule());

    // Вимикаємо запис дат як timestamp (використовуємо ISO-8601 формат)
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    // Вмикаємо форматування JSON (pretty print)
    mapper.enable(SerializationFeature.INDENT_OUTPUT);

    // Ігноруємо невідомі властивості при десеріалізації
    mapper.configure(
        com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
        false
    );

    return mapper;
  }
}