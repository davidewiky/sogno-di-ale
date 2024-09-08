package it.ale.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.ale.common.CommonUtils;
import it.ale.config.ConfigParams;
import it.ale.dto.DashboardItemDTO;
import it.ale.exception.ResourceNotFoundException;
import it.ale.model.DashboardItem;
import it.ale.repository.DashboardRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import it.ale.dto.DashboardType;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Service
public class DashboardService {

	private final ConfigParams configParams;
	private final DashboardRepository dashboardRepository;
	private final ModelMapper mapper;

	private static final String DASHBOARD_ITEM_EXCEPTION_INFO = "DashboardItem";

	public DashboardService(DashboardRepository dashboardRepository, ModelMapper mapper, ConfigParams configParams) {
		this.dashboardRepository = dashboardRepository;
		this.mapper = mapper;
		this.configParams = configParams;
	}

	public List<DashboardItem> getDashboardByType(DashboardType dashboardType) {
		return null;
	}

	public DashboardItemDTO getById(String id) throws JsonProcessingException, ResourceNotFoundException {
		Optional<DashboardItem> dashboardItem = dashboardRepository.findById(UUID.fromString(id));
		if(dashboardItem.isPresent()) {
			return mapper.map(dashboardItem.get(), DashboardItemDTO.class);
		}
		throw new ResourceNotFoundException(DASHBOARD_ITEM_EXCEPTION_INFO, "id", id);
	}

	public DashboardItemDTO save(DashboardItemDTO dto) {
		log.info("Saving item type:{}", dto.getDashboardType());
		DashboardItem dashboardItemToSave = mapper.map(dto, DashboardItem.class);
		//dashboardItemToSave.setCreatedBy(sessionInfo.getUserSession());
		dashboardItemToSave.setCreationDate(CommonUtils.getLocalInstant());
		DashboardItem saved = dashboardRepository.save(dashboardItemToSave);
		return mapper.map(saved, DashboardItemDTO.class);
	}

	public DashboardItemDTO update(String id, DashboardItemDTO dto) throws ResourceNotFoundException, JsonProcessingException {
		log.info("Update item id: {}, type: {}", id, dto.getDashboardType());
		Optional<DashboardItem> item = dashboardRepository.findById(UUID.fromString(id));
		if (item.isEmpty()) {
			throw new ResourceNotFoundException(
					DASHBOARD_ITEM_EXCEPTION_INFO,
					"id",
					id);
		}
		DashboardItemDTO dashboardItemDTO = mapper.map(item, DashboardItemDTO.class);
		DashboardItem itemToUpdate = mapper.map(dto, DashboardItem.class);
		itemToUpdate.setLastUpdateDate(CommonUtils.getLocalInstant());
		//itemToUpdate.setUpdatedBy(sessionInfo.getUserSession());
		itemToUpdate.setId(UUID.fromString(id));
		DashboardItem saved = dashboardRepository.save(itemToUpdate);
		return mapper.map(saved, DashboardItemDTO.class);
	}

	public List<DashboardItemDTO> retrieveValidItemByType(DashboardType dashboardType) {
		log.info("Retrieve item with type: {}", dashboardType);
		List<DashboardItem> dashboardItems = dashboardRepository.findByDashboardType(dashboardType);
		dashboardItems.sort(Comparator.comparing(DashboardItem::getCreationDate).reversed());

		List<DashboardItemDTO> result = new ArrayList<>();
		dashboardItems.forEach(item -> {
			File file = new File(configParams.getFileStorage() + item.getAttachmentId());
			DashboardItemDTO dashboardItemDTO = mapper.map(item, DashboardItemDTO.class);
            try {
                dashboardItemDTO.setAttachment(Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath())));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
			result.add(dashboardItemDTO);
        });
		return result;
	}

	public DashboardItemDTO deleteItem(String itemId) throws ResourceNotFoundException {
		Optional<DashboardItem> dashboardItemOpt = dashboardRepository
				.findById(UUID.fromString(itemId));
		if(dashboardItemOpt.isEmpty()){
			throw new ResourceNotFoundException(
					DASHBOARD_ITEM_EXCEPTION_INFO,
					"id",
					itemId);
		}
		DashboardItem dashboardItem = dashboardItemOpt.get();
		dashboardRepository.deleteById(UUID.fromString(itemId));
		return mapper.map(dashboardItem, DashboardItemDTO.class);
	}

	public String saveAttachment(MultipartFile attach) throws IOException {
		Path uploadPath = Paths.get(configParams.getFileStorage());
		String extension = FilenameUtils.getExtension(attach.getOriginalFilename());
		String id = UUID.randomUUID().toString();
		String fileName = String.format("%s.%s", id, extension);
		Path filePath = uploadPath.resolve(fileName);
		attach.transferTo(filePath.toFile());
		return fileName;
	}
}
