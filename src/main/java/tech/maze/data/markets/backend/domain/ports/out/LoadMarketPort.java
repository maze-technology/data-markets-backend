package tech.maze.data.markets.backend.domain.ports.out;

import java.util.Optional;
import java.util.UUID;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.data.markets.backend.domain.models.MarketType;

/**
 * Generated type.
 */
public interface LoadMarketPort {
  /**
   * Generated method.
   */
  Optional<Market> findById(UUID id);

  /**
   * Generated method.
   */
  Optional<Market> findByTypeAndExchangeAndBaseAndQuote(
      MarketType type,
      String exchange,
      String base,
      String quote
  );
}
