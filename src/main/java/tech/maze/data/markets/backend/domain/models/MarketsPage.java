package tech.maze.data.markets.backend.domain.models;

import java.util.List;

/**
 * Paged markets query result.
 */
public record MarketsPage(
    List<Market> markets,
    long totalElements,
    long totalPages
) {}
