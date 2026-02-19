package tech.maze.data.markets.backend.api.mappers;

import com.google.protobuf.Message;
import com.google.protobuf.Timestamp;
import com.google.protobuf.Value;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;
import tech.maze.data.markets.backend.domain.models.Market;
import tech.maze.data.markets.backend.domain.models.OptionSpecificData;

/**
 * Maps between market domain and DTO models.
 */
@Service
public class MarketDtoMapper {
  /**
   * Maps a domain market to its DTO representation.
   */
  public tech.maze.dtos.markets.models.Market toDto(Market market) {
    if (market == null) {
      return tech.maze.dtos.markets.models.Market.getDefaultInstance();
    }

    final tech.maze.dtos.markets.models.Market.Builder builder =
        tech.maze.dtos.markets.models.Market.newBuilder();

    setIfPresent(builder, "id", toDocument(market.id()));
    if (market.type() != null) {
      setEnumIfPresent(builder, "type", market.type().name());
    }
    setIfPresent(builder, "exchangeId", toDocumentString(market.exchange()));
    setIfPresent(builder, "baseId", toDocumentString(market.base()));
    setIfPresent(builder, "quoteId", toDocumentString(market.quote()));
    if (market.createdAt() != null) {
      setIfPresent(builder, "createdAt", toTimestamp(market.createdAt()));
    }

    return builder.build();
  }

  /**
   * Extracts a domain id from a FindOne request if present.
   */
  public UUID toId(tech.maze.dtos.markets.requests.FindOneRequest request) {
    if (request == null) {
      return null;
    }

    final Object criterion = getField(request, "criterion");
    if (!(criterion instanceof Message criterionMessage)) {
      return null;
    }

    final Object filter = getField(criterionMessage, "filter");
    if (!(filter instanceof Message filterMessage)) {
      return null;
    }

    final Object byId = getField(filterMessage, "byId");
    if (byId instanceof Value value && value.hasStringValue()) {
      try {
        return UUID.fromString(value.getStringValue());
      } catch (IllegalArgumentException ex) {
        return null;
      }
    }

    return null;
  }

  /**
   * Included for future request-to-domain expansion.
   */
  public OptionSpecificData toOptionSpecificData(tech.maze.dtos.markets.models.Market market) {
    return null;
  }

  private static Value toDocument(UUID uuid) {
    if (uuid == null) {
      return Value.getDefaultInstance();
    }
    return Value.newBuilder().setStringValue(uuid.toString()).build();
  }

  private static Value toDocumentString(String value) {
    if (value == null) {
      return Value.getDefaultInstance();
    }
    return Value.newBuilder().setStringValue(value).build();
  }

  private static Timestamp toTimestamp(Instant instant) {
    return Timestamp.newBuilder()
        .setSeconds(instant.getEpochSecond())
        .setNanos(instant.getNano())
        .build();
  }

  private static Object getField(Message message, String fieldName) {
    com.google.protobuf.Descriptors.FieldDescriptor field =
        message.getDescriptorForType().findFieldByName(fieldName);
    if (field == null) {
      return null;
    }
    return message.getField(field);
  }

  private static void setIfPresent(Message.Builder builder, String fieldName, Object value) {
    com.google.protobuf.Descriptors.FieldDescriptor field =
        builder.getDescriptorForType().findFieldByName(fieldName);
    if (field != null && value != null) {
      builder.setField(field, value);
    }
  }

  private static void setEnumIfPresent(Message.Builder builder, String fieldName, String enumName) {
    com.google.protobuf.Descriptors.FieldDescriptor field =
        builder.getDescriptorForType().findFieldByName(fieldName);
    if (field == null || enumName == null || !field.getJavaType().equals(
        com.google.protobuf.Descriptors.FieldDescriptor.JavaType.ENUM)) {
      return;
    }

    com.google.protobuf.Descriptors.EnumValueDescriptor value =
        field.getEnumType().findValueByName(enumName);
    if (value != null) {
      builder.setField(field, value);
    }
  }
}
