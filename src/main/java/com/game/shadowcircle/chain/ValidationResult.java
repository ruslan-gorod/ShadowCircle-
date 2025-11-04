package com.game.shadowcircle.chain;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class ValidationResult {

  private final boolean valid;
  private final String reason;
  private final List<String> warnings;

  private ValidationResult(boolean valid, String reason) {
    this.valid = valid;
    this.reason = reason;
    this.warnings = new ArrayList<>();
  }

  private ValidationResult(boolean valid, String reason, List<String> warnings) {
    this.valid = valid;
    this.reason = reason;
    this.warnings = warnings != null ? warnings : new ArrayList<>();
  }

  public static ValidationResult valid() {
    return new ValidationResult(true, null);
  }

  public static ValidationResult invalid(String reason) {
    return new ValidationResult(false, reason);
  }

  public static ValidationResult warning(String warning) {
    ValidationResult result = new ValidationResult(true, null);
    result.warnings.add(warning);
    return result;
  }

  public boolean hasWarnings() {
    return !warnings.isEmpty();
  }
}