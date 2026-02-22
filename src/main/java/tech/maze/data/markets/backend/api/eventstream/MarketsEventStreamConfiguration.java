package tech.maze.data.markets.backend.api.eventstream;

import com.google.protobuf.Empty;
import io.cloudevents.CloudEvent;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.maze.commons.eventstream.EventSender;
import tech.maze.commons.eventstream.MazeEventProperties;

/**
 * Event stream configuration for markets processing.
 */
@Configuration
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@EnableConfigurationProperties(MazeEventProperties.class)
@Slf4j
public class MarketsEventStreamConfiguration {
  EventSender eventSender;
  ObjectProvider<MeterRegistry> meterRegistryProvider;

  /**
   * Handles FetchMarketsRequest events delivered via the event stream.
   *
   * @return a consumer for CloudEvents
   */
  @Bean
  public Consumer<CloudEvent> fetchMarketsRequestConsumer() {
    return event -> {
      if (!tech.maze.dtos.markets.events.EventTypes.FETCH_MARKETS_REQUEST.equals(event.getType())) {
        log.warn(
            "Skipping event type {} (expected {})",
            event.getType(),
            tech.maze.dtos.markets.events.EventTypes.FETCH_MARKETS_REQUEST
        );
        return;
      }

      sendReply(
          event,
          Empty.getDefaultInstance(),
          tech.maze.dtos.markets.events.EventTypes.FETCH_MARKETS_REQUEST
      );
    };
  }

  /**
   * Handles SyncMarketsRequest events delivered via the event stream.
   *
   * @return a consumer for CloudEvents
   */
  @Bean
  public Consumer<CloudEvent> syncMarketsRequestConsumer() {
    return event -> {
      if (!tech.maze.dtos.markets.events.EventTypes.SYNC_MARKETS_REQUEST.equals(event.getType())) {
        log.warn(
            "Skipping event type {} (expected {})",
            event.getType(),
            tech.maze.dtos.markets.events.EventTypes.SYNC_MARKETS_REQUEST
        );
        return;
      }

      sendReply(
          event,
          Empty.getDefaultInstance(),
          tech.maze.dtos.markets.events.EventTypes.SYNC_MARKETS_REQUEST
      );
    };
  }

  private void sendReply(CloudEvent event, com.google.protobuf.Message response, String eventType) {
    final String replyTo = eventSender.resolveReplyTo(event);
    if (replyTo == null || replyTo.isBlank()) {
      return;
    }

    final boolean sent = eventSender.send(replyTo, response);
    if (!sent) {
      log.error("Failed to dispatch reply for event {}", event.getId());
      final MeterRegistry registry = meterRegistryProvider.getIfAvailable();
      if (registry != null) {
        registry.counter("maze.events.reply.failed", "eventType", eventType).increment();
      }
    }
  }

}
