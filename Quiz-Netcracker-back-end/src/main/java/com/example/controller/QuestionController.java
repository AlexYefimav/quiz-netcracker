package com.example.controller;

import com.example.dto.QuestionDto;
import com.example.exception.ArgumentNotValidException;
import com.example.model.Question;
import com.example.service.interfaces.QuestionService;
import com.example.service.mapper.QuestionMapper;
import com.example.service.validation.group.CreateValidationGroup;
import com.example.service.validation.group.UpdateValidationGroup;
import com.example.service.validation.validator.impl.BaseCustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;
    private final QuestionMapper mapper;
    private final BaseCustomValidator baseCustomValidator;

    @Autowired
    public QuestionController(QuestionService questionService,
                              QuestionMapper mapper,
                              BaseCustomValidator baseCustomValidator) {
        this.questionService = questionService;
        this.mapper = mapper;
        this.baseCustomValidator = baseCustomValidator;
    }

    @GetMapping("/findAllQuestions")
    public List<QuestionDto> getQuestions() {
        return questionService.findAllQuestion().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/findQuestion/{questionId}")
    public QuestionDto getQuestions(@PathVariable UUID questionId) {
        return mapper.toDto(questionService.getQuestionById(questionId));
    }

    @PostMapping("/save")
    public QuestionDto createQuestion(@RequestBody QuestionDto questionDto) {
        String errorMessages = baseCustomValidator.validate(questionDto, CreateValidationGroup.class);
        if (!errorMessages.isEmpty()) {
            throw new ArgumentNotValidException(errorMessages);
        }
        Question question = mapper.toEntity(questionDto);
        return mapper.toDto(questionService.saveQuestion(question));
    }

    @PutMapping("/update/{questionId}")
    public QuestionDto updateQuestion(@PathVariable UUID questionId,
                                      @RequestBody QuestionDto questionRequest) {
        String errorMessages = baseCustomValidator.validate(questionRequest, UpdateValidationGroup.class);
        if (!errorMessages.isEmpty()) {
            throw new ArgumentNotValidException(errorMessages);
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

