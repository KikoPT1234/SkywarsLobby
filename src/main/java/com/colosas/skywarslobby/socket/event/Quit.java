package com.colosas.skywarslobby.socket.event;

import com.colosas.skywarslobby.SkywarsLobby;
import com.colosas.skywarslobby.objects.SkywarsServer;
import org.bukkit.Bukkit;

import java.util.Map;

public class Quit extends Event {

    public Quit() {
        super("Quit");
    }

    @Override
    public String[] run(int serverPort, String[] args) {
        SkywarsServer server = null;

        Map<String, SkywarsServer> servers = SkywarsLobby.getInstance().getServers();

        for (SkywarsServer s : servers.values()) {
            if (s.getPort() == serverPort) {
                server = s;
                break;
            }
        }

        if (server == null) return null;

        server.setPlayers(server.getPlayers() - 1);

        if (server.getSign() != null) Bukkit.getScheduler().scheduleSyncDelayedTask(SkywarsLobby.getInstance(), server::updateSign);

        return null;
    }
}
