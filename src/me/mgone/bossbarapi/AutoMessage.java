package me.mgone.bossbarapi;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class AutoMessage {
	
	 public int interval = 0;
	  public int MessageID = 0;
	  public int number = 0;
	  public int total = 0;
    
	
		
		
	
	  
	@SuppressWarnings("deprecation")
	public AutoMessage(final Plugin plugin) {
		
		
		if(plugin.getConfig().getBoolean("EnableAutoMessage")){	
		
		total = plugin.getConfig().getConfigurationSection("messages").getKeys(false).size();
		interval = plugin.getConfig().getInt("interval");
		
		if(interval < 1) for (Player pe : Bukkit.getOnlinePlayers()) { if(pe.isOp()) pe.sendMessage("§c[BossbarAPI] §finterval can't be < 1!!!!"); return; } //must be change error console
          
          Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin,
					new Runnable() {
						public void run() {
							
							
					        getMessageID();
					        String message = plugin.getConfig().getString("messages." + MessageID + ".message");
					        message = message.replace("&", "§");
					        
					        for (Player p : Bukkit.getOnlinePlayers())
					        {   
					        	
					        	message = message.replace("%player%", p.getName());
					        	if(BossbarAPI.McVersion(p)) { BossbarAPI.setBarWither(p, message);  }
					        	BossbarAPI.setBarDragon(p, message);
					        }
							

   
				}
						
          }, 100, interval * 20);
          
		} 
  }
	
	
	

	  public void getMessageID()
	  {
	    if (number < total)
	    {
	      if (BossbarAPI.plugin.getConfig().getString("messages." + number + ".message") != null)
	      {
	    	MessageID = number;
	        number += 1;
	      }
	      else
	      {
	        number += 1;
	        total += 1;
	        getMessageID();
	      }
	    }
	    else
	    {
	      number = 0;
	      total = BossbarAPI.plugin.getConfig().getConfigurationSection("messages").getKeys(false).size();
	      getMessageID();
	    }
	  }
	
	

}
