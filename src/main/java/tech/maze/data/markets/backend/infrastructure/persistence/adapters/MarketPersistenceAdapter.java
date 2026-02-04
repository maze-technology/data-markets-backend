package tech.maze.data.markets.backend.infrastructure.persistence.adapters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.data.markets.backend.domain.ports.out.LoadMarketPort;
import tech.maze.data.markets.backend.domain.ports.out.SaveMarketPort;
import tech.maze.data.markets.backend.domain.ports.out.SearchMarketsPort;
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
  public List<Market> findAll() {
    return marketJpaRepository.findAll().stream().map(marketEntityMapper::toDomain).toList();
  }

  @Override
  public Market save(Market market) {
    return marketEntityMapper.toDomain(marketJpaRepository.save(marketEntityMapper.toEntity(market)));
  }
}
