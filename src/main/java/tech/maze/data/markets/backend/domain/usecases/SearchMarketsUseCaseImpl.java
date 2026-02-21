package tech.maze.data.markets.backend.domain.usecases;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.data.markets.backend.domain.models.MarketsPage;
import tech.maze.data.markets.backend.domain.ports.in.SearchMarketsUseCase;
import tech.maze.data.markets.backend.domain.ports.out.SearchMarketsPort;

/**
 * Use case for listing markets.
 */
@Service
@RequiredArgsConstructor
public class SearchMarketsUseCaseImpl implements SearchMarketsUseCase {
  private final SearchMarketsPort searchMarketsPort;

  @Override
  public List<Market> findAll() {
    return searchMarketsPort.findAll();
  }

  @Override
  public MarketsPage findByDataProviderIds(List<UUID> dataProviderIds, int page, int limit) {
    return searchMarketsPort.findByDataProviderIds(dataProviderIds, page, limit);
  }
}
