package tech.maze.data.markets.backend.api.search;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.maze.data.markets.backend.api.support.CriterionValueExtractor;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.data.markets.backend.domain.ports.in.FindMarketUseCase;
import tech.maze.dtos.markets.search.Criterion;

/**
 * FindOne strategy that resolves markets by criterion.filter.byId.
 */
@Service
@RequiredArgsConstructor
public class FindOneMarketByIdSearchStrategy implements FindOneMarketSearchStrategy {
  private final FindMarketUseCase findMarketUseCase;
  private final CriterionValueExtractor criterionValueExtractor;

  @Override
  public boolean supports(Criterion criterion) {
    return criterion != null
        && criterion.hasFilter()
        && criterion.getFilter().hasById()
        && criterion.getFilter().getById().hasStringValue();
  }

  @Override
  public Optional<Market> search(Criterion criterion) {
    return criterionValueExtractor.extractUuid(criterion.getFilter().getById())
        .flatMap(findMarketUseCase::findById);
  }
}
