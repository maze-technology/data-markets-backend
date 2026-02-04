package tech.maze.data.markets.backend.domain.ports.out;

import java.util.Optional;
import java.util.UUID;
import tech.maze.data.markets.backend.domain.models.Market;

/**
 * Generated type.
 */
public interface LoadMarketPort {
  /**
   * Generated method.
   */
  Optional<Market> findById(UUID id);
}
