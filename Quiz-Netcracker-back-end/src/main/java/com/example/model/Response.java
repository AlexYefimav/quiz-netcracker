package com.example.model;

import com.example.dto.GameDto;
import com.example.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Response {
    private List<GameDto> games;
    private int totalPages;
    private int pageNumber;
    private int pageSize;
}
