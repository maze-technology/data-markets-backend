package tech.maze.data.markets.backend.api.mappers;

import com.google.protobuf.Value;
import java.util.UUID;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import tech.maze.commons.mappers.BaseDtoMapper;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.data.markets.backend.domain.models.MarketType;
import tech.maze.data.markets.backend.domain.models.OptionSpecificData;

/**
 * Maps between market domain and DTO models.
 */
@Mapper(
    componentModel = "spring",
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {
      BaseDtoMapper.class
    }
)
public interface MarketDtoMapper {
  /**
   * Maps a domain market to its DTO representation.
   */
  @Mapping(target = "id", source = "id", qualifiedByName = "uuidToValue")
  @Mapping(target = "type", source = "type", qualifiedByName = "marketTypeToDto")
  @Mapping(target = "exchangeId", source = "exchange", qualifiedByName = "stringToValue")
  @Mapping(target = "baseId", source = "base", qualifiedByName = "stringToValue")
  @Mapping(target = "quoteId", source = "quote", qualifiedByName = "stringToValue")
  tech.maze.dtos.markets.models.Market toDto(Market market);

  /**
   * Included for future request-to-domain expansion.
   */
  default OptionSpecificData toOptionSpecificData(tech.maze.dtos.markets.models.Market market) {
    return null;
  }

  /**
   * Converts UUID values to protobuf {@link Value} wrappers.
   */
  @Named("uuidToValue")
  default Value uuidToValue(UUID value) {
    if (value == null) {
      return Value.getDefaultInstance();
    }
    return Value.newBuilder().setStringValue(value.toString()).build();
  }

  /**
   * Converts string values to protobuf {@link Value} wrappers.
   */
  @Named("stringToValue")
  default Value stringToValue(String value) {
    if (value == null) {
      return Value.getDefaultInstance();
    }
    return Value.newBuilder().setStringValue(value).build();
  }

  /**
   * Converts domain market type values to DTO enum values.
   */
  @Named("marketTypeToDto")
  default tech.maze.dtos.markets.enums.Type marketTypeToDto(MarketType value) {
    if (value == null) {
      return tech.maze.dtos.markets.enums.Type.UNRECOGNIZED;
    }

    return switch (value) {
      case SPOT -> tech.maze.dtos.markets.enums.Type.SPOT;
      case PERP -> tech.maze.dtos.markets.enums.Type.PERPETUAL;
      case FUTURES -> tech.maze.dtos.markets.enums.Type.PERPETUAL;
      case OPTION -> tech.maze.dtos.markets.enums.Type.PERPETUAL;
    };
  }
}
