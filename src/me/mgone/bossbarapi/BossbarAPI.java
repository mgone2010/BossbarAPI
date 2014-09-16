package me.mgone.bossbarapi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


/**
 * Thanks for chasechocolate and BigTeddy98 for the tutorial
 * it is based on the code by them and some other tutorial
 * https://forums.bukkit.org/threads/util-set-a-players-boss-bar-nms.245073/
 * https://forums.bukkit.org/threads/tutorial-utilizing-the-boss-health-bar.158018/
 * @author Marzouki Ghofrane , mgone CraftZone.fr
 */


public class BossbarAPI extends JavaPlugin implements Listener {
	
	
	public static Plugin plugin;
	public static WeakHashMap<Player, String> playerdragonbartask = new WeakHashMap<Player, String>();
	public static WeakHashMap<Player, Float> healthdragonbartask = new WeakHashMap<Player, Float>();
	public static WeakHashMap<Player, Integer> cooldownsdragonbar= new WeakHashMap<Player, Integer>();
	public static WeakHashMap<Player, Integer> starttimerdragonbar= new WeakHashMap<Player, Integer>();
	
	public static WeakHashMap<Player, String> playerwitherbartask = new WeakHashMap<Player, String>();
	public static WeakHashMap<Player, Float> healthwitherbartask = new WeakHashMap<Player, Float>();
	public static WeakHashMap<Player, Integer> cooldownswitherbar= new WeakHashMap<Player, Integer>();
	public static WeakHashMap<Player, Integer> starttimerwitherbar= new WeakHashMap<Player, Integer>();
	
	  public void onEnable()
	  {   
		  
		  getServer().getPluginManager().registerEvents(this, this);
		  getConfig().options().copyDefaults(true);
		  saveDefaultConfig();
		  reloadConfig();
		  plugin = this;
		  Bukkit.getConsoleSender().sendMessage("§e§lBossbarAPI Enabled...");
		  DragonBarTask();
		  //new AutoMessage(this);
		  
		  try {
			  Metrics metrics = new Metrics(this); metrics.start();
			  } catch (IOException e) { // Failed to submit the stats :-(
			  System.out.println("Error Submitting stats!");
			  }
	  }
		  
		  
		  
	  public void onDisable()
	  {
	    Bukkit.getConsoleSender().sendMessage("§3§lBossbarAPI Disabled...");
	  }
	
	
	  public static Plugin getInstance(){
		    return plugin;
		}
	  
	  
	  
	  public void reloadConfig()
	  {
	    super.reloadConfig();
	    new AutoMessage(this);
	    
	  }
	  
	  
      public void DragonBarTask() {
         
         new BukkitRunnable() {
                 
                  @SuppressWarnings("deprecation")
					@Override
                  public void run() {
                          for(Player p : plugin.getServer().getOnlinePlayers()){
                       
                        	if(!cooldownsdragonbar.containsKey(p))	{ 
                        	  
                         if(playerdragonbartask.containsKey(p) && !healthdragonbartask.containsKey(p))	{ setBarDragon(p, playerdragonbartask.get(p)); }
                         else if(playerdragonbartask.containsKey(p) && healthdragonbartask.containsKey(p))	{ setBarDragonHealth(p, playerdragonbartask.get(p), healthdragonbartask.get(p)); }
                         
                        	                 }
                        	
                        	if(!cooldownswitherbar.containsKey(p))	{ 
                          	  
                         if(playerwitherbartask.containsKey(p) && !healthwitherbartask.containsKey(p))	{ setBarWither(p, playerwitherbartask.get(p)); }
                         else if(playerwitherbartask.containsKey(p) && healthwitherbartask.containsKey(p))	{ setBarWitherHealth(p, playerwitherbartask.get(p), healthwitherbartask.get(p)); }
                         
                        	                 }
                            
                          }
                  }
          }.runTaskTimer(this, 0, 40); 
          
          
          
          
          new BukkitRunnable() {
              
              @SuppressWarnings("deprecation")
				@Override
              public void run() {
                      for(Player p : plugin.getServer().getOnlinePlayers()){
                    	  
                     if(cooldownsdragonbar.containsKey(p))	{ 
                    	 
                    	if(cooldownsdragonbar.get(p) > 0) 
                    	{ cooldownsdragonbar.put(p,cooldownsdragonbar.get(p)-1); setBarDragonTimer(p, playerdragonbartask.get(p), cooldownsdragonbar.get(p)); }
                    	else removeBarDragon(p);

                     }
                     
                     
                     if(cooldownswitherbar.containsKey(p))	{ 
                    	 
                    	if(cooldownswitherbar.get(p) > 0) 
                    	{ cooldownswitherbar.put(p,cooldownswitherbar.get(p)-1); setBarWitherTimer(p, playerwitherbartask.get(p), cooldownswitherbar.get(p)); }
                    	else removeBarWither(p);

                     }
                        
                      }
              }
      }.runTaskTimer(this, 0, 20); 
          
          
  }
      
      
      
     @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  	public void PlayerQuit(PlayerQuitEvent event) {
  		Player p = event.getPlayer();
  		removeBar(p);
  		FDragon.removehorligneD(p);
  		FWither.removehorligneW(p);
  	}

  	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  	public void PlayerKick(PlayerKickEvent event) {
  		Player p = event.getPlayer();
  		removeBar(p);
  		FDragon.removehorligneD(p);
  		FWither.removehorligneW(p);
  	}

      
      //dragon
	  
	  
	  
		public static void setBarDragon(Player p, String text) {
			playerdragonbartask.put(p, text);
			FDragon.setBossBartext(p, text);
		}
	
	  
		public static void setBarDragonHealth(Player p, String text, float health) {
			if(health<=0 || health >100) { health = 100; text = "health must be between 1 and 100 it's a %";}
			playerdragonbartask.put(p, text);
			healthdragonbartask.put(p, (health/100)*200);
			FDragon.setBossBar(p, text, health);
		}
		
		public static void setBarDragonTimer(Player p, String text, int timer) {
			playerdragonbartask.put(p, text);
			cooldownsdragonbar.put(p, timer);
			if(!starttimerdragonbar.containsKey(p)) starttimerdragonbar.put(p, timer);
			int unite = Math.round(200/starttimerdragonbar.get(p));
			FDragon.setBossBar(p, text, unite*timer);
		
		}
		
		
		public static void removeBarDragon(Player p) {	
			playerdragonbartask.remove(p);
			healthdragonbartask.remove(p);
			cooldownsdragonbar.remove(p);
			starttimerdragonbar.remove(p);
			FDragon.removeBossBar(p);
		}
		
		public static boolean hasBarDragon(Player p) {	
			return playerdragonbartask.get(p) != null;	
		}
		
		
		public static String getMessageDragon(Player p) {	
			if(playerdragonbartask.containsKey(p)) return playerdragonbartask.get(p);
			else return " ";
		}
		
		
		
		
		//wither
		
		public static void setBarWither(Player p, String text) {
			playerwitherbartask.put(p, text);
			FWither.setBossBartext(p, text);
		}
	
	  
		public static void setBarWitherHealth(Player p, String text, float health) {
			if(health<=0 || health >100) { health = 100; text = "health must be between 1 and 100 it's a %";}
			playerwitherbartask.put(p, text);
			healthwitherbartask.put(p, (health/100)*300);
			FWither.setBossBar(p, text, health);
		}
		
		public static void setBarWitherTimer(Player p, String text, int timer) {
			playerwitherbartask.put(p, text);
			cooldownswitherbar.put(p, timer);
			if(!starttimerwitherbar.containsKey(p)) starttimerwitherbar.put(p, timer);
			int unite = Math.round(300/starttimerwitherbar.get(p));
			FWither.setBossBar(p, text, unite*timer);
		
		}
		
		
		public static void removeBarWither(Player p) {	
			playerwitherbartask.remove(p);
			healthwitherbartask.remove(p);
			cooldownswitherbar.remove(p);
			starttimerwitherbar.remove(p);
			FWither.removeBossBar(p);
		}
		
		public static boolean hasBarWither(Player p) {	
			return playerwitherbartask.get(p) != null;	
		}
		
		
		public static String getMessageWither(Player p) {	
			if(playerwitherbartask.containsKey(p)) return playerwitherbartask.get(p);
			else return " ";
		}
		
		
		
		
		
		//both
		
		public static void setBar(Player p, String text) {
			if(McVersion(p)) {
			playerwitherbartask.put(p, text);
			FWither.setBossBartext(p, text); }
			
			playerdragonbartask.put(p, text);
			FDragon.setBossBartext(p, text);
		}
	
	  
		public static void setBarHealth(Player p, String text, float health) {
			if(health<=0 || health >100) { health = 100; text = "health must be between 1 and 100 it's a %";}
			if(McVersion(p)) {
			playerwitherbartask.put(p, text);
			healthwitherbartask.put(p, (health/100)*300);
			FWither.setBossBar(p, text, health); }
			
			playerdragonbartask.put(p, text);
			healthdragonbartask.put(p, (health/100)*200);
			FDragon.setBossBar(p, text, health);
		}
		
		public static void setBarTimer(Player p, String text, int timer) {
			if(McVersion(p)) {
			playerwitherbartask.put(p, text);
			cooldownswitherbar.put(p, timer);
			if(!starttimerwitherbar.containsKey(p)) starttimerwitherbar.put(p, timer);
			int unite = Math.round(300/starttimerwitherbar.get(p));
			FWither.setBossBar(p, text, unite*timer); }
			
			playerdragonbartask.put(p, text);
			cooldownsdragonbar.put(p, timer);
			if(!starttimerdragonbar.containsKey(p)) starttimerdragonbar.put(p, timer);
			int unite1 = Math.round(200/starttimerdragonbar.get(p));
			FDragon.setBossBar(p, text, unite1*timer);
		
		}
		
		
		public static void removeBar(Player p) {
			if(McVersion(p)) {
			playerwitherbartask.remove(p);
			healthwitherbartask.remove(p);
			cooldownswitherbar.remove(p);
			starttimerwitherbar.remove(p);
			FWither.removeBossBar(p); }
			
			playerdragonbartask.remove(p);
			healthdragonbartask.remove(p);
			cooldownsdragonbar.remove(p);
			starttimerdragonbar.remove(p);
			FDragon.removeBossBar(p);
		}
		
		public static boolean hasBar(Player p) {	
			
			if(McVersion(p)) {
			
			if(playerwitherbartask.containsKey(p) && playerdragonbartask.containsKey(p))
				return true;
			else return false; }
			
			
			else {
				
				return playerdragonbartask.get(p) != null;	
			}
		}
		
		
		public static String getMessage(Player p) {	
			if(playerdragonbartask.containsKey(p)) return playerdragonbartask.get(p);
			else return " ";
		}
		
		
		
		
		public static boolean McVersion(Player p) {
			CraftPlayer p1 = (CraftPlayer) p;
			if (p1.getHandle().playerConnection.networkManager.getVersion() >= 47) return true;
			else return false;
		}
		
		
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
		  {
			
			
		    if ((sender instanceof Player))
		    {  Player p = (Player)sender;
		    
		    if (cmd.getName().equalsIgnoreCase("Bossbar"))
		    {
		    	
		    	

		    	if (args.length == 1) {
		    	
		    	
		    	if (args[0].equalsIgnoreCase("reload")) {
		    		

		    	if(p.hasPermission("bossbarapi.reload")) {
		        	reloadConfig();
		            p.sendMessage(ChatColor.GOLD + "[BossbarAPI] " + ChatColor.GRAY + "You have reloaded config."); }
		        	else  p.sendMessage(ChatColor.GOLD + "[BossbarAPI] " + ChatColor.GRAY + "You don't have permission ;).");
		    	
		    	} //fin reload
		    	
		    	} else p.sendMessage(ChatColor.GOLD + "BossbarAPI " + ChatColor.GRAY + "V2.1 \n" + ChatColor.LIGHT_PURPLE + "Plugin By " + ChatColor.AQUA + "mgone2010");

		    
		    
		    
		    
		    }
			
		    } 
			
		    return false;
			
			
		  }
		   
		 
		     


}
