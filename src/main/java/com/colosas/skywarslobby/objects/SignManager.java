package com.colosas.skywarslobby.objects;

import com.colosas.skywarslobby.SkywarsLobby;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.bukkit.block.Sign;

import java.util.Map;

public class SignManager {

    public void addSign(Sign sign) {
        String serverName = PlainComponentSerializer.plain().serialize(sign.line(0));
        Map<String, SkywarsServer> servers = SkywarsLobby.getInstance().getServers();

        SkywarsServer server = servers.get(serverName);

        if (server == null) {
            server = new SkywarsServer(serverName, "", true, 0, "", sign, 0);
        };

        server.setSign(sign);

        server.updateSign();
    }
}
