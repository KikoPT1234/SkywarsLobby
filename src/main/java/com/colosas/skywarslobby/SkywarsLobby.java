package com.colosas.skywarslobby;

import com.colosas.skywarslobby.commands.AddSign;
import com.colosas.skywarslobby.commands.Reload;
import com.colosas.skywarslobby.objects.SignManager;
import com.colosas.skywarslobby.objects.SkywarsServer;
import com.colosas.skywarslobby.socket.SocketServer;
import com.colosas.skywarslobby.socket.event.Info;
import com.colosas.skywarslobby.socket.event.Join;
import com.colosas.skywarslobby.socket.event.Quit;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class SkywarsLobby extends JavaPlugin {

    private static SkywarsLobby instance;

    private SignManager signManager = new SignManager();
    private String serverName;

    private SocketServer socketServer;

    private final Map<String, SkywarsServer> servers = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        saveDefaultConfig();

        serverName = getConfig().getString("serverName");
        socketServer = new SocketServer(getConfig().getInt("socketPort"));

        socketServer.registerEvent(new Info());
        socketServer.registerEvent(new Join());
        socketServer.registerEvent(new Quit());

        getServer().getPluginCommand("addsign").setExecutor(new AddSign());
        getServer().getPluginCommand("swrl").setExecutor(new Reload());
    }

    @Override
    public void onDisable() {
        socketServer.close();
    }

    public void reload() {

        reloadConfig();
        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        saveDefaultConfig();

        serverName = getConfig().getString("serverName");
        socketServer.reloadStop();
        socketServer = new SocketServer(getConfig().getInt("socketPort"));

        socketServer.registerEvent(new Info());
        socketServer.registerEvent(new Join());
        socketServer.registerEvent(new Quit());

        signManager = new SignManager();

        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);

        if (player == null) return;

        getLogger().info(player.getName());

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetServer");

        player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
    }

    public static SkywarsLobby getInstance() {
        return instance;
    }

    public SignManager getSignManager() {
        return signManager;
    }

    public String getServerName() {
        return serverName;
    }

    public SocketServer getSocketServer() {
        return socketServer;
    }

    public Map<String, SkywarsServer> getServers() {
        return servers;
    }
}
