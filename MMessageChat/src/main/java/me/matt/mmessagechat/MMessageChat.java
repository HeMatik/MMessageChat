package me.matt.mmessagechat;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MMessageChat extends JavaPlugin {
    private final Map<UUID, ArmorStand> chatMessages = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new ChatAboveHeadListener(), this);
    }

    private class ChatAboveHeadListener implements Listener {
        @EventHandler
        public void onPlayerChat(PlayerChatEvent event) {
            Player player = event.getPlayer();
            Location playerLocation = player.getLocation();
            ArmorStand armorStand = player.getWorld().spawn(playerLocation.add(0, 0, 0), ArmorStand.class);
            armorStand.setCustomName(event.getMessage());
            armorStand.setCustomNameVisible(true);
            armorStand.setSmall(false);
            armorStand.setInvisible(true);
            armorStand.setGravity(false);
            chatMessages.put(player.getUniqueId(), armorStand);

            new BukkitRunnable() {
                @Override
                public void run() {
                    Location playerLocation = player.getLocation().add(0,0,0);
                    armorStand.teleport(playerLocation);
                }
            }.runTaskTimer(MMessageChat.this, 0L, 1L);

            new BukkitRunnable() {
                @Override
                public void run() {
                    ArmorStand storedArmorStand = chatMessages.remove(player.getUniqueId());
                    if (storedArmorStand != null) {
                        storedArmorStand.remove();
                    }
                }
            }.runTaskLater(MMessageChat.this, 7 * 20L);
        }
    }
}