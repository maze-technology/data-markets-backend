package tech.maze.data.markets.backend.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * JPA entity for market data-provider metadata.
 */
@Entity
@Table(name = "markets_dataproviders_metadatas")
@Getter
@Setter
@NoArgsConstructor
public class MarketDataProviderMetaDataEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "market_id", nullable = false)
  private MarketEntity market;

  @Column(name = "data_provider_id", nullable = false)
  private UUID dataProviderId;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "extra_datas")
  private Map<String, String> extraDatas;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "tool_box")
  private Map<String, String> toolBox;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;
}
