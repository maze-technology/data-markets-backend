package tech.maze.data.markets.backend.api.search;

import java.util.Optional;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.dtos.markets.search.Criterion;

/**
 * Strategy for resolving a single market from a request criterion.
 */
public interface FindOneMarketSearchStrategy {
  /**
   * Whether this strategy supports the given criterion.
   */
  boolean supports(Criterion criterion);

  /**
   * Attempts to resolve a market for the criterion.
   */
  Optional<Market> search(Criterion criterion);
}
