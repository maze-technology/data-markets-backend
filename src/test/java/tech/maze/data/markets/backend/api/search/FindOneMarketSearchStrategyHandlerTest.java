package tech.maze.data.markets.backend.api.search;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.maze.data.markets.backend.domain.models.Market;

@ExtendWith(MockitoExtension.class)
class FindOneMarketSearchStrategyHandlerTest {
  @Mock
  private FindOneMarketSearchStrategy first;
  @Mock
  private FindOneMarketSearchStrategy second;
  @Mock
  private Market market;

  @Test
  void usesFirstSupportingStrategy() {
    final var criterion = tech.maze.dtos.markets.search.Criterion.newBuilder().build();
    final var handler = new FindOneMarketSearchStrategyHandler(List.of(first, second));
    when(first.supports(criterion)).thenReturn(false);
    when(second.supports(criterion)).thenReturn(true);
    when(second.search(criterion)).thenReturn(Optional.of(market));

    final var result = handler.handleSearch(criterion);

    assertThat(result).contains(market);
    verify(first).supports(criterion);
    verify(second).supports(criterion);
    verify(second).search(criterion);
  }

  @Test
  void returnsEmptyWhenNoStrategySupportsCriterion() {
    final var criterion = tech.maze.dtos.markets.search.Criterion.newBuilder().build();
    final var handler = new FindOneMarketSearchStrategyHandler(List.of(first, second));
    when(first.supports(criterion)).thenReturn(false);
    when(second.supports(criterion)).thenReturn(false);

    final var result = handler.handleSearch(criterion);

    assertThat(result).isEmpty();
    verify(first).supports(criterion);
    verify(second).supports(criterion);
    verify(first, never()).search(criterion);
    verify(second, never()).search(criterion);
  }
}
