package tech.maze.data.markets.backend.domain.ports.in;

import java.util.List;
import java.util.UUID;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.data.markets.backend.domain.models.MarketsPage;

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
  MarketsPage findByDataProviderIds(List<UUID> dataProviderIds, int page, int limit);
}
