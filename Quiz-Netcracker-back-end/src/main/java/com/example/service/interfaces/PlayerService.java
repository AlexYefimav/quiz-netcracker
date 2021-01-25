package com.example.service.interfaces;

import com.example.model.Player;

import java.util.List;
import java.util.UUID;

public interface PlayerService {
    List<Player> findAllPlayers();
//    Player savePlayer(Player player);
//    Player updatePlayer(UUID playerId, User playerRequest);
//    void deletePlayer(UUID playerId);
    Player findPlayer(String property);
    Player findPlayerById(UUID id);

    Player findPlayerByUserId(UUID player);
}
