package com.github.syldium.fkboard.websocket.responses;

import com.github.syldium.fkboard.status.PlayerStatus;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.devsylone.fkpi.api.ITeam;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class TeamsList implements Response
{
    private final JsonArray teams;

    public TeamsList(@NotNull List<? extends ITeam> teams, @NotNull PlayerStatus playerStatus)
    {
        this.teams = new Gson().toJsonTree(teams.stream().map(team -> {
            JsonObject object = new JsonObject();
            object.addProperty("name", team.getName());
            object.addProperty("chatcolor", team.getChatColor().toString());
            JsonArray players = new JsonArray();
            for (String playerName : team.getPlayers()) {
                JsonObject player = new JsonObject();
                player.addProperty("name", playerName);
                player.addProperty("online", playerStatus.isPlayerOnline(playerName));
                players.add(player);
            }
            object.add("players", players);
            return object;
        }).collect(Collectors.toList())).getAsJsonArray();
    }

    @Override
    public int getStatusCode()
    {
        return 1000;
    }

    public @NotNull String toJSON()
    {
        JsonObject object = new JsonObject();
        object.addProperty("code", getStatusCode());
        object.add("teams", teams);
        return object.toString();
    }
}