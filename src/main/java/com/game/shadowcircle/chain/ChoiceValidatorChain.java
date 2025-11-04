package com.game.shadowcircle.chain;

import com.game.shadowcircle.model.Choice;
import com.game.shadowcircle.model.GameContext;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChoiceValidatorChain {

  private final List<ChoiceValidator> validators = new ArrayList<>();

  public ChoiceValidatorChain() {
    // Додаємо валідатори в порядку пріоритету
    validators.add(new PlayerAliveValidator());
    validators.add(new CoverIntactValidator());
    validators.add(new StealthRequirementValidator());
    validators.add(new ItemRequirementValidator());
    validators.add(new HealthRequirementValidator());
  }

  public ValidationResult validate(Choice choice, GameContext context) {
    ValidationResult result = ValidationResult.valid();

    for (ChoiceValidator validator : validators) {
      ValidationResult currentResult = validator.validate(choice, context);

      if (!currentResult.isValid()) {
        return currentResult; // Зупиняємось на першій помилці
      }

      // Збираємо попередження
      if (currentResult.hasWarnings()) {
        result.getWarnings().addAll(currentResult.getWarnings());
      }
    }

    return result;
  }

  public void addValidator(ChoiceValidator validator) {
    validators.add(validator);
  }
}