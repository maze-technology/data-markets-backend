package tech.maze.data.markets.backend.domain.ports.in;

import java.util.Optional;
import java.util.UUID;
import tech.maze.data.markets.backend.domain.models.Market;

/**
 * Generated type.
 */
public interface FindMarketUseCase {
  /**
   * Generated method.
   */
  Optional<Market> findById(UUID id);
}
