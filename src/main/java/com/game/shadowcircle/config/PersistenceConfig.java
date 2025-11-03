package com.game.shadowcircle.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфігурація для файлових репозиторіїв — JSON або YAML. Використовується для серіалізації /
 * десеріалізації сцен та станів гри.
 */
@Configuration
public class PersistenceConfig {

  /**
   * Можливі значення: json або yaml
   */
  @Value("${game.persistence.format:json}")
  private String format;

  /**
   * Повертає відповідний ObjectMapper залежно від формату. YAML зручно для ручного редагування
   * сценаріїв.
   */
  @Bean
  public ObjectMapper objectMapper() {
    if ("yaml".equalsIgnoreCase(format) || "yml".equalsIgnoreCase(format)) {
      return new ObjectMapper(new YAMLFactory());
    }
    return new ObjectMapper(); // стандартний JSON
  }

  public String getFormat() {
    return format;
  }
}
