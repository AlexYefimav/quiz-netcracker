package com.example.service.impl;

import com.example.exception.DeleteEntityException;
import com.example.exception.ResourceNotFoundException;
import com.example.exception.detail.ErrorInfo;
import com.example.model.GameAccess;
import com.example.model.Player;
import com.example.repository.PlayerRepository;
import com.example.service.interfaces.GameAccessService;
import com.example.service.interfaces.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final MessageSource messageSource;
    private final GameAccessService gameAccessService;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, MessageSource messageSource, GameAccessService gameAccessService) {
        this.playerRepository = playerRepository;
        this.messageSource = messageSource;
        this.gameAccessService = gameAccessService;
    }


    @Override
    public List<Player> findAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public Player findPlayer(String property) {
        if (property == null) {
            throw new ResourceNotFoundException(ErrorInfo.RESOURCE_NOT_FOUND,
                    messageSource.getMessage("message.ResourceNotFound", new Object[]{null}, LocaleContextHolder.getLocale()));
        }
        return playerRepository.findPlayerByEmailAndName(property, property).get();
    }

    @Override
    public Player findPlayerById(UUID id) {
        if (id == null) {
            throw new ResourceNotFoundException(ErrorInfo.RESOURCE_NOT_FOUND,
                    messageSource.getMessage("message.ResourceNotFound", new Object[]{null}, LocaleContextHolder.getLocale()));
        }
        UUID[] args = new UUID[]{ id };
        return playerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(ErrorInfo.RESOURCE_NOT_FOUND,
                messageSource.getMessage("message.ResourceNotFound", args, LocaleContextHolder.getLocale())));
    }

    @Override
    public Player findPlayerByUserId(UUID user) {
        if (user == null) {
            throw new ResourceNotFoundException(ErrorInfo.RESOURCE_NOT_FOUND,
                    messageSource.getMessage("message.ResourceNotFound", new Object[]{null}, LocaleContextHolder.getLocale()));
        }
        return playerRepository.getPlayerByUserId(user);
    }

    @Override
    public Player findGuest(String name) {
        if (name == null) {
            throw new ResourceNotFoundException(ErrorInfo.RESOURCE_NOT_FOUND,
                    messageSource.getMessage("message.ResourceNotFound", new Object[]{null}, LocaleContextHolder.getLocale()));
        }
        Player player = playerRepository.findPlayerByName(name);
        if (player == null) {
            throw new ResourceNotFoundException(ErrorInfo.RESOURCE_NOT_FOUND,
                    messageSource.getMessage("message.ResourceNotFound", new Object[]{null}, LocaleContextHolder.getLocale()));
        }
        return player;
    }

    @Override
    public void deletePlayer(UUID id) {
        try {
            List<GameAccess> gameAccesses=gameAccessService.getGameAccessesByPlayerId(id);
            if(gameAccesses==null){
                playerRepository.deleteById(id);
            }
            else {
                gameAccessService.getGameAccessesByGameId(id)
                        .stream()
                        .peek(gameAccess -> {
                            gameAccessService.delete(gameAccess.getId());
                        })
                        .collect(Collectors.toList());
                playerRepository.deleteById(id);
            }
        }
        catch (RuntimeException exception) {
            UUID[] args = new UUID[]{ id };
            throw new DeleteEntityException(ErrorInfo.DELETE_ENTITY_ERROR,
                    messageSource.getMessage("message.DeleteEntityError", args, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public Player updatePlayer(UUID playerId, Player playerRequest) {
        UUID[] args = new UUID[]{ playerId };
        return playerRepository.findById(playerId).map(player -> {
            player.setName(playerRequest.getName());
            player.setEmail(playerRequest.getEmail());
            player.setPhoto(playerRequest.getPhoto());
            return playerRepository.save(player);
        }).orElseThrow(()-> new ResourceNotFoundException(ErrorInfo.RESOURCE_NOT_FOUND,
                messageSource.getMessage("message.ResourceNotFound", args, LocaleContextHolder.getLocale())));
    }
}
