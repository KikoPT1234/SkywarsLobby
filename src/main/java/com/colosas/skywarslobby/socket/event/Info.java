package com.colosas.skywarslobby.socket.event;

import com.colosas.skywarslobby.SkywarsLobby;
import com.colosas.skywarslobby.objects.SignManager;
import com.colosas.skywarslobby.objects.SkywarsServer;
import org.bukkit.Bukkit;

import java.util.Map;

public class Info extends Event {

    public Info() {
        super("Info");
    }

    @Override
    public String[] run(int serverPort, String[] args) {
        Map<String, SkywarsServer> servers = SkywarsLobby.getInstance().getServers();
        SkywarsServer server = servers.get(args[0]);

        if (server == null) servers.put(args[0], new SkywarsServer(args[0], args[1], Boolean.parseBoolean(args[2]), Integer.parseInt(args[3]), args[4], null, serverPort));
        else {
            server.setPort(serverPort);
            server.setMatchName(args[1]);
            server.setOpen(Boolean.parseBoolean(args[2]));
            server.setPlayers(Integer.parseInt(args[3]));
            server.setMapName(args[4]);
            if (server.getSign() != null) Bukkit.getScheduler().scheduleSyncDelayedTask(SkywarsLobby.getInstance(), server::updateSign);
        }

        return null;
    }
}
