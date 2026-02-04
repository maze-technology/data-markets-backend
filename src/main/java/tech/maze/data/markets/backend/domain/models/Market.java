package tech.maze.data.markets.backend.domain.models;

import java.time.Instant;
import java.util.UUID;

public record Market(
    UUID id,
    MarketType type,
    String exchange,
    String base,
    String quote,
    OptionSpecificData optionSpecificData,
    Instant createdAt
) {}
