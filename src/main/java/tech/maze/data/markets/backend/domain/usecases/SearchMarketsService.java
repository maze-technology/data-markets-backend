package tech.maze.data.markets.backend.domain.usecases;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.data.markets.backend.domain.ports.in.SearchMarketsUseCase;
import tech.maze.data.markets.backend.domain.ports.out.SearchMarketsPort;

@Service
@RequiredArgsConstructor
/**
 * Generated type.
 */
public class SearchMarketsService implements SearchMarketsUseCase {
  private final SearchMarketsPort searchMarketsPort;

  @Override
  public List<Market> findAll() {
    return searchMarketsPort.findAll();
  }
}
