package tech.maze.data.markets.backend.api.support;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.protobuf.Value;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CriterionValueExtractorTest {
  private final CriterionValueExtractor extractor = new CriterionValueExtractor();

  @Test
  void extractUuidReturnsUuidWhenValueContainsValidString() {
    final UUID id = UUID.randomUUID();
    final Value value = Value.newBuilder().setStringValue(id.toString()).build();

    assertThat(extractor.extractUuid(value)).contains(id);
  }

  @Test
  void extractStringReturnsStringWhenValueContainsNonBlankString() {
    final Value value = Value.newBuilder().setStringValue("binance").build();

    assertThat(extractor.extractString(value)).contains("binance");
  }

  @Test
  void extractUuidsKeepsOnlyValidDistinctValues() {
    final UUID id = UUID.randomUUID();

    final List<UUID> result = extractor.extractUuids(List.of(
        Value.newBuilder().setStringValue(id.toString()).build(),
        Value.newBuilder().setStringValue("not-a-uuid").build(),
        Value.newBuilder().setStringValue(id.toString()).build()
    ));

    assertThat(result).containsExactly(id);
  }
}
