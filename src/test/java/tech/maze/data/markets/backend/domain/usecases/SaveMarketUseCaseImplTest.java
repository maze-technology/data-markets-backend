package tech.maze.data.markets.backend.domain.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.data.markets.backend.domain.ports.out.SaveMarketPort;

@ExtendWith(MockitoExtension.class)
class SaveMarketUseCaseImplTest {
  @Mock
  private SaveMarketPort saveMarketPort;
  @Mock
  private Market market;

  @Test
  void delegatesSave() {
    when(saveMarketPort.save(market)).thenReturn(market);

    final var service = new SaveMarketUseCaseImpl(saveMarketPort);
    final var result = service.save(market);

    assertThat(result).isSameAs(market);
    verify(saveMarketPort).save(market);
  }
}
