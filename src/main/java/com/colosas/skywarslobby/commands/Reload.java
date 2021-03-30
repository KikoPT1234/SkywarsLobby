package com.colosas.skywarslobby.commands;

import com.colosas.skywarslobby.SkywarsLobby;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Reload implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        SkywarsLobby.getInstance().reload();
        sender.sendMessage(Component.text("Reload complete!", NamedTextColor.GREEN));
        return true;
    }
}
