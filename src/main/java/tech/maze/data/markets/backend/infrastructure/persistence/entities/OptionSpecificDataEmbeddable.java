package tech.maze.data.markets.backend.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.maze.data.markets.backend.domain.models.OptionStyle;
import tech.maze.data.markets.backend.domain.models.OptionType;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class OptionSpecificDataEmbeddable {
  @Column(name = "option_strike")
  private Double strike;

  @Column(name = "option_expired_at")
  private Instant expiredAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "option_type")
  private OptionType type;

  @Enumerated(EnumType.STRING)
  @Column(name = "option_style")
  private OptionStyle style;

  @Column(name = "option_underlying")
  private String underlying;
}
