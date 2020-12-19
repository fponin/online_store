package com.jm.online_store.controller.rest;

import com.jm.online_store.model.ReportComment;
import com.jm.online_store.model.dto.ReportCommentDto;
import com.jm.online_store.service.interf.CommentService;
import com.jm.online_store.service.interf.ReportCommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Рест контроллер для модератора.
 */
@RestController
@RequestMapping("/api/moderator")
@AllArgsConstructor
public class ModeratorRestController {
    private final CommentService commentService;
    private final ReportCommentService reportCommentService;

    /**
     * Получения списка всех жалоб на комментарии с помощью WebSocket.
     *
     * @return ReportCommentDto
     */
    @MessageMapping("/report")
    @SendTo("/table/report")
    public List<ReportCommentDto> allReportComments() {
        return reportCommentService.findAllReportComments().stream()
                .map(ReportCommentDto::entityToDto)
                .collect(Collectors.toList());
    }

    /**
     * Добавление жалобы на комментарий.
     *
     * @param reportCommentDto
     */
    @PostMapping
    public ResponseEntity<ReportCommentDto> addReportComment(@RequestBody ReportCommentDto reportCommentDto) {
        ReportComment reportComment = ReportCommentDto.DtoToEntity(reportCommentDto);
        reportComment.setComment(commentService.findById(reportCommentDto.getCommentId()));
        reportCommentService.addReportComment(reportComment);
        return ResponseEntity.ok(reportCommentDto);
    }

    /**
     * Удаления жалобы.
     *
     * @param id жалобы.
     */
    @DeleteMapping("/leave/{id}")
    public ResponseEntity<ReportComment> deleteReport(@PathVariable("id") Long id) {
        reportCommentService.deleteReport(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Удаление жалобы и коментария.
     *
     * @param id комментария.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ReportComment> deleteReportAndComment(@PathVariable("id") Long id) {
        reportCommentService.deleteReportAndComment(id);
        return ResponseEntity.ok().build();
    }
}
