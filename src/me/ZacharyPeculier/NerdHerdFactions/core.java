package me.ZacharyPeculier.NerdHerdFactions;

import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class core extends JavaPlugin
{
	public final java.util.logging.Logger logger = Logger.getLogger("Minecraft");
	World world;
    int spawnX = 0;
    int spawnY = 69;
    int spawnZ = 0;
    
    
    @Override
    public void onDisable()
    {
    	PluginDescriptionFile pdFile = this.getDescription();
        this.logger.info(pdFile.getName() + " version " + pdFile.getVersion() + " has been deactivated!");
    }
    
    @Override
    public void onEnable()
    {
    	PluginDescriptionFile pdFile = this.getDescription();
        this.logger.info(pdFile.getName() + " version " + pdFile.getVersion() + " has been activated!");
        world = getServer().getWorld("world");
        world.setSpawnLocation(spawnX, spawnY, spawnZ);
        saveDefaultConfig();
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
    	Player player = (Player) sender;
    	UUID playerID = player.getUniqueId();
    	if(commandLabel.equalsIgnoreCase("spawn"))
    	{
    		player.teleport(new Location(getServer().getWorld("world"), spawnX, spawnY, spawnZ));
    		return true;
    	}
    	else if (commandLabel.equalsIgnoreCase("sethome"))
    	{
    		Location home = new Location(getServer().getWorld("world"), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
    		getConfig().set(playerID.toString() + ".homeLocation", home);
    		player.sendMessage(ChatColor.GOLD + "Your home has been set!");
    		saveConfig();
    		return true;
    	}
    	else if (commandLabel.equalsIgnoreCase("home"))
    	{
    		try
    		{
    			Location home = getConfig().getLocation(playerID.toString() + ".homeLocation");
    			player.teleport(home);
    		}
    		catch(Exception e)
    		{
    			player.sendMessage(ChatColor.DARK_RED + "You have no home");
    		}
    		return true;
    	}
    	else
    	{
    		player.sendMessage(ChatColor.RED + "Unknown command");
    		return false;
    	}
		
    }
}
