package com.sam.library.student.controller;

import com.sam.library.student.common.ApiResponse;
import com.sam.library.student.common.PageResponse;
import com.sam.library.student.dto.chat.*;
import com.sam.library.student.entity.chat.*;
import com.sam.library.student.mapper.ConversationMapper;
import com.sam.library.student.service.ConversationService;
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
@RequestMapping("api/chat/conversations")
@Tag(name = "Chat - Conversations", description = "Conversation management APIs")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;
    private final ConversationMapper conversationMapper;

    @GetMapping
    public ResponseEntity<PageResponse<ConversationDTO>> getMyConversations(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
        Page<ConversationDTO> result = conversationService.getMyConversations(pageable)
                .map(conversationMapper::toDTO);
        return ResponseEntity.ok(PageResponse.of(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ConversationDTO>> getConversation(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                conversationMapper.toDTO(conversationService.getConversationById(id))));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ConversationDTO>> createConversation(
            @Valid @RequestBody CreateConversationDTO dto) {
        Conversation conv = conversationService.createConversation(dto);
        return ResponseEntity.status(201).body(ApiResponse.success("Conversation created",
                conversationMapper.toDTO(conv)));
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<ApiResponse<List<ConversationMember>>> getMembers(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(conversationService.getMembers(id)));
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<ApiResponse<String>> addMembers(
            @PathVariable Long id,
            @Valid @RequestBody AddMembersDTO dto) {
        conversationService.addMembers(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Members added", null));
    }

    @DeleteMapping("/{id}/members/{userId}")
    public ResponseEntity<ApiResponse<String>> removeMember(
            @PathVariable Long id,
            @PathVariable Long userId) {
        conversationService.removeMember(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Member removed", null));
    }

    @PostMapping("/join/{token}")
    public ResponseEntity<ApiResponse<ConversationDTO>> joinByToken(@PathVariable String token) {
        return ResponseEntity.ok(ApiResponse.success(
                conversationMapper.toDTO(conversationService.joinByInviteToken(token))));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<ApiResponse<UserConversationDTO>> markAsRead(
            @PathVariable Long id,
            @RequestParam Long messageId) {
        return ResponseEntity.ok(ApiResponse.success(
                conversationMapper.toUserConversationDTO(conversationService.markAsRead(id, messageId))));
    }

    @PatchMapping("/{id}/mute")
    public ResponseEntity<ApiResponse<UserConversationDTO>> toggleMute(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                conversationMapper.toUserConversationDTO(conversationService.toggleMute(id))));
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<ApiResponse<UserConversationDTO>> toggleArchive(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                conversationMapper.toUserConversationDTO(conversationService.toggleArchive(id))));
    }

    @PostMapping("/{id}/star/{messageId}")
    public ResponseEntity<ApiResponse<UserConversationDTO>> starMessage(
            @PathVariable Long id, @PathVariable Long messageId) {
        return ResponseEntity.ok(ApiResponse.success(
                conversationMapper.toUserConversationDTO(conversationService.starMessage(id, messageId))));
    }

    @DeleteMapping("/{id}/star/{messageId}")
    public ResponseEntity<ApiResponse<UserConversationDTO>> unstarMessage(
            @PathVariable Long id, @PathVariable Long messageId) {
        return ResponseEntity.ok(ApiResponse.success(
                conversationMapper.toUserConversationDTO(conversationService.unstarMessage(id, messageId))));
    }

    // --- Saved Replies ---

    @GetMapping("/saved-replies")
    public ResponseEntity<ApiResponse<List<SavedReplyDTO>>> getSavedReplies() {
        return ResponseEntity.ok(ApiResponse.success(
                conversationService.getSavedReplies().stream()
                        .map(conversationMapper::toSavedReplyDTO).toList()));
    }

    @PostMapping("/saved-replies")
    public ResponseEntity<ApiResponse<SavedReplyDTO>> createSavedReply(
            @Valid @RequestBody CreateSavedReplyDTO dto) {
        return ResponseEntity.status(201).body(ApiResponse.success("Saved reply created",
                conversationMapper.toSavedReplyDTO(conversationService.createSavedReply(dto))));
    }

    @DeleteMapping("/saved-replies/{id}")
    public ResponseEntity<ApiResponse<String>> deleteSavedReply(@PathVariable Long id) {
        conversationService.deleteSavedReply(id);
        return ResponseEntity.ok(ApiResponse.success("Saved reply deleted", null));
    }

    // --- Reminders ---

    @GetMapping("/reminders")
    public ResponseEntity<ApiResponse<List<MessageReminderDTO>>> getReminders() {
        return ResponseEntity.ok(ApiResponse.success(
                conversationService.getReminders().stream()
                        .map(conversationMapper::toReminderDTO).toList()));
    }

    @PostMapping("/reminders")
    public ResponseEntity<ApiResponse<MessageReminderDTO>> createReminder(
            @Valid @RequestBody CreateMessageReminderDTO dto) {
        return ResponseEntity.status(201).body(ApiResponse.success("Reminder created",
                conversationMapper.toReminderDTO(conversationService.createReminder(dto))));
    }

    @DeleteMapping("/reminders/{id}")
    public ResponseEntity<ApiResponse<String>> cancelReminder(@PathVariable Long id) {
        conversationService.cancelReminder(id);
        return ResponseEntity.ok(ApiResponse.success("Reminder cancelled", null));
    }

    // --- Scheduled Messages ---

    @GetMapping("/{id}/scheduled")
    public ResponseEntity<ApiResponse<List<ScheduledMessageDTO>>> getScheduledMessages(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                conversationService.getScheduledMessages(id).stream()
                        .map(conversationMapper::toScheduledMessageDTO).toList()));
    }

    @PostMapping("/scheduled")
    public ResponseEntity<ApiResponse<ScheduledMessageDTO>> createScheduledMessage(
            @Valid @RequestBody CreateScheduledMessageDTO dto) {
        return ResponseEntity.status(201).body(ApiResponse.success("Scheduled message created",
                conversationMapper.toScheduledMessageDTO(conversationService.createScheduledMessage(dto))));
    }

    @DeleteMapping("/scheduled/{id}")
    public ResponseEntity<ApiResponse<String>> cancelScheduledMessage(@PathVariable Long id) {
        conversationService.cancelScheduledMessage(id);
        return ResponseEntity.ok(ApiResponse.success("Scheduled message cancelled", null));
    }
}
