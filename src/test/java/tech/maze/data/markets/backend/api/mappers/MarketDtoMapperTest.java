package tech.maze.data.markets.backend.api.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.data.markets.backend.domain.models.MarketType;

class MarketDtoMapperTest {
  private final MarketTypeDtoMapper marketTypeDtoMapper = new MarketTypeDtoMapper();
  private final MarketDtoMapper mapper = new MarketDtoMapper() {
    @Override
    public tech.maze.dtos.markets.models.Market toDto(Market market) {
      return null;
    }
  };

  @Test
  void marketTypeToDtoMapsValues() {
    assertThat(marketTypeDtoMapper.toDto(null)).isEqualTo(tech.maze.dtos.markets.enums.Type.UNRECOGNIZED);
    assertThat(marketTypeDtoMapper.toDto(MarketType.SPOT)).isEqualTo(tech.maze.dtos.markets.enums.Type.SPOT);
    assertThat(marketTypeDtoMapper.toDto(MarketType.PERP)).isEqualTo(tech.maze.dtos.markets.enums.Type.PERPETUAL);
    assertThat(marketTypeDtoMapper.toDto(MarketType.FUTURES)).isEqualTo(tech.maze.dtos.markets.enums.Type.PERPETUAL);
    assertThat(marketTypeDtoMapper.toDto(MarketType.OPTION)).isEqualTo(tech.maze.dtos.markets.enums.Type.PERPETUAL);
  }
}
