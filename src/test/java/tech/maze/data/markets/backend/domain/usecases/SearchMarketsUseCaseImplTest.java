package tech.maze.data.markets.backend.domain.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.data.markets.backend.domain.models.MarketsPage;
import tech.maze.data.markets.backend.domain.ports.out.SearchMarketsPort;

@ExtendWith(MockitoExtension.class)
class SearchMarketsUseCaseImplTest {
  @Mock
  private SearchMarketsPort searchMarketsPort;
  @Mock
  private Market market;

  @Test
  void delegatesFindAll() {
    when(searchMarketsPort.findAll()).thenReturn(List.of(market));

    final var service = new SearchMarketsUseCaseImpl(searchMarketsPort);
    final var result = service.findAll();

    assertThat(result).containsExactly(market);
    verify(searchMarketsPort).findAll();
  }

  @Test
  void delegatesFindByDataProviderIds() {
    final UUID dataProviderId = UUID.randomUUID();
    final MarketsPage expected = new MarketsPage(List.of(market), 1, 1);
    when(searchMarketsPort.findByDataProviderIds(List.of(dataProviderId), 0, 50))
        .thenReturn(expected);

    final var service = new SearchMarketsUseCaseImpl(searchMarketsPort);
    final var result = service.findByDataProviderIds(List.of(dataProviderId), 0, 50);

    assertThat(result).isEqualTo(expected);
    verify(searchMarketsPort).findByDataProviderIds(List.of(dataProviderId), 0, 50);
  }
}
