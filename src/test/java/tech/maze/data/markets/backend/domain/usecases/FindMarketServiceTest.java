package tech.maze.data.markets.backend.domain.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.data.markets.backend.domain.models.MarketType;
import tech.maze.data.markets.backend.domain.ports.out.LoadMarketPort;

@ExtendWith(MockitoExtension.class)
class FindMarketServiceTest {
  @Mock
  private LoadMarketPort loadMarketPort;
  @Mock
  private Market market;

  @Test
  void delegatesFindById() {
    final UUID id = UUID.randomUUID();
    when(loadMarketPort.findById(id)).thenReturn(Optional.of(market));

    final var service = new FindMarketService(loadMarketPort);
    final var result = service.findById(id);

    assertThat(result).contains(market);
    verify(loadMarketPort).findById(id);
  }

  @Test
  void delegatesFindByTypeAndExchangeAndBaseAndQuote() {
    when(loadMarketPort.findByTypeAndExchangeAndBaseAndQuote(MarketType.SPOT, "binance", "BTC", "USDT"))
        .thenReturn(Optional.of(market));

    final var service = new FindMarketService(loadMarketPort);
    final var result = service.findByTypeAndExchangeAndBaseAndQuote(
        MarketType.SPOT,
        "binance",
        "BTC",
        "USDT"
    );

    assertThat(result).contains(market);
    verify(loadMarketPort).findByTypeAndExchangeAndBaseAndQuote(MarketType.SPOT, "binance", "BTC", "USDT");
  }
}
