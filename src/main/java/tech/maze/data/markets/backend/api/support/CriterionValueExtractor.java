package tech.maze.data.markets.backend.api.support;

import com.google.protobuf.Value;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * Shared extraction helpers for criterion values carried as protobuf documents.
 */
@Component
public class CriterionValueExtractor {
  /**
   * Extracts a UUID from a protobuf document value.
   */
  public Optional<UUID> extractUuid(Value value) {
    return extractString(value).flatMap(this::parseUuid);
  }

  /**
   * Extracts a string from a protobuf document value.
   */
  public Optional<String> extractString(Value value) {
    if (value == null || !value.hasStringValue()) {
      return Optional.empty();
    }

    String raw = value.getStringValue();
    if (raw == null || raw.isBlank()) {
      return Optional.empty();
    }

    return Optional.of(raw);
  }

  /**
   * Extracts all valid UUIDs from protobuf document values.
   */
  public List<UUID> extractUuids(List<Value> values) {
    if (values == null || values.isEmpty()) {
      return List.of();
    }

    return values.stream()
        .map(this::extractUuid)
        .flatMap(Optional::stream)
        .distinct()
        .toList();
  }

  private Optional<UUID> parseUuid(String raw) {
    try {
      return Optional.of(UUID.fromString(raw));
    } catch (IllegalArgumentException ex) {
      return Optional.empty();
    }
  }
}
