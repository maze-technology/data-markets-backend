package tech.maze.data.markets.backend.domain.ports.in;

import java.util.List;
import java.util.UUID;
import tech.maze.data.markets.backend.domain.models.Market;

/**
 * Generated type.
 */
public interface SearchMarketsUseCase {
  /**
   * Generated method.
   */
  List<Market> findAll();

  /**
   * Generated method.
   */
  List<Market> findByDataProviderIds(List<UUID> dataProviderIds);
}
