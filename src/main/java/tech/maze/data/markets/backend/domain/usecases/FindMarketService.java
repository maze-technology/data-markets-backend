package tech.maze.data.markets.backend.domain.usecases;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.data.markets.backend.domain.ports.in.FindMarketUseCase;
import tech.maze.data.markets.backend.domain.ports.out.LoadMarketPort;

@Service
@RequiredArgsConstructor
public class FindMarketService implements FindMarketUseCase {
  private final LoadMarketPort loadMarketPort;

  @Override
  public Optional<Market> findById(UUID id) {
    return loadMarketPort.findById(id);
  }
}
