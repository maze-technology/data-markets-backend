package tech.maze.data.markets.backend.api.search;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.maze.data.markets.backend.api.mappers.MarketTypeDtoMapper;
import tech.maze.data.markets.backend.api.support.CriterionValueExtractor;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.data.markets.backend.domain.models.MarketType;
import tech.maze.data.markets.backend.domain.ports.in.FindMarketUseCase;
import tech.maze.dtos.markets.search.CriteriaFilterByTypeFilter;
import tech.maze.dtos.markets.search.Criterion;
import tech.maze.dtos.markets.search.CriterionFilterByTypeAndExchangeIdAndBaseIdAndQuoteId;

/**
 * FindOne strategy that resolves markets by type/exchange/base/quote.
 */
@Service
@RequiredArgsConstructor
public class FindOneMarketByTypeAndExchangeIdAndBaseIdAndQuoteIdSearchStrategy
    implements FindOneMarketSearchStrategy {
  private final FindMarketUseCase findMarketUseCase;
  private final MarketTypeDtoMapper marketTypeDtoMapper;
  private final CriterionValueExtractor criterionValueExtractor;

  @Override
  public boolean supports(Criterion criterion) {
    if (criterion == null
        || !criterion.hasFilter()
        || !criterion.getFilter().hasByTypeAndExchangeIdAndBaseIdAndQuoteId()) {
      return false;
    }

    CriterionFilterByTypeAndExchangeIdAndBaseIdAndQuoteId filter =
        criterion.getFilter().getByTypeAndExchangeIdAndBaseIdAndQuoteId();
    if (!filter.hasType()
        || !filter.hasExchangeId()
        || !filter.hasBaseId()
        || !filter.hasQuoteId()) {
      return false;
    }

    return extractMarketType(filter.getType().getFilter()).isPresent()
        && criterionValueExtractor.extractString(filter.getExchangeId()).isPresent()
        && criterionValueExtractor.extractString(filter.getBaseId()).isPresent()
        && criterionValueExtractor.extractString(filter.getQuoteId()).isPresent();
  }

  @Override
  public Optional<Market> search(Criterion criterion) {
    CriterionFilterByTypeAndExchangeIdAndBaseIdAndQuoteId filter =
        criterion.getFilter().getByTypeAndExchangeIdAndBaseIdAndQuoteId();
    Optional<MarketType> marketType = extractMarketType(filter.getType().getFilter());
    Optional<String> exchangeId = criterionValueExtractor.extractString(filter.getExchangeId());
    Optional<String> baseId = criterionValueExtractor.extractString(filter.getBaseId());
    Optional<String> quoteId = criterionValueExtractor.extractString(filter.getQuoteId());
    if (marketType.isEmpty() || exchangeId.isEmpty() || baseId.isEmpty() || quoteId.isEmpty()) {
      return Optional.empty();
    }

    return findMarketUseCase.findByTypeAndExchangeAndBaseAndQuote(
        marketType.get(),
        exchangeId.get(),
        baseId.get(),
        quoteId.get()
    );
  }

  private Optional<MarketType> extractMarketType(CriteriaFilterByTypeFilter filter) {
    if (filter == null || !filter.hasEnum()) {
      return Optional.empty();
    }

    return Optional.ofNullable(marketTypeDtoMapper.toDomain(filter.getEnum()));
  }
}
