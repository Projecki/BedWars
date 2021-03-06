package org.screamingsandals.bedwars.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.bedwars.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.lib.debug.Debug;

public class BungeeUtils {
    public static void movePlayerToBungeeServer(Player player, boolean serverRestart) {
        if (serverRestart) {
            internalMove(player, true);
            return;
        }

        new BukkitRunnable() {
            public void run() {
               internalMove(player, false);
            }
        }.runTask(Main.getInstance().getPluginDescription().as(JavaPlugin.class));
    }

    public static void sendPlayerBungeeMessage(Player player, String string) {
        new BukkitRunnable() {
            public void run() {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();

                out.writeUTF("Message");
                out.writeUTF(player.getName());
                out.writeUTF(string);

                Bukkit.getServer().sendPluginMessage(Main.getInstance().getPluginDescription().as(JavaPlugin.class), "BungeeCord", out.toByteArray());
            }
        }.runTaskLater(Main.getInstance().getPluginDescription().as(JavaPlugin.class), 30L);
    }

    private static void internalMove(Player player, boolean restart) {
        String server = Main.getConfigurator().node("bungee", "server").getString();
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("Connect");
        out.writeUTF(server);

        player.sendPluginMessage(Main.getInstance().getPluginDescription().as(JavaPlugin.class), "BungeeCord", out.toByteArray());
        Debug.info("player " + player.getName() + " has been moved to hub server ");
        if (!restart && Main.getConfigurator().node("bungee", "kick-when-proxy-too-slow").getBoolean()) {
            Bukkit.getScheduler().runTaskLater(Main.getInstance().getPluginDescription().as(JavaPlugin.class), () -> {
                if (player.isOnline()) {
                    player.kickPlayer("Bedwars can't properly transfer player through bungee network. Contact server admin.");
                }
            }, 20L);
        }
    }
}