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
import tech.maze.data.markets.backend.api.support.CriterionValueExtractor;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.data.markets.backend.domain.ports.in.FindMarketUseCase;

@ExtendWith(MockitoExtension.class)
class FindOneMarketByIdSearchStrategyTest {
  @Mock
  private FindMarketUseCase findMarketUseCase;
  @Mock
  private Market market;

  @Test
  void supportsOnlyCriterionWithByIdStringValue() {
    final var strategy = new FindOneMarketByIdSearchStrategy(findMarketUseCase, new CriterionValueExtractor());
    final var valid = criterionWithId(UUID.randomUUID().toString());

    assertThat(strategy.supports(valid)).isTrue();
    assertThat(strategy.supports(null)).isFalse();
    assertThat(strategy.supports(tech.maze.dtos.markets.search.Criterion.newBuilder().build())).isFalse();
  }

  @Test
  void searchDelegatesToFindUseCaseForValidUuid() {
    final var strategy = new FindOneMarketByIdSearchStrategy(findMarketUseCase, new CriterionValueExtractor());
    final UUID id = UUID.randomUUID();
    final var criterion = criterionWithId(id.toString());
    when(findMarketUseCase.findById(id)).thenReturn(Optional.of(market));

    final var result = strategy.search(criterion);

    assertThat(result).contains(market);
    verify(findMarketUseCase).findById(id);
  }

  @Test
  void searchReturnsEmptyWhenUuidIsInvalid() {
    final var strategy = new FindOneMarketByIdSearchStrategy(findMarketUseCase, new CriterionValueExtractor());

    final var result = strategy.search(criterionWithId("not-a-uuid"));

    assertThat(result).isEmpty();
    verifyNoInteractions(findMarketUseCase);
  }

  private static tech.maze.dtos.markets.search.Criterion criterionWithId(String id) {
    return tech.maze.dtos.markets.search.Criterion.newBuilder()
        .setFilter(
            tech.maze.dtos.markets.search.CriterionFilter.newBuilder()
                .setById(Value.newBuilder().setStringValue(id).build())
                .build()
        )
        .build();
  }
}
