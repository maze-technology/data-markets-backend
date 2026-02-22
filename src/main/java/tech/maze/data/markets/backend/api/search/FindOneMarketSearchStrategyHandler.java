package tech.maze.data.markets.backend.api.search;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.dtos.markets.search.Criterion;

/**
 * Delegates FindOne market lookup to the first matching strategy.
 */
@Service
@RequiredArgsConstructor
public class FindOneMarketSearchStrategyHandler {
  private final List<FindOneMarketSearchStrategy> strategies;

  /**
   * Resolves a single market from criterion using available strategies.
   */
  public Optional<Market> handleSearch(Criterion criterion) {
    return strategies.stream()
        .filter(strategy -> strategy.supports(criterion))
        .findFirst()
        .flatMap(strategy -> strategy.search(criterion));
  }
}
