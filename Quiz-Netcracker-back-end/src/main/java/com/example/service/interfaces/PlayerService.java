package com.example.service.interfaces;

import com.example.model.Player;
import com.example.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayerService {
    List<Player> findAllPlayers();
    Player findPlayer(String property);
    Player findPlayerById(UUID id);

}
