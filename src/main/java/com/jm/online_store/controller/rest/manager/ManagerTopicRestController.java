package com.jm.online_store.controller.rest.manager;

import com.jm.online_store.model.Topic;
import com.jm.online_store.service.interf.TopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RestController для чтения/добавления/изменения тем для обратной связи
 */
@RestController
@RequestMapping("/api/manager/topic")
@RequiredArgsConstructor
@Api(description = "Rest controller for read/add/update feedback topics")
public class ManagerTopicRestController {
    private final TopicService topicService;

    /**
     * Метод для получения единственной темы
     *
     * @param id идентификатор темы
     * @return ResponseEntity<Topic> возвращает единственную тему со статусом ответа,
     * если темы с таким id не существует - только статус
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "Get topic by ID")
    @ApiResponse(code = 404, message = "Topic was not found")
    public ResponseEntity<Topic> readTopic(@PathVariable(name = "id") long id) {
        if (!topicService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(topicService.findById(id));
    }

    /**
     * Метод для добавления новой темы
     *
     * @param topic тема, которая будет создана
     * @return ResponseEntity<Topic> возвращает созданную тему со статусом ответа,
     * если тема с таким именем уже существует - только статус
     */
    @PostMapping
    @ApiOperation(value = "Create new topic")
    @ApiResponse(code = 304, message = "Topic was not modified")
    public ResponseEntity<Topic> createTopic(@RequestBody Topic topic){
        if (topicService.existsByTopicName(topic.getTopicName())) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.ok(topicService.create(topic));
    }

    /**
     * Метод для изменения темы
     *
     * @param id идентификатор темы
     * @param topic тема с внесенными изменениями
     * @return ResponseEntity<Topic> возвращает измененную тему со статусом ответа,
     * если тема с таким id не существует - только статус
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "Update topic by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 304, message = "Topic was not modified"),
            @ApiResponse(code = 404, message = "Topic was not found")
    })
    public ResponseEntity<Topic> editTopic(@PathVariable(name = "id") long id, @RequestBody Topic topic) {
        if (topicService.existsByTopicName(topic.getTopicName())) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        if(!topicService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(topicService.update(topic));
    }
}
