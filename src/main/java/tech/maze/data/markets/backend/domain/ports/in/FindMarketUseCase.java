package tech.maze.data.markets.backend.domain.ports.in;

import java.util.Optional;
import java.util.UUID;
import tech.maze.data.markets.backend.domain.models.Market;

public interface FindMarketUseCase {
  Optional<Market> findById(UUID id);
}
