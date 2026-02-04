package tech.maze.data.markets.backend.domain.ports.in;

import tech.maze.data.markets.backend.domain.models.Market;

public interface SaveMarketUseCase {
  Market save(Market market);
}
