package tech.maze.data.markets.backend.api.controllers;

import io.grpc.stub.StreamObserver;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import tech.maze.data.markets.backend.api.mappers.MarketDtoMapper;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.data.markets.backend.domain.ports.in.FindMarketUseCase;
import tech.maze.data.markets.backend.domain.ports.in.SearchMarketsUseCase;

/**
 * gRPC controller for markets API operations.
 */
@RequiredArgsConstructor
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MarketsGrpcController extends tech.maze.dtos.markets.controllers.MarketsGRPCGrpc.MarketsGRPCImplBase {
  FindMarketUseCase findMarketUseCase;
  SearchMarketsUseCase searchMarketsUseCase;
  MarketDtoMapper marketDtoMapper;

  @Override
  public void findOne(
      tech.maze.dtos.markets.requests.FindOneRequest request,
      StreamObserver<tech.maze.dtos.markets.requests.FindOneResponse> responseObserver
  ) {
    final var responseBuilder = tech.maze.dtos.markets.requests.FindOneResponse.newBuilder();
    final var id = marketDtoMapper.toId(request);

    if (id != null) {
      findMarketUseCase.findById(id)
          .map(marketDtoMapper::toDto)
          .ifPresent(responseBuilder::setMarket);
    }

    responseObserver.onNext(responseBuilder.build());
    responseObserver.onCompleted();
  }

  @Override
  public void findByDataProviders(
      tech.maze.dtos.markets.requests.FindByDataProvidersRequest request,
      StreamObserver<tech.maze.dtos.markets.requests.FindByDataProvidersResponse> responseObserver
  ) {
    final List<Market> markets = searchMarketsUseCase.findAll();
    final var response = tech.maze.dtos.markets.requests.FindByDataProvidersResponse.newBuilder()
        .addAllMarkets(markets.stream().map(marketDtoMapper::toDto).toList())
        .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
