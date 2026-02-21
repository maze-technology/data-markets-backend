package tech.maze.data.markets.backend.api.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.data.markets.backend.domain.models.MarketType;

class MarketDtoMapperTest {
  private final MarketDtoMapper mapper = new MarketDtoMapper() {
    @Override
    public tech.maze.dtos.markets.models.Market toDto(Market market) {
      return null;
    }
  };

  @Test
  void marketTypeToDtoMapsValues() {
    assertThat(mapper.marketTypeToDto(null)).isEqualTo(tech.maze.dtos.markets.enums.Type.UNRECOGNIZED);
    assertThat(mapper.marketTypeToDto(MarketType.SPOT)).isEqualTo(tech.maze.dtos.markets.enums.Type.SPOT);
    assertThat(mapper.marketTypeToDto(MarketType.PERP)).isEqualTo(tech.maze.dtos.markets.enums.Type.PERPETUAL);
    assertThat(mapper.marketTypeToDto(MarketType.FUTURES)).isEqualTo(tech.maze.dtos.markets.enums.Type.PERPETUAL);
    assertThat(mapper.marketTypeToDto(MarketType.OPTION)).isEqualTo(tech.maze.dtos.markets.enums.Type.PERPETUAL);
  }

  @Test
  void toOptionSpecificDataReturnsNullForNow() {
    assertThat(mapper.toOptionSpecificData(tech.maze.dtos.markets.models.Market.getDefaultInstance())).isNull();
  }
}
