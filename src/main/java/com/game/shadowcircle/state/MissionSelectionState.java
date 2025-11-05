package com.game.shadowcircle.state;

import com.game.shadowcircle.events.GameEventPublisher;
import com.game.shadowcircle.model.GameContext;
import com.game.shadowcircle.model.Mission;
import com.game.shadowcircle.service.MissionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Стан вибору місії
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MissionSelectionState implements State {

  private final MissionService missionService;
  private final GameEventPublisher eventPublisher;
  private List<Mission> availableMissions;

  @Override
  public void enter(GameContext context) {
    System.out.println("\n=== SELECT MISSION ===");

// Get available missions
    availableMissions = missionService.getAllMissions();

    if (availableMissions.isEmpty()) {

      System.out.println("No missions available.");
      System.out.println("\n0. Return to main menu");
      return;
    }

    // Відображаємо список місій
    for (int i = 0; i < availableMissions.size(); i++) {
      Mission mission = availableMissions.get(i);
      System.out.printf("%d. %s\n", i + 1, mission.getTitle());

      if (mission.getDescription() != null) {
        System.out.printf("   %s\n", mission.getDescription());
      }

      // Показуємо складність якщо є
      if (mission.getDifficulty() != null) {
        System.out.printf(" Difficulty: %s\n", mission.getDifficulty().getDisplayName());
      }

// Display the number of scenes
      if (mission.getScenes() != null) {
        System.out.printf(" Scenes: %d\n", mission.getScenes().size());
      }

      System.out.println();
    }

    System.out.println("0. Return to main menu");
    System.out.print("\nSelect a mission: ");
  }

  @Override
  public void update(GameContext context) {
    // Можна додати оновлення списку місій
  }

  @Override
  public void exit(GameContext context) {
    log.debug("Exiting mission selection state");
  }

  @Override
  public State handleInput(String input, GameContext context) {
    try {
      int choice = Integer.parseInt(input.trim());

      // Повернення до меню
      if (choice == 0) {
        return new MainMenuState();
      }

      // Перевірка валідності вибору
      if (choice < 1 || choice > availableMissions.size()) {
        System.out.println("Invalid mission number. Try again.");
        return this;
      }

      // Вибираємо місію
      Mission selectedMission = availableMissions.get(choice - 1);
      context.setCurrentMission(selectedMission);

      System.out.printf("\n Mission selected: %s\n", selectedMission.getTitle());

      return new MissionBriefingState(eventPublisher);

    } catch (NumberFormatException e) {
      System.out.println("Please enter a number. Try again.");
      return this;
    }
  }
}