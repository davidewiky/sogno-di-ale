package it.ale.repository;

import it.ale.dto.DashboardType;
import it.ale.model.DashboardItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DashboardRepository extends JpaRepository<DashboardItem, UUID> {

    List<DashboardItem> findByDashboardType(DashboardType type);
}
