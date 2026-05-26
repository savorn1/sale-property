package com.sam.library.student.controller;

import com.sam.library.student.common.ApiResponse;
import com.sam.library.student.common.PageResponse;
import com.sam.library.student.dto.CreateOpenItemDTO;
import com.sam.library.student.dto.OpenItemDTO;
import com.sam.library.student.entity.OpenItem;
import com.sam.library.student.enums.OpenItemStatus;
import com.sam.library.student.mapper.OpenItemMapper;
import com.sam.library.student.service.OpenItemService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/open-item")
@Tag(name = "Open Item", description = "Opening stock balance management APIs")
@RequiredArgsConstructor
public class OpenItemController {

    private final OpenItemService openItemService;
    private final OpenItemMapper openItemMapper;

    @PreAuthorize("hasAuthority('OPEN_ITEM_READ')")
    @GetMapping
    public ResponseEntity<PageResponse<OpenItemDTO>> getAll(
            @Parameter(description = "Page number, 1-based", example = "1")
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Search by open item number")
            @RequestParam(required = false) String q,
            @Parameter(description = "Filter by status (multiple allowed)")
            @RequestParam(required = false) List<OpenItemStatus> status) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<OpenItemDTO> result = openItemService.getAll(q, status, pageable)
                .map(openItemMapper::toDTO);
        return ResponseEntity.ok(PageResponse.of(result));
    }

    @PreAuthorize("hasAuthority('OPEN_ITEM_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OpenItemDTO>> getById(@PathVariable Long id) {
        OpenItem openItem = openItemService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(openItemMapper.toDTO(openItem)));
    }

    @PreAuthorize("hasAuthority('OPEN_ITEM_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<OpenItemDTO>> create(@Valid @RequestBody CreateOpenItemDTO dto) {
        OpenItem openItem = openItemService.create(dto);
        return ResponseEntity.status(201)
                .body(ApiResponse.success("Open item created", openItemMapper.toDTO(openItem)));
    }

    @PreAuthorize("hasAuthority('OPEN_ITEM_UPDATE')")
    @PostMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse<OpenItemDTO>> confirm(@PathVariable Long id) {
        OpenItem openItem = openItemService.confirm(id);
        return ResponseEntity.ok(ApiResponse.success("Open item confirmed — stock updated", openItemMapper.toDTO(openItem)));
    }

    @PreAuthorize("hasAuthority('OPEN_ITEM_UPDATE')")
    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<OpenItemDTO>> cancel(@PathVariable Long id) {
        OpenItem openItem = openItemService.cancel(id);
        return ResponseEntity.ok(ApiResponse.success("Open item cancelled", openItemMapper.toDTO(openItem)));
    }

    @PreAuthorize("hasAuthority('OPEN_ITEM_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        openItemService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Open item deleted with id: " + id));
    }
}
