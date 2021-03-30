package com.colosas.skywarslobby.commands;

import com.colosas.skywarslobby.SkywarsLobby;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AddSign implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("Only a player can execute this command!", NamedTextColor.RED));
            return false;
        }

        Player player = (Player) sender;

        Block targetedBlock = player.getTargetBlock(5);

        if (targetedBlock == null || !(targetedBlock.getState() instanceof Sign)) {
            player.sendMessage(Component.text("Sign block not found in front of you.", NamedTextColor.RED));
            return false;
        }

        Sign sign = (Sign) targetedBlock.getState();

        List<Component> lines = sign.lines();
        for (Component line : lines) SkywarsLobby.getInstance().getLogger().info(PlainComponentSerializer.plain().serialize(line));
        if (PlainComponentSerializer.plain().serialize(lines.get(0)).matches("^ *$")) {
            player.sendMessage(Component.text("Invalid sign.", NamedTextColor.RED));
            return false;
        }

        SkywarsLobby.getInstance().getSignManager().addSign(sign);

        player.sendMessage(Component.text("Sign added!", NamedTextColor.GREEN));

        return true;
    }
}
