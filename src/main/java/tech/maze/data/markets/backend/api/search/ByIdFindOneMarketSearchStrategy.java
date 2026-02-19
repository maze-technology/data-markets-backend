package tech.maze.data.markets.backend.api.search;

import com.google.protobuf.Value;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.data.markets.backend.domain.ports.in.FindMarketUseCase;
import tech.maze.dtos.markets.search.Criterion;

/**
 * FindOne strategy that resolves markets by criterion.filter.byId.
 */
@Service
@RequiredArgsConstructor
public class ByIdFindOneMarketSearchStrategy implements FindOneMarketSearchStrategy {
  private final FindMarketUseCase findMarketUseCase;

  @Override
  public boolean supports(Criterion criterion) {
    return criterion != null
        && criterion.hasFilter()
        && criterion.getFilter().hasById()
        && criterion.getFilter().getById().hasStringValue();
  }

  @Override
  public Optional<Market> search(Criterion criterion) {
    Value value = criterion.getFilter().getById();
    try {
      UUID id = UUID.fromString(value.getStringValue());
      return findMarketUseCase.findById(id);
    } catch (IllegalArgumentException ex) {
      return Optional.empty();
    }
  }
}
