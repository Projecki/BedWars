package org.screamingsandals.bedwars.commands.admin;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.sender.CommandSenderWrapper;

import static org.screamingsandals.bedwars.lib.lang.I.i18n;

public class Pos2Command extends BaseAdminSubCommand {
    public Pos2Command(CommandManager<CommandSenderWrapper> manager, Command.Builder<CommandSenderWrapper> commandSenderWrapperBuilder) {
        super(manager, commandSenderWrapperBuilder, "pos2");
    }

    @Override
    public void construct(Command.Builder<CommandSenderWrapper> commandSenderWrapperBuilder) {
        manager.command(
                commandSenderWrapperBuilder
                        .handler(commandContext -> editMode(commandContext, (sender, game) -> {
                            var loc = sender.as(Player.class).getLocation();

                            if (game.getWorld() == null) {
                                game.setWorld(loc.getWorld());
                            }
                            if (game.getWorld() != loc.getWorld()) {
                                sender.sendMessage(i18n("admin_command_must_be_in_same_world"));
                                return;
                            }
                            if (game.getPos1() != null) {
                                if (Math.abs(game.getPos1().getBlockY() - loc.getBlockY()) <= 5) {
                                    sender.sendMessage(i18n("admin_command_pos1_pos2_difference_must_be_higher"));
                                    return;
                                }
                            }
                            game.setPos2(loc);
                            sender.sendMessage(
                                    i18n("admin_command_pos2_setted")
                                            .replace("%arena%", game.getName())
                                            .replace("%x%", Integer.toString(loc.getBlockX()))
                                            .replace("%y%", Integer.toString(loc.getBlockY()))
                                            .replace("%z%", Integer.toString(loc.getBlockZ()))
                            );
                        }))
        );
    }
}
