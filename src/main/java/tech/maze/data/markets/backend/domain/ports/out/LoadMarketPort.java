package tech.maze.data.markets.backend.domain.ports.out;

import java.util.Optional;
import java.util.UUID;
import tech.maze.data.markets.backend.domain.models.Market;

public interface LoadMarketPort {
  Optional<Market> findById(UUID id);
}
