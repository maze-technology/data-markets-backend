package tech.maze.data.markets.backend.domain.ports.out;

import tech.maze.data.markets.backend.domain.models.Market;

public interface SaveMarketPort {
  Market save(Market market);
}
