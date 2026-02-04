package tech.maze.data.markets.backend.infrastructure.persistence.mappers;

import org.mapstruct.Mapper;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.data.markets.backend.infrastructure.persistence.entities.MarketEntity;

@Mapper(componentModel = "spring")
public interface MarketEntityMapper {
  Market toDomain(MarketEntity entity);
  MarketEntity toEntity(Market market);

  default tech.maze.data.markets.backend.infrastructure.persistence.entities.OptionSpecificDataEmbeddable toEmbeddable(
      tech.maze.data.markets.backend.domain.models.OptionSpecificData optionSpecificData) {
    if (optionSpecificData == null) {
      return null;
    }
    final tech.maze.data.markets.backend.infrastructure.persistence.entities.OptionSpecificDataEmbeddable embeddable =
        new tech.maze.data.markets.backend.infrastructure.persistence.entities.OptionSpecificDataEmbeddable();
    embeddable.setStrike(optionSpecificData.strike());
    embeddable.setExpiredAt(optionSpecificData.expiredAt());
    embeddable.setType(optionSpecificData.type());
    embeddable.setStyle(optionSpecificData.style());
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
