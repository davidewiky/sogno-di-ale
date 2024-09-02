package it.ale.model;

import it.ale.dto.DashboardType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("deleted_date IS NULL")
@Table(name = "dashboard_item")
public class DashboardItem {

    @Id
    @UuidGenerator
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    private String content;
    @Enumerated(EnumType.STRING)
    private DashboardType dashboardType;
    private int orderView;
    private Instant creationDate;
    private String createdBy;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private Instant lastUpdateDate;
    private String updatedBy;
    private Instant deletedDate;
}
