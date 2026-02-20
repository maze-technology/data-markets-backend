package tech.maze.data.markets.backend.api.mappers;

import org.springframework.stereotype.Component;
import tech.maze.data.markets.backend.domain.models.MarketType;

/**
 * Maps between market type DTO and domain enums.
 */
@Component
public class MarketTypeDtoMapper {
  /**
   * Converts a DTO market type to a domain market type.
   */
  public MarketType toDomain(tech.maze.dtos.markets.enums.Type value) {
    if (value == null) {
      return null;
    }

    return switch (value) {
      case SPOT -> MarketType.SPOT;
      case PERPETUAL -> MarketType.PERP;
      case UNRECOGNIZED -> null;
      default -> null;
    };
  }
}
