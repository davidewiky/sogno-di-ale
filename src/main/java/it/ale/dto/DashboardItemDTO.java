package it.ale.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardItemDTO {

    private String id;
    private String title;
    private String content;
    private DashboardType dashboardType;
    private int orderView;
    private Instant creationDate;
    private String createdBy;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private Instant lastUpdate;
    private String updatedBy;
    private String attachmentId;
    private String attachment;
}
