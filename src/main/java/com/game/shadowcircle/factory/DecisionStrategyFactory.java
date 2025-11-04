package com.game.shadowcircle.factory;

import com.game.shadowcircle.strategy.AggressiveDecisionStrategy;
import com.game.shadowcircle.strategy.BalancedDecisionStrategy;
import com.game.shadowcircle.strategy.DecisionStrategy;
import com.game.shadowcircle.strategy.DiplomaticDecisionStrategy;
import com.game.shadowcircle.strategy.StealthDecisionStrategy;
import com.game.shadowcircle.strategy.TechnicalDecisionStrategy;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DecisionStrategyFactory {

  private final Map<String, DecisionStrategy> strategies = new HashMap<>();

  public DecisionStrategyFactory() {
    // Реєструємо стандартні стратегії
    strategies.put("STEALTH", new StealthDecisionStrategy());
    strategies.put("AGGRESSIVE", new AggressiveDecisionStrategy());
    strategies.put("DIPLOMATIC", new DiplomaticDecisionStrategy());
    strategies.put("TECHNICAL", new TechnicalDecisionStrategy());
  }

  public DecisionStrategy getStrategy(String choiceType, String preferredApproach) {
    // Якщо є спеціальна стратегія для типу вибору, використовуємо її
    DecisionStrategy strategy = strategies.get(choiceType);

    // Інакше використовуємо улюблений підхід гравця
    if (strategy == null && preferredApproach != null) {
      strategy = strategies.get(preferredApproach);
    }

    // За замовчуванням - збалансована стратегія
    return strategy != null ? strategy : new BalancedDecisionStrategy();
  }

  public void registerStrategy(String type, DecisionStrategy strategy) {
    strategies.put(type, strategy);
    log.debug("Зареєстровано стратегію: {}", type);
  }
}
