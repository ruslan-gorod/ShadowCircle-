package com.game.shadowcircle.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Шаблон NPC
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NPCTemplate {

  private String id;
  private String nameTemplate; // "Охоронець #{num}", "Агент {codename}"
  private NPCRole role;
  private String description;
  // Характеристики
  private int suspicionLevel; // Наскільки підозрілий
  private int intelligence; // Розум
  private int alertness; // Пильність
  // Можливі дії
  @Builder.Default
  private List<String> possibleDialogues = new ArrayList<>();
  @Builder.Default
  private List<String> possibleActions = new ArrayList<>();
  // Відносини
  @Builder.Default
  private Map<String, Integer> relationshipModifiers = new HashMap<>();

  public static NPCTemplate guard(int alertness) {
    return NPCTemplate.builder()
        .id("guard_" + UUID.randomUUID())
        .nameTemplate("Guard")
        .role(NPCRole.GUARD)
        .alertness(alertness)
        .suspicionLevel(50)
        .intelligence(40)
        .description("Regular Guard")
        .build();
  }

  public static NPCTemplate informant() {
    return NPCTemplate.builder()
        .id("informant_" + UUID.randomUUID())
        .nameTemplate("Informant")
        .role(NPCRole.INFORMANT)
        .alertness(70)
        .intelligence(60)
        .suspicionLevel(30)
        .description("Source of information")
        .build();
  }

  public static NPCTemplate target(String name) {
    return NPCTemplate.builder()
        .id("target_" + UUID.randomUUID())
        .nameTemplate(name)
        .role(NPCRole.TARGET)
        .alertness(80)
        .intelligence(70)
        .suspicionLevel(60)
        .description("Main mission objective")
        .build();
  }

  public static NPCTemplate ally(String name) {
    return NPCTemplate.builder()
        .id("ally_" + UUID.randomUUID())
        .nameTemplate(name)
        .role(NPCRole.ALLY)
        .alertness(50)
        .intelligence(65)
        .suspicionLevel(10)
        .description("A reliable ally")
        .build();
  }

  public enum NPCRole {
    TARGET("Target"),
    CONTACT("Contact"),
    GUARD("Guard"),
    INFORMANT("Informant"),
    ALLY("Ally"),
    NEUTRAL("Neutral"),
    ENEMY("Enemy");

    private final String displayName;

    NPCRole(String displayName) {
      this.displayName = displayName;
    }

    public String getDisplayName() {
      return displayName;
    }
  }
}