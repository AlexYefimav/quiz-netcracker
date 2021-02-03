package com.example.controller;

import com.example.dto.QuestionDto;
import com.example.exception.ArgumentNotValidException;
import com.example.exception.detail.ErrorInfo;
import com.example.model.Question;
import com.example.service.interfaces.QuestionService;
import com.example.service.mapper.QuestionMapper;
import com.example.service.validation.group.Create;
import com.example.service.validation.group.Update;
import com.example.service.validation.validator.CustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("/question")
public class QuestionController {
    private final QuestionService questionService;
    private final QuestionMapper mapper;
    private final CustomValidator customValidator;
    private final MessageSource messageSource;

    @Autowired
    public QuestionController(QuestionService questionService,
                              QuestionMapper mapper,
                              CustomValidator customValidator,
                              MessageSource messageSource) {
        this.questionService = questionService;
        this.mapper = mapper;
        this.customValidator = customValidator;
        this.messageSource = messageSource;
    }

    @GetMapping()
    public List<QuestionDto> getQuestions() {
        return questionService.findAllQuestion().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{questionId}")
    public QuestionDto getQuestions(@PathVariable UUID questionId) {
        return mapper.toDto(questionService.getQuestionById(questionId));
    }

    @GetMapping("/game/{gameId}")
    public List<QuestionDto> getQuestionsByGameId(@PathVariable UUID gameId) {
        return questionService.getQuestionsByGameId(gameId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/save")
    public QuestionDto createQuestion(@RequestBody QuestionDto questionDto) {
        Map<String, String> propertyViolation = customValidator.validate(questionDto, Create.class);
        if (!propertyViolation.isEmpty()) {
            throw new ArgumentNotValidException(ErrorInfo.ARGUMENT_NOT_VALID, propertyViolation, messageSource);
        }
        Question question = mapper.toEntity(questionDto);
        return mapper.toDto(questionService.saveQuestion(question));
    }

    @PutMapping("/update/{questionId}")
    public QuestionDto updateQuestion(@PathVariable UUID questionId,
                                      @RequestBody QuestionDto questionRequest) {
        Map<String, String> propertyViolation = customValidator.validate(questionRequest, Update.class);
        if (!propertyViolation.isEmpty()) {
            throw new ArgumentNotValidException(ErrorInfo.ARGUMENT_NOT_VALID, propertyViolation, messageSource);
        }
        Question question = mapper.toEntity(questionRequest);
        return mapper.toDto(questionService.updateQuestion(questionId, question));
    }

    @DeleteMapping("/delete/{questionId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable UUID questionId) {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.ok().build();
    }
}

