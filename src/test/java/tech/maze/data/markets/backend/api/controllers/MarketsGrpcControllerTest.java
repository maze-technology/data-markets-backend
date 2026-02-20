package tech.maze.data.markets.backend.api.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.google.protobuf.Value;
import io.grpc.stub.StreamObserver;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.maze.data.markets.backend.api.mappers.MarketDtoMapper;
import tech.maze.data.markets.backend.api.search.FindOneMarketSearchStrategyHandler;
import tech.maze.data.markets.backend.api.support.CriterionValueExtractor;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.data.markets.backend.domain.models.MarketType;
import tech.maze.data.markets.backend.domain.ports.in.SearchMarketsUseCase;

@ExtendWith(MockitoExtension.class)
class MarketsGrpcControllerTest {
  @Mock
  private FindOneMarketSearchStrategyHandler findOneMarketSearchStrategyHandler;
  @Mock
  private SearchMarketsUseCase searchMarketsUseCase;
  @Mock
  private CriterionValueExtractor criterionValueExtractor;
  @Mock
  private MarketDtoMapper marketDtoMapper;

  @Mock
  private StreamObserver<tech.maze.dtos.markets.requests.FindOneResponse> findOneObserver;
  @Mock
  private StreamObserver<tech.maze.dtos.markets.requests.FindByDataProvidersResponse> findByProvidersObserver;

  @Test
  void findOneSetsMarketWhenCriterionIsPresentAndMatchFound() {
    final var controller = new MarketsGrpcController(
        findOneMarketSearchStrategyHandler,
        searchMarketsUseCase,
        criterionValueExtractor,
        marketDtoMapper
    );
    final UUID id = UUID.randomUUID();
    final var request = tech.maze.dtos.markets.requests.FindOneRequest.newBuilder()
        .setCriterion(
            tech.maze.dtos.markets.search.Criterion.newBuilder()
                .setFilter(
                    tech.maze.dtos.markets.search.CriterionFilter.newBuilder()
                        .setById(Value.newBuilder().setStringValue(id.toString()).build())
                        .build()
                )
                .build()
        )
        .build();
    final var market = new Market(id, MarketType.SPOT, "binance", "BTC", "USDT", null, Instant.now());
    final var dto = tech.maze.dtos.markets.models.Market.newBuilder()
        .setQuoteId(Value.newBuilder().setStringValue("USDT").build())
        .build();

    when(findOneMarketSearchStrategyHandler.handleSearch(request.getCriterion())).thenReturn(Optional.of(market));
    when(marketDtoMapper.toDto(market)).thenReturn(dto);

    controller.findOne(request, findOneObserver);

    final ArgumentCaptor<tech.maze.dtos.markets.requests.FindOneResponse> captor =
        ArgumentCaptor.forClass(tech.maze.dtos.markets.requests.FindOneResponse.class);
    verify(findOneObserver).onNext(captor.capture());
    verify(findOneObserver).onCompleted();
    assertThat(captor.getValue().hasMarket()).isTrue();
    assertThat(captor.getValue().getMarket()).isEqualTo(dto);
  }

  @Test
  void findOneReturnsEmptyResponseWhenCriterionMissing() {
    final var controller = new MarketsGrpcController(
        findOneMarketSearchStrategyHandler,
        searchMarketsUseCase,
        criterionValueExtractor,
        marketDtoMapper
    );

    controller.findOne(tech.maze.dtos.markets.requests.FindOneRequest.newBuilder().build(), findOneObserver);

    final ArgumentCaptor<tech.maze.dtos.markets.requests.FindOneResponse> captor =
        ArgumentCaptor.forClass(tech.maze.dtos.markets.requests.FindOneResponse.class);
    verify(findOneObserver).onNext(captor.capture());
    verify(findOneObserver).onCompleted();
    verifyNoInteractions(findOneMarketSearchStrategyHandler, marketDtoMapper);
    assertThat(captor.getValue().hasMarket()).isFalse();
  }

  @Test
  void findByDataProvidersReturnsMappedMarkets() {
    final var controller = new MarketsGrpcController(
        findOneMarketSearchStrategyHandler,
        searchMarketsUseCase,
        criterionValueExtractor,
        marketDtoMapper
    );
    final var marketA = new Market(UUID.randomUUID(), MarketType.SPOT, "binance", "BTC", "USDT", null, Instant.now());
    final var marketB = new Market(UUID.randomUUID(), MarketType.PERP, "bybit", "ETH", "USDT", null, Instant.now());
    final var dtoA = tech.maze.dtos.markets.models.Market.newBuilder()
        .setBaseId(Value.newBuilder().setStringValue("BTC").build())
        .build();
    final var dtoB = tech.maze.dtos.markets.models.Market.newBuilder()
        .setBaseId(Value.newBuilder().setStringValue("ETH").build())
        .build();

    final UUID dataProviderA = UUID.randomUUID();
    final UUID dataProviderB = UUID.randomUUID();
    final var request = tech.maze.dtos.markets.requests.FindByDataProvidersRequest.newBuilder()
        .addDataProviders(Value.newBuilder().setStringValue(dataProviderA.toString()).build())
        .addDataProviders(Value.newBuilder().setStringValue(dataProviderB.toString()).build())
        .build();
    when(criterionValueExtractor.extractUuids(request.getDataProvidersList())).thenReturn(List.of(dataProviderA, dataProviderB));
    when(searchMarketsUseCase.findByDataProviderIds(List.of(dataProviderA, dataProviderB))).thenReturn(List.of(marketA, marketB));
    when(marketDtoMapper.toDto(marketA)).thenReturn(dtoA);
    when(marketDtoMapper.toDto(marketB)).thenReturn(dtoB);

    controller.findByDataProviders(request, findByProvidersObserver);

    final ArgumentCaptor<tech.maze.dtos.markets.requests.FindByDataProvidersResponse> captor =
        ArgumentCaptor.forClass(tech.maze.dtos.markets.requests.FindByDataProvidersResponse.class);
    verify(findByProvidersObserver).onNext(captor.capture());
    verify(findByProvidersObserver).onCompleted();
    verify(criterionValueExtractor).extractUuids(request.getDataProvidersList());
    verify(searchMarketsUseCase).findByDataProviderIds(List.of(dataProviderA, dataProviderB));
    assertThat(captor.getValue().getMarketsList()).containsExactly(dtoA, dtoB);
  }
}
