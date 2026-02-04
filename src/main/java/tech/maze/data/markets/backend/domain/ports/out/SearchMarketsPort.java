package tech.maze.data.markets.backend.domain.ports.out;

import java.util.List;
import tech.maze.data.markets.backend.domain.models.Market;

public interface SearchMarketsPort {
  List<Market> findAll();
}
