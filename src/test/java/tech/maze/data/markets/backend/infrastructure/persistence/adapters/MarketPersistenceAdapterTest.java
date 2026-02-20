package tech.maze.data.markets.backend.infrastructure.persistence.adapters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.data.markets.backend.domain.models.MarketType;
import tech.maze.data.markets.backend.infrastructure.persistence.entities.MarketEntity;
import tech.maze.data.markets.backend.infrastructure.persistence.mappers.MarketEntityMapper;
import tech.maze.data.markets.backend.infrastructure.persistence.repositories.MarketJpaRepository;

@ExtendWith(MockitoExtension.class)
class MarketPersistenceAdapterTest {
  @Mock
  private MarketJpaRepository marketJpaRepository;
  @Mock
  private MarketEntityMapper marketEntityMapper;
  @Mock
  private MarketEntity entity;
  @Mock
  private Market market;

  @Test
  void findByIdMapsEntityToDomain() {
    final UUID id = UUID.randomUUID();
    when(marketJpaRepository.findById(id)).thenReturn(Optional.of(entity));
    when(marketEntityMapper.toDomain(entity)).thenReturn(market);

    final var adapter = new MarketPersistenceAdapter(marketJpaRepository, marketEntityMapper);
    final var result = adapter.findById(id);

    assertThat(result).contains(market);
    verify(marketJpaRepository).findById(id);
  }

  @Test
  void findAllMapsEntitiesToDomain() {
    when(marketJpaRepository.findAll()).thenReturn(List.of(entity));
    when(marketEntityMapper.toDomain(entity)).thenReturn(market);

    final var adapter = new MarketPersistenceAdapter(marketJpaRepository, marketEntityMapper);
    final var result = adapter.findAll();

    assertThat(result).containsExactly(market);
    verify(marketJpaRepository).findAll();
  }

  @Test
  void findByDataProviderIdsReturnsEmptyWhenInputIsEmpty() {
    final var adapter = new MarketPersistenceAdapter(marketJpaRepository, marketEntityMapper);

    final var result = adapter.findByDataProviderIds(List.of());

    assertThat(result).isEmpty();
  }

  @Test
  void findByDataProviderIdsDelegatesToRepository() {
    final UUID dataProviderId = UUID.randomUUID();
    when(marketJpaRepository.findAllByDataProviderIds(List.of(dataProviderId.toString()))).thenReturn(List.of(entity));
    when(marketEntityMapper.toDomain(entity)).thenReturn(market);

    final var adapter = new MarketPersistenceAdapter(marketJpaRepository, marketEntityMapper);
    final var result = adapter.findByDataProviderIds(List.of(dataProviderId));

    assertThat(result).containsExactly(market);
    verify(marketJpaRepository).findAllByDataProviderIds(List.of(dataProviderId.toString()));
  }

  @Test
  void findByTypeAndExchangeAndBaseAndQuoteDelegatesToRepository() {
    when(marketJpaRepository.findFirstByTypeAndExchangeIgnoreCaseAndBaseIgnoreCaseAndQuoteIgnoreCase(
        MarketType.SPOT,
        "binance",
        "BTC",
        "USDT"
    )).thenReturn(Optional.of(entity));
    when(marketEntityMapper.toDomain(entity)).thenReturn(market);

    final var adapter = new MarketPersistenceAdapter(marketJpaRepository, marketEntityMapper);
    final var result = adapter.findByTypeAndExchangeAndBaseAndQuote(MarketType.SPOT, "binance", "BTC", "USDT");

    assertThat(result).contains(market);
  }

  @Test
  void saveMapsDomainToEntityAndBack() {
    when(marketEntityMapper.toEntity(market)).thenReturn(entity);
    when(marketJpaRepository.save(entity)).thenReturn(entity);
    when(marketEntityMapper.toDomain(entity)).thenReturn(market);

    final var adapter = new MarketPersistenceAdapter(marketJpaRepository, marketEntityMapper);
    final var result = adapter.save(market);

    assertThat(result).isSameAs(market);
    verify(marketEntityMapper).toEntity(market);
    verify(marketJpaRepository).save(entity);
    verify(marketEntityMapper).toDomain(entity);
  }
}
