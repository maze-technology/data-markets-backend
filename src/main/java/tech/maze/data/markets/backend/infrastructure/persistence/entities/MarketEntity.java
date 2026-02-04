package tech.maze.data.markets.backend.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.maze.data.markets.backend.domain.models.MarketType;

@Entity
@Table(name = "markets")
@Getter
@Setter
@NoArgsConstructor
/**
 * Generated type.
 */
public class MarketEntity {
  @Id
  private UUID id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private MarketType type;

  @Column(nullable = false)
  private String exchange;

  @Column(nullable = false)
  private String base;

  @Column(nullable = false)
  private String quote;

  @Embedded
  private OptionSpecificDataEmbeddable optionSpecificData;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;
}
