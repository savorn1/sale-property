package com.sam.library.student.controller;

import com.sam.library.student.common.ApiResponse;
import com.sam.library.student.common.PageResponse;
import com.sam.library.student.dto.chat.AddReactionDTO;
import com.sam.library.student.dto.chat.EditMessageDTO;
import com.sam.library.student.dto.chat.MessageDTO;
import com.sam.library.student.dto.chat.MessageReactionDTO;
import com.sam.library.student.dto.chat.SendMessageDTO;
import com.sam.library.student.mapper.ConversationMapper;
import com.sam.library.student.mapper.MessageMapper;
import com.sam.library.student.service.MessageService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/chat/messages")
@Tag(name = "Chat - Messages", description = "Message management APIs")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final MessageMapper messageMapper;
    private final ConversationMapper conversationMapper;

    @GetMapping
    public ResponseEntity<PageResponse<MessageDTO>> getMessages(
            @Parameter(description = "Conversation ID") @RequestParam Long conversationId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<MessageDTO> result = messageService.getMessages(conversationId, pageable)
                .map(messageMapper::toDTO);
        return ResponseEntity.ok(PageResponse.of(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MessageDTO>> getMessage(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(messageMapper.toDTO(messageService.getMessageById(id))));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MessageDTO>> sendMessage(@Valid @RequestBody SendMessageDTO dto) {
        return ResponseEntity.status(201).body(ApiResponse.success("Message sent",
                messageMapper.toDTO(messageService.sendMessage(dto))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MessageDTO>> editMessage(
            @PathVariable Long id,
            @Valid @RequestBody EditMessageDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Message updated",
                messageMapper.toDTO(messageService.editMessage(id, dto))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.ok(ApiResponse.success("Message deleted", null));
    }

    @GetMapping("/{id}/reactions")
    public ResponseEntity<ApiResponse<List<MessageReactionDTO>>> getReactions(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                messageService.getReactions(id).stream()
                        .map(conversationMapper::toReactionDTO).toList()));
    }

    @PostMapping("/{id}/reactions")
    public ResponseEntity<ApiResponse<MessageReactionDTO>> addReaction(
            @PathVariable Long id,
            @Valid @RequestBody AddReactionDTO dto) {
        return ResponseEntity.status(201).body(ApiResponse.success("Reaction added",
                conversationMapper.toReactionDTO(messageService.addReaction(id, dto))));
    }

    @DeleteMapping("/{id}/reactions/{emoji}")
    public ResponseEntity<ApiResponse<String>> removeReaction(
            @PathVariable Long id,
            @PathVariable String emoji) {
        messageService.removeReaction(id, emoji);
        return ResponseEntity.ok(ApiResponse.success("Reaction removed", null));
    }

    @PatchMapping("/{id}/delivered")
    public ResponseEntity<ApiResponse<String>> markDelivered(@PathVariable Long id) {
        messageService.markDelivered(id);
        return ResponseEntity.ok(ApiResponse.success("Marked as delivered", null));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<ApiResponse<String>> markRead(@PathVariable Long id) {
        messageService.markRead(id);
        return ResponseEntity.ok(ApiResponse.success("Marked as read", null));
    }
}
