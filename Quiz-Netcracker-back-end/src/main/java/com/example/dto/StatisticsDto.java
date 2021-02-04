package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class StatisticsDto {
    UUID id;
    UUID question;
    UUID answer;
    UUID player;
}
