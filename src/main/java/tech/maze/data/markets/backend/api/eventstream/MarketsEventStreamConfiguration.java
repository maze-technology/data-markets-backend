package tech.maze.data.markets.backend.api.eventstream;

import com.google.protobuf.InvalidProtocolBufferException;
import io.cloudevents.CloudEvent;
import io.cloudevents.CloudEventData;
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

      parseFetchRequest(event);
      final var response = tech.maze.dtos.markets.payloads.FetchMarketsResponse.newBuilder()
          .setSourceRequest(tech.maze.dtos.markets.payloads.FetchMarketsRequest.newBuilder().build())
          .setKey("")
          .setIsLast(true)
          .setSkipped(0)
          .build();
      sendReply(event, response, tech.maze.dtos.markets.events.EventTypes.FETCH_MARKETS_REQUEST);
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

      parseSyncRequest(event);
      final var response = tech.maze.dtos.markets.payloads.SyncMarketsResponse.newBuilder()
          .setSkipped(0)
          .build();
      sendReply(event, response, tech.maze.dtos.markets.events.EventTypes.SYNC_MARKETS_REQUEST);
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

  private static tech.maze.dtos.markets.payloads.FetchMarketsRequest parseFetchRequest(CloudEvent event) {
    try {
      return tech.maze.dtos.markets.payloads.FetchMarketsRequest.parseFrom(extractBytes(event));
    } catch (InvalidProtocolBufferException ex) {
      throw new IllegalArgumentException("Failed to decode FetchMarketsRequest payload", ex);
    }
  }

  private static tech.maze.dtos.markets.payloads.SyncMarketsRequest parseSyncRequest(CloudEvent event) {
    try {
      return tech.maze.dtos.markets.payloads.SyncMarketsRequest.parseFrom(extractBytes(event));
    } catch (InvalidProtocolBufferException ex) {
      throw new IllegalArgumentException("Failed to decode SyncMarketsRequest payload", ex);
    }
  }

  private static byte[] extractBytes(CloudEvent event) {
    final CloudEventData data = event.getData();
    if (data == null) {
      throw new IllegalArgumentException("CloudEvent has no data");
    }
    return data.toBytes();
  }
}
