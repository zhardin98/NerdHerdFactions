package me.ZacharyPeculier.NerdHerdFactions.Listeners;

import java.util.Set;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class PlayerListener implements Listener {
	
	private final Set<UUID> combatPlayers = new HashSet<UUID>();
	
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
    	Player player = event.getPlayer();
    	
        event.setJoinMessage("Welcome, " + event.getPlayer().getName() + "!");
        
        if (combatPlayers.contains(player.getUniqueId())) {
        	player.getInventory().clear();
        	player.setHealth(0);
        	combatPlayers.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
    	Player player = event.getPlayer();
        event.setQuitMessage(player.getName() + " has left.");
        
        if (combatPlayers.contains(player.getUniqueId())) {
            for (ItemStack itemStack : player.getInventory().getContents()) {
            	if (itemStack == null) {
            		continue;
            	}
            	
                player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
                player.getInventory().remove(itemStack);
            }
        }
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
    	Entity entity = event.getEntity();
    	
    	if (entity instanceof Player) {
    		Player player = (Player)entity;
    		
    		if (combatPlayers.contains(player.getUniqueId())) {
    			combatPlayers.remove(player.getUniqueId());
    		}
    	}
    }
    
    private void addCombatPlayer(Player player) {
    	if (combatPlayers.contains(player.getUniqueId())) {
    		return;
    	}
    	
    	player.sendMessage(ChatColor.RED + "You are now in combat, if you leave you will be killed.");
    	combatPlayers.add(player.getUniqueId());
    }

    @EventHandler
    public void onPvPDamage(EntityDamageByEntityEvent event)
    {
        Entity attacked = event.getEntity();
        Entity attacker = event.getDamager();
        
        if (attacked instanceof Player && attacker instanceof Player) {
        	addCombatPlayer((Player)attacked);
        	addCombatPlayer((Player)attacker);
        }
    }
}
