package it.ale.repository;

import it.ale.dto.DashboardType;
import it.ale.model.DashboardItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface DashboardRepository extends JpaRepository<DashboardItem, UUID> {

    List<DashboardItem> findDashboardItemsByDashboardType(DashboardType dashboardType);
    List<DashboardItem> findByDashboardTypeAndValidToGreaterThan(DashboardType type, LocalDateTime dateTime);
    List<DashboardItem> findByDashboardTypeAndValidToLessThan(DashboardType type, LocalDateTime dateTime);
}
