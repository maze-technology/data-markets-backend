package tech.maze.data.markets.backend.api.search;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.google.protobuf.Value;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.maze.data.markets.backend.api.mappers.MarketTypeDtoMapper;
import org.mapstruct.factory.Mappers;
import tech.maze.commons.mappers.ProtobufValueMapper;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.data.markets.backend.domain.models.MarketType;
import tech.maze.data.markets.backend.domain.ports.in.FindMarketUseCase;

@ExtendWith(MockitoExtension.class)
class FindOneMarketByTypeAndExchangeIdAndBaseIdAndQuoteIdSearchStrategyTest {
  private static final ProtobufValueMapper PROTOBUF_VALUE_MAPPER =
      Mappers.getMapper(ProtobufValueMapper.class);

  @Mock
  private FindMarketUseCase findMarketUseCase;
  @Mock
  private Market market;

  @Test
  void supportsOnlyWhenTypeAndIdentifiersArePresent() {
    final var strategy = new FindOneMarketByTypeAndExchangeIdAndBaseIdAndQuoteIdSearchStrategy(
        findMarketUseCase,
        new MarketTypeDtoMapper(),
        PROTOBUF_VALUE_MAPPER
    );
    final var valid = criterion("binance", "BTC", "USDT", tech.maze.dtos.markets.enums.Type.SPOT);

    assertThat(strategy.supports(valid)).isTrue();
    assertThat(strategy.supports(criterion("", "BTC", "USDT", tech.maze.dtos.markets.enums.Type.SPOT))).isFalse();
  }

  @Test
  void searchDelegatesToFindUseCase() {
    final var strategy = new FindOneMarketByTypeAndExchangeIdAndBaseIdAndQuoteIdSearchStrategy(
        findMarketUseCase,
        new MarketTypeDtoMapper(),
        PROTOBUF_VALUE_MAPPER
    );
    final var criterion = criterion("binance", "BTC", "USDT", tech.maze.dtos.markets.enums.Type.SPOT);
    when(findMarketUseCase.findByTypeAndExchangeAndBaseAndQuote(MarketType.SPOT, "binance", "BTC", "USDT"))
        .thenReturn(Optional.of(market));

    final var result = strategy.search(criterion);

    assertThat(result).contains(market);
    verify(findMarketUseCase).findByTypeAndExchangeAndBaseAndQuote(MarketType.SPOT, "binance", "BTC", "USDT");
  }

  @Test
  void searchReturnsEmptyWhenTypeIsUnsupported() {
    final var strategy = new FindOneMarketByTypeAndExchangeIdAndBaseIdAndQuoteIdSearchStrategy(
        findMarketUseCase,
        new MarketTypeDtoMapper(),
        PROTOBUF_VALUE_MAPPER
    );
    final var criterion = criterionWithUnknownType("binance", "BTC", "USDT");

    final var result = strategy.search(criterion);

    assertThat(result).isEmpty();
    verifyNoInteractions(findMarketUseCase);
  }

  private static tech.maze.dtos.markets.search.Criterion criterion(
      String exchangeId,
      String baseId,
      String quoteId,
      tech.maze.dtos.markets.enums.Type type
  ) {
    return tech.maze.dtos.markets.search.Criterion.newBuilder()
        .setFilter(
            tech.maze.dtos.markets.search.CriterionFilter.newBuilder()
                .setByTypeAndExchangeIdAndBaseIdAndQuoteId(
                    tech.maze.dtos.markets.search.CriterionFilterByTypeAndExchangeIdAndBaseIdAndQuoteId
                        .newBuilder()
                        .setType(
                            tech.maze.dtos.markets.search.CriteriaFilterByType.newBuilder()
                                .setFilter(
                                    tech.maze.dtos.markets.search.CriteriaFilterByTypeFilter.newBuilder()
                                        .setEnum(type)
                                        .build()
                                )
                                .build()
                        )
                        .setExchangeId(Value.newBuilder().setStringValue(exchangeId).build())
                        .setBaseId(Value.newBuilder().setStringValue(baseId).build())
                        .setQuoteId(Value.newBuilder().setStringValue(quoteId).build())
                        .build()
                )
                .build()
        )
        .build();
  }

  private static tech.maze.dtos.markets.search.Criterion criterionWithUnknownType(
      String exchangeId,
      String baseId,
      String quoteId
  ) {
    return tech.maze.dtos.markets.search.Criterion.newBuilder()
        .setFilter(
            tech.maze.dtos.markets.search.CriterionFilter.newBuilder()
                .setByTypeAndExchangeIdAndBaseIdAndQuoteId(
                    tech.maze.dtos.markets.search.CriterionFilterByTypeAndExchangeIdAndBaseIdAndQuoteId
                        .newBuilder()
                        .setType(
                            tech.maze.dtos.markets.search.CriteriaFilterByType.newBuilder()
                                .setFilter(
                                    tech.maze.dtos.markets.search.CriteriaFilterByTypeFilter.newBuilder()
                                        .setEnumValue(999)
                                        .build()
                                )
                                .build()
                        )
                        .setExchangeId(Value.newBuilder().setStringValue(exchangeId).build())
                        .setBaseId(Value.newBuilder().setStringValue(baseId).build())
                        .setQuoteId(Value.newBuilder().setStringValue(quoteId).build())
                        .build()
                )
                .build()
        )
        .build();
  }
}
