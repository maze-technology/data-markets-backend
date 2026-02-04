package tech.maze.data.markets.backend.infrastructure.persistence.mappers;

import org.mapstruct.Mapper;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.data.markets.backend.infrastructure.persistence.entities.MarketEntity;

@Mapper(componentModel = "spring")
/**
 * Generated type.
 */
public interface MarketEntityMapper {
  /**
   * Generated method.
   */
  Market toDomain(MarketEntity entity);
  /**
   * Generated method.
   */
  MarketEntity toEntity(Market market);

  default tech.maze.data.markets.backend.infrastructure.persistence.entities.OptionSpecificDataEmbeddable toEmbeddable(
      tech.maze.data.markets.backend.domain.models.OptionSpecificData optionSpecificData) {
    if (optionSpecificData == null) {
      return null;
    }
    final tech.maze.data.markets.backend.infrastructure.persistence.entities.OptionSpecificDataEmbeddable embeddable =
  /**
   * Generated method.
   */
        new tech.maze.data.markets.backend.infrastructure.persistence.entities.OptionSpecificDataEmbeddable();
  /**
   * Generated method.
   */
    embeddable.setStrike(optionSpecificData.strike());
  /**
   * Generated method.
   */
    embeddable.setExpiredAt(optionSpecificData.expiredAt());
  /**
   * Generated method.
   */
    embeddable.setType(optionSpecificData.type());
  /**
   * Generated method.
   */
    embeddable.setStyle(optionSpecificData.style());
  /**
   * Generated method.
   */
    embeddable.setUnderlying(optionSpecificData.underlying());
    return embeddable;
  }

  default tech.maze.data.markets.backend.domain.models.OptionSpecificData toDomain(
      tech.maze.data.markets.backend.infrastructure.persistence.entities.OptionSpecificDataEmbeddable embeddable) {
    if (embeddable == null) {
      return null;
    }
    return new tech.maze.data.markets.backend.domain.models.OptionSpecificData(
        embeddable.getStrike() == null ? 0.0 : embeddable.getStrike(),
        embeddable.getExpiredAt(),
        embeddable.getType(),
        embeddable.getStyle(),
        embeddable.getUnderlying()
    );
  }
}
