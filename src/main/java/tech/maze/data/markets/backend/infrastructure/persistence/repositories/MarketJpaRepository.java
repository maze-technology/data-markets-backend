package tech.maze.data.markets.backend.infrastructure.persistence.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.maze.data.markets.backend.infrastructure.persistence.entities.MarketEntity;

@Repository
public interface MarketJpaRepository extends JpaRepository<MarketEntity, UUID> {}
