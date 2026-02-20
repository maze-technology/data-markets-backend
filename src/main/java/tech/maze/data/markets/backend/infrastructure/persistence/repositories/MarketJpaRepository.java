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
      value = """
          SELECT DISTINCT m.*
          FROM markets m
          WHERE EXISTS (
            SELECT 1
            FROM jsonb_array_elements(
                COALESCE(m.data_providers_meta_datas::jsonb, '[]'::jsonb)
            ) AS metadata
            WHERE COALESCE(
                metadata ->> 'dataProviderId',
                metadata ->> 'data_provider_id'
            ) IN (:dataProviderIds)
          )
          """,
      nativeQuery = true
  )
  java.util.List<MarketEntity> findAllByDataProviderIds(
      @Param("dataProviderIds") java.util.List<String> dataProviderIds
  );
}
