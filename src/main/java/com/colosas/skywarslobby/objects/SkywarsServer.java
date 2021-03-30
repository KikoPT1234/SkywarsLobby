package com.colosas.skywarslobby.objects;

import com.colosas.skywarslobby.SkywarsLobby;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.block.Sign;

public class SkywarsServer {

    private String name;
    private String matchName;
    private boolean open;
    private int players;
    private String mapName;
    private Sign sign;
    private int port;

    public SkywarsServer(String name, String matchName, boolean open, int players, String mapName, Sign sign, int port) {
        this.name = name;
        this.matchName = matchName;
        this.open = open;
        this.players = players;
        this.mapName = mapName;
        this.sign = sign;
        this.port = port;
    }

    public void updateSign() {
        sign.setEditable(true);
        sign.line(0, Component.text(matchName, NamedTextColor.RED, TextDecoration.BOLD));
        sign.line(1, open ? Component.text("[OPEN]", NamedTextColor.DARK_GREEN, TextDecoration.BOLD) : Component.text("[ONGOING]", NamedTextColor.DARK_RED, TextDecoration.BOLD));
        sign.line(2, Component.text("Players: ").append(Component.text(Integer.toString(players), NamedTextColor.RED)));
        sign.line(3, Component.text(mapName, NamedTextColor.RED));
        SkywarsLobby.getInstance().getLogger().info(Boolean.toString(sign.update(true)));
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Sign getSign() {
        return sign;
    }

    public void setSign(Sign sign) {
        this.sign = sign;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

}
