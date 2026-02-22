package tech.maze.data.markets.backend.infrastructure.persistence.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.data.markets.backend.domain.models.OptionSpecificData;
import tech.maze.data.markets.backend.infrastructure.persistence.entities.MarketEntity;
import tech.maze.data.markets.backend.infrastructure.persistence.entities.OptionSpecificDataEmbeddable;

/**
 * Maps market entities to domain models and back.
 */
@Mapper(componentModel = "spring")
public interface MarketEntityMapper {
  /**
   * Maps a market entity into its domain model.
   */
  Market toDomain(MarketEntity entity);

  /**
   * Maps option specific embeddable data into the domain model.
   */
  default OptionSpecificData toDomain(OptionSpecificDataEmbeddable embeddable) {
    if (embeddable == null) {
      return null;
    }
    final Double strike = embeddable.getStrike();
    final double strikeValue = strike == null ? 0.0 : strike;
    return new OptionSpecificData(
        strikeValue,
        embeddable.getExpiredAt(),
        embeddable.getType(),
        embeddable.getStyle(),
        embeddable.getUnderlying()
    );
  }

  /**
   * Maps a market domain model into its entity representation.
   */
  @Mapping(target = "dataProvidersMetaDatas", ignore = true)
  MarketEntity toEntity(Market market);

  /**
   * Maps option specific data into its embeddable representation.
   */
  default OptionSpecificDataEmbeddable toEmbeddable(OptionSpecificData optionSpecificData) {
    if (optionSpecificData == null) {
      return null;
    }
    final OptionSpecificDataEmbeddable embeddable = new OptionSpecificDataEmbeddable();
    embeddable.setStrike(optionSpecificData.strike());
    embeddable.setExpiredAt(optionSpecificData.expiredAt());
    embeddable.setType(optionSpecificData.type());
    embeddable.setStyle(optionSpecificData.style());
    embeddable.setUnderlying(optionSpecificData.underlying());
    return embeddable;
  }
}
