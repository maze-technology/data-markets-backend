package tech.maze.data.markets.backend.domain.ports.in;

import java.util.List;
import tech.maze.data.markets.backend.domain.models.Market;

public interface SearchMarketsUseCase {
  List<Market> findAll();
}
