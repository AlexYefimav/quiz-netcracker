package com.example.controller;

import com.example.dto.PlayerDto;
import com.example.model.Player;
import com.example.model.RoleList;
import com.example.model.User;
import com.example.security.LoginModel;
import com.example.security.UserRoleList;
import com.example.service.interfaces.PlayerService;
import com.example.service.interfaces.UserService;
import com.example.service.mapper.PlayerMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("/player")
public class PlayerController {

    private final PlayerService playerService;
    private final UserService userService;
    private final PlayerMapper mapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public PlayerController(PlayerService playerService, UserService userService, PlayerMapper mapper) {
        this.playerService = playerService;
        this.userService = userService;
        this.mapper = mapper;
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @GetMapping("/{userProperty}")
    public PlayerDto getPlayer(@PathVariable String userProperty) {
        return mapper.toDto(playerService.findPlayer(userProperty));
    }

    @GetMapping("/id/{id}")
    public PlayerDto getPlayerById(@PathVariable UUID id) {
        return mapper.toDto(playerService.findPlayerByUserId(id));
    }

    @GetMapping()
    public List<PlayerDto> getAllClients() {
        return playerService.findAllPlayers().stream().map(mapper::toDto).collect(Collectors.toList());
    }

}