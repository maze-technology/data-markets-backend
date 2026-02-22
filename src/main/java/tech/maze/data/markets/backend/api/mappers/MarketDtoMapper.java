package tech.maze.data.markets.backend.api.mappers;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import tech.maze.commons.mappers.BaseDtoMapper;
import tech.maze.commons.mappers.ProtobufValueMapper;
import tech.maze.data.markets.backend.domain.models.Market;

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
      BaseDtoMapper.class,
      ProtobufValueMapper.class,
      MarketTypeDtoMapper.class
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

}
