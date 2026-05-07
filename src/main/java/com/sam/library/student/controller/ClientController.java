package com.sam.library.student.controller;

import com.sam.library.student.common.ApiResponse;
import com.sam.library.student.common.PageResponse;
import com.sam.library.student.dto.ClientDTO;
import com.sam.library.student.dto.CreateClientDTO;
import com.sam.library.student.entity.Client;
import com.sam.library.student.mapper.ClientMapper;
import com.sam.library.student.service.ClientService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/client")
@Tag(name = "Client", description = "Client services APIs")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final ClientMapper clientMapper;

    @PreAuthorize("hasAuthority('CLIENT_READ')")
    @GetMapping
    public ResponseEntity<PageResponse<ClientDTO>> getAllClients(
            @Parameter(description = "Page number, 1-based", example = "1")
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Filter by name (partial match)")
            @RequestParam(required = false) String name) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ClientDTO> result = clientService.getAllClients(name, pageable).map(clientMapper::toClientDTO);
        return ResponseEntity.ok(PageResponse.of(result));
    }

    @PreAuthorize("hasAuthority('CLIENT_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientDTO>> getClientById(@PathVariable Long id) {
        Client client = clientService.getClientById(id);
        return ResponseEntity.ok(ApiResponse.success(clientMapper.toClientDTO(client)));
    }

    @PreAuthorize("hasAuthority('CLIENT_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<ClientDTO>> createClient(@RequestBody CreateClientDTO dto) {
        Client created = clientService.createClient(clientMapper.toClient(dto));
        return ResponseEntity.status(201).body(ApiResponse.success("Client created", clientMapper.toClientDTO(created)));
    }

    @PreAuthorize("hasAuthority('CLIENT_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientDTO>> updateClient(@PathVariable Long id, @RequestBody ClientDTO dto) {
        Client client = clientService.updateClient(id, clientMapper.toClient(dto));
        return ResponseEntity.ok(ApiResponse.success("Client updated", clientMapper.toClientDTO(client)));
    }

    @PreAuthorize("hasAuthority('CLIENT_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteClient(@PathVariable Long id) {
        String result = clientService.deleteClient(id);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
