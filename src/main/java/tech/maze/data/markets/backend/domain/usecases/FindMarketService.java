package tech.maze.data.markets.backend.domain.usecases;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.data.markets.backend.domain.models.MarketType;
import tech.maze.data.markets.backend.domain.ports.in.FindMarketUseCase;
import tech.maze.data.markets.backend.domain.ports.out.LoadMarketPort;

/**
 * Use case for loading a market by id.
 */
@Service
@RequiredArgsConstructor
public class FindMarketService implements FindMarketUseCase {
  private final LoadMarketPort loadMarketPort;

  @Override
  public Optional<Market> findById(UUID id) {
    return loadMarketPort.findById(id);
  }

  @Override
  public Optional<Market> findByTypeAndExchangeAndBaseAndQuote(
      MarketType type,
      String exchange,
      String base,
      String quote
  ) {
    return loadMarketPort.findByTypeAndExchangeAndBaseAndQuote(type, exchange, base, quote);
  }
}
