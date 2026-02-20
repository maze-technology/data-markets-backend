package tech.maze.data.markets.backend.infrastructure.persistence.adapters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.data.markets.backend.domain.models.MarketType;
import tech.maze.data.markets.backend.domain.ports.out.LoadMarketPort;
import tech.maze.data.markets.backend.domain.ports.out.SaveMarketPort;
import tech.maze.data.markets.backend.domain.ports.out.SearchMarketsPort;
import tech.maze.data.markets.backend.infrastructure.persistence.entities.MarketEntity;
import tech.maze.data.markets.backend.infrastructure.persistence.mappers.MarketEntityMapper;
import tech.maze.data.markets.backend.infrastructure.persistence.repositories.MarketJpaRepository;

/**
 * Persistence adapter for markets.
 */
@Component
@RequiredArgsConstructor
public class MarketPersistenceAdapter implements LoadMarketPort, SaveMarketPort, SearchMarketsPort {
  private final MarketJpaRepository marketJpaRepository;
  private final MarketEntityMapper marketEntityMapper;

  @Override
  public Optional<Market> findById(UUID id) {
    return marketJpaRepository.findById(id).map(marketEntityMapper::toDomain);
  }

  @Override
  public Optional<Market> findByTypeAndExchangeAndBaseAndQuote(
      MarketType type,
      String exchange,
      String base,
      String quote
  ) {
    return marketJpaRepository
        .findFirstByTypeAndExchangeIgnoreCaseAndBaseIgnoreCaseAndQuoteIgnoreCase(
            type,
            exchange,
            base,
            quote
        )
        .map(marketEntityMapper::toDomain);
  }

  @Override
  public List<Market> findAll() {
    return marketJpaRepository.findAll().stream().map(marketEntityMapper::toDomain).toList();
  }

  @Override
  public List<Market> findByDataProviderIds(List<UUID> dataProviderIds) {
    if (dataProviderIds == null || dataProviderIds.isEmpty()) {
      return List.of();
    }

    List<String> serializedDataProviderIds = dataProviderIds.stream()
        .map(UUID::toString)
        .toList();
    return marketJpaRepository.findAllByDataProviderIds(serializedDataProviderIds).stream()
        .map(marketEntityMapper::toDomain)
        .toList();
  }

  @Override
  public Market save(Market market) {
    final MarketEntity saved = marketJpaRepository.save(marketEntityMapper.toEntity(market));
    return marketEntityMapper.toDomain(saved);
  }
}
