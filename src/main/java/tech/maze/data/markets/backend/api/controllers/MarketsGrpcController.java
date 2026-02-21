package tech.maze.data.markets.backend.api.controllers;

import io.grpc.stub.StreamObserver;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import tech.maze.commons.exceptions.GrpcStatusException;
import tech.maze.commons.pagination.Pagination;
import tech.maze.commons.pagination.PaginationUtils;
import tech.maze.data.markets.backend.api.mappers.MarketDtoMapper;
import tech.maze.data.markets.backend.api.search.FindOneMarketSearchStrategyHandler;
import tech.maze.data.markets.backend.api.support.CriterionValueExtractor;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.data.markets.backend.domain.models.MarketsPage;
import tech.maze.data.markets.backend.domain.ports.in.SearchMarketsUseCase;

/**
 * gRPC controller for markets API operations.
 */
@RequiredArgsConstructor
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MarketsGrpcController
    extends tech.maze.dtos.markets.controllers.MarketsGRPCGrpc.MarketsGRPCImplBase {
  FindOneMarketSearchStrategyHandler findOneMarketSearchStrategyHandler;
  SearchMarketsUseCase searchMarketsUseCase;
  CriterionValueExtractor criterionValueExtractor;
  MarketDtoMapper marketDtoMapper;

  @Override
  public void findOne(
      tech.maze.dtos.markets.requests.FindOneRequest request,
      StreamObserver<tech.maze.dtos.markets.requests.FindOneResponse> responseObserver
  ) {
    if (!request.hasCriterion()) {
      throw GrpcStatusException.invalidArgument("criterion is required");
    }

    tech.maze.dtos.markets.requests.FindOneResponse.Builder responseBuilder =
        tech.maze.dtos.markets.requests.FindOneResponse.newBuilder();
    findOneMarketSearchStrategyHandler.handleSearch(request.getCriterion())
        .map(marketDtoMapper::toDto)
        .ifPresent(responseBuilder::setMarket);

    responseObserver.onNext(responseBuilder.build());
    responseObserver.onCompleted();
  }

  @Override
  public void findByDataProviders(
      tech.maze.dtos.markets.requests.FindByDataProvidersRequest request,
      StreamObserver<tech.maze.dtos.markets.requests.FindByDataProvidersResponse> responseObserver
  ) {
    final List<java.util.UUID> dataProviderIds =
        criterionValueExtractor.extractUuids(request.getDataProvidersList());
    final Pagination pagination = PaginationUtils.normalize(
        request.hasPagination() ? request.getPagination().getPage() : 0L,
        request.hasPagination() ? request.getPagination().getLimit() : 50L,
        50
    );
    final MarketsPage marketsPage = searchMarketsUseCase.findByDataProviderIds(
        dataProviderIds,
        pagination.page(),
        pagination.limit()
    );
    final List<Market> markets = marketsPage.markets();
    tech.maze.dtos.markets.requests.FindByDataProvidersResponse response =
        tech.maze.dtos.markets.requests.FindByDataProvidersResponse.newBuilder()
            .addAllMarkets(markets.stream().map(marketDtoMapper::toDto).toList())
            .setPaginationInfos(
                tech.maze.dtos.commons.search.PaginationInfos.newBuilder()
                    .setTotalElements(marketsPage.totalElements())
                    .setTotalPages(marketsPage.totalPages())
                    .build()
            )
            .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
