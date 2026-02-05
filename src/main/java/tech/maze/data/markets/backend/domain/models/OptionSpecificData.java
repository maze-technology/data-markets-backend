package tech.maze.data.markets.backend.domain.models;

import java.time.Instant;

/**
 * Generated type.
 */
public record OptionSpecificData(
    double strike,
    Instant expiredAt,
    OptionType type,
    OptionStyle style,
    String underlying
) {}
