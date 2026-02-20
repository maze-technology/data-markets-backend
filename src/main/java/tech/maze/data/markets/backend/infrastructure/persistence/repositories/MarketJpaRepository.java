package tech.maze.data.markets.backend.infrastructure.persistence.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.maze.data.markets.backend.domain.models.MarketType;
import tech.maze.data.markets.backend.infrastructure.persistence.entities.MarketEntity;

/**
 * JPA repository for markets.
 */
@Repository
public interface MarketJpaRepository extends JpaRepository<MarketEntity, UUID> {
  /**
   * Finds one market by type/exchange/base/quote.
   */
  java.util.Optional<MarketEntity>
      findFirstByTypeAndExchangeIgnoreCaseAndBaseIgnoreCaseAndQuoteIgnoreCase(
      MarketType type,
      String exchange,
      String base,
      String quote
  );

  /**
   * Finds markets linked to any data provider ids.
   */
  @Query(
      """
          SELECT DISTINCT m
          FROM MarketEntity m
          JOIN m.dataProvidersMetaDatas d
          WHERE d.dataProviderId IN :dataProviderIds
          """
  )
  java.util.List<MarketEntity> findAllByDataProviderIds(
      @Param("dataProviderIds") java.util.List<UUID> dataProviderIds
  );
}
