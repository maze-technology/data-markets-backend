package tech.maze.data.markets.backend.domain.ports.out;

import java.util.List;
import java.util.UUID;
import tech.maze.data.markets.backend.domain.models.Market;

/**
 * Generated type.
 */
public interface SearchMarketsPort {
  /**
   * Generated method.
   */
  List<Market> findAll();

  /**
   * Generated method.
   */
  List<Market> findByDataProviderIds(List<UUID> dataProviderIds);
}
