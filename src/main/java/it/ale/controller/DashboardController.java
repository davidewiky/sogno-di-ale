package it.ale.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.ale.dto.DashboardItemDTO;
import it.ale.dto.DashboardType;
import it.ale.dto.MessageDTO;
import it.ale.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.ale.service.DashboardService;

@RestController
@Tag(name = "Dashboard controller", description = "Dashboard info")
@RequestMapping(path = { "/api/dashboard" })
public class DashboardController {

	//private final AccountService accountService;
	private final DashboardService dashboardService;
	private final ModelMapper mapper;

	public DashboardController(//AccountService accountService,
							   DashboardService dashboardService,
							   ModelMapper mapper) {
		//this.accountService = accountService;
		this.dashboardService = dashboardService;
		this.mapper = mapper;
	}

	/*@GetMapping(value = "/user-rights")
	public ResponseEntity<List<UserRight>> getUserRights() {
		return new ResponseEntity<>(accountService.getEocupUserRights(), HttpStatus.OK);
	}*/

	@GetMapping("{id}")
	public ResponseEntity<DashboardItemDTO> getItemById(@PathVariable String id) throws ResourceNotFoundException, JsonProcessingException {
		return new ResponseEntity<>(dashboardService.getById(id), HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<DashboardItemDTO> save(@RequestBody DashboardItemDTO dashboardItemDTO) throws JsonProcessingException {
		return new ResponseEntity<>(dashboardService.save(dashboardItemDTO), HttpStatus.OK);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<DashboardItemDTO> updateItem(@PathVariable String id, @RequestBody DashboardItemDTO dashboardItemDTO) throws ResourceNotFoundException, JsonProcessingException {
		return new ResponseEntity<>(dashboardService.update(id, dashboardItemDTO), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<DashboardItemDTO>> getAll() {
		final List<DashboardItemDTO> result = new ArrayList<>();
		Stream.of(DashboardType.values()).forEach(dashboardType -> result.addAll(dashboardService.retrieveValidItemByType(dashboardType)));
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping(value = "/type/{type}")
	public ResponseEntity<List<DashboardItemDTO>> getItem(@PathVariable String type) {
		final List<DashboardItemDTO> result = new ArrayList<>(dashboardService.retrieveValidItemByType(DashboardType.valueOf(type)));
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<MessageDTO> deleteItem(@PathVariable String id) throws ResourceNotFoundException, JsonProcessingException {
		DashboardItemDTO dashboardItemDTO = dashboardService.deleteItem(id);
		return new ResponseEntity<>(new MessageDTO(String.format("Item with id:%s successfully deleted!", id)), HttpStatus.OK);
	}
}