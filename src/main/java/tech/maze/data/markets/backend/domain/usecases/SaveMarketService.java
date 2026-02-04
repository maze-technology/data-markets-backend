package tech.maze.data.markets.backend.domain.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.data.markets.backend.domain.ports.in.SaveMarketUseCase;
import tech.maze.data.markets.backend.domain.ports.out.SaveMarketPort;

@Service
@RequiredArgsConstructor
public class SaveMarketService implements SaveMarketUseCase {
  private final SaveMarketPort saveMarketPort;

  @Override
  public Market save(Market market) {
    return saveMarketPort.save(market);
  }
}
