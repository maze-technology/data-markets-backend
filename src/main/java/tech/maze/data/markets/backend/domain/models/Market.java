package tech.maze.data.markets.backend.domain.models;

import java.time.Instant;
import java.util.UUID;

/**
 * Generated type.
 */
public record Market(
    UUID id,
    MarketType type,
    String exchange,
    String base,
    String quote,
    OptionSpecificData optionSpecificData,
    Instant createdAt
) {}
