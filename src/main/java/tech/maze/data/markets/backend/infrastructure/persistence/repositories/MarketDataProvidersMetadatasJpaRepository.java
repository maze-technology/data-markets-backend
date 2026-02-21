package tech.maze.data.markets.backend.infrastructure.persistence.repositories;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.maze.data.markets.backend.infrastructure.persistence.entities.MarketDataProviderMetaDataEntity;

/**
 * JPA repository for market data-provider metadata.
 */
@Repository
public interface MarketDataProvidersMetadatasJpaRepository
    extends JpaRepository<MarketDataProviderMetaDataEntity, Long> {
  /**
   * Finds one metadata record by market id and data provider id.
   */
  Optional<MarketDataProviderMetaDataEntity> findFirstByMarketIdAndDataProviderId(
      UUID marketId,
      UUID dataProviderId
  );
}
