package me.mgone.bossbarapi;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;


public class FDragon {
    

    private static Constructor<?> packetPlayOutSpawnEntityLiving;
    private static Constructor<?> entityEnderdragon;
    
    private static Method setLocation;
    private static Method setCustomName;
    private static Method setHealth;
    private static Method setInvisible;

    private static Method getWorldHandle;
    private static Method getPlayerHandle;
    private static Field playerConnection;
    private static Method sendPacket;

    private static Method getDatawatcher;
    private static Method a;
    private static Field d;
    
    
    public static Map<String, Object> playerDragons = new HashMap<String, Object>();
    public static Map<String, String> playerTextDragon = new HashMap<String, String>();

    static {
            try {

                    packetPlayOutSpawnEntityLiving = getMCClass("PacketPlayOutSpawnEntityLiving").getConstructor(getMCClass("EntityLiving"));
                    entityEnderdragon = getMCClass("EntityEnderDragon").getConstructor(getMCClass("World"));
                    
                    setLocation = getMCClass("EntityEnderDragon").getMethod("setLocation", double.class, double.class, double.class, float.class, float.class); 
                    setCustomName = getMCClass("EntityEnderDragon").getMethod("setCustomName", new Class<?>[] { String.class });
                    setHealth = getMCClass("EntityEnderDragon").getMethod("setHealth", new Class<?>[] { float.class });
                    setInvisible = getMCClass("EntityEnderDragon").getMethod("setInvisible", new Class<?>[] { boolean.class });


                    getWorldHandle = getCraftClass("CraftWorld").getMethod("getHandle");
                    getPlayerHandle = getCraftClass("entity.CraftPlayer").getMethod("getHandle");
                    playerConnection = getMCClass("EntityPlayer").getDeclaredField("playerConnection");
                    sendPacket = getMCClass("PlayerConnection").getMethod("sendPacket", getMCClass("Packet"));

                    getDatawatcher = getMCClass("EntityEnderDragon").getMethod("getDataWatcher");
                    a = getMCClass("DataWatcher").getMethod("a", int.class, Object.class);
                    d = getMCClass("DataWatcher").getDeclaredField("d");
                    d.setAccessible(true);
            } catch (Exception e) {
                    e.printStackTrace();
            }
    }


    public static Object getEnderDragon(Player p) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
            if (FDragon.playerDragons.containsKey(p.getName())) {
                    return FDragon.playerDragons.get(p.getName());
            } else {
                    Object nms_world = getWorldHandle.invoke(p.getWorld());
                    FDragon.playerDragons.put(p.getName(), entityEnderdragon.newInstance(nms_world));
                    return FDragon.getEnderDragon(p);
            }
    }

    
    
    public static void setBossBartext(Player p, String text) {
        playerTextDragon.put(p.getName(), text);
        try {
                Object nms_dragon = getEnderDragon(p);
                //setLocation.invoke(nms_dragon, loc.getX(), 150, loc.getZ(), 0F, 0F);
                setLocation.invoke(nms_dragon, getPlayerLoc(p).getX(), getPlayerLoc(p).getY()+800, getPlayerLoc(p).getZ(), 0F, 0F);
                setCustomName.invoke(nms_dragon,text);
                setHealth.invoke(nms_dragon,200);
                setInvisible.invoke(nms_dragon,true);
                changeWatcher(nms_dragon, text);
                Object nms_packet = packetPlayOutSpawnEntityLiving.newInstance(nms_dragon);
                Object nms_player = getPlayerHandle.invoke(p);
                Object nms_connection = playerConnection.get(nms_player);
                sendPacket.invoke(nms_connection, nms_packet);
        } catch (Exception e) {
                e.printStackTrace();
        }
}
    
    
    public static void setBossBar(Player p, String text, float vie) {
            playerTextDragon.put(p.getName(), text);

            try {
                    Object nms_dragon = getEnderDragon(p);
                    setLocation.invoke(nms_dragon, getPlayerLoc(p).getX(), getPlayerLoc(p).getY()+800, getPlayerLoc(p).getZ(), 0F, 0F);
                    setCustomName.invoke(nms_dragon,text);
                    setHealth.invoke(nms_dragon,vie);
                    setInvisible.invoke(nms_dragon,true);
                    changeWatcher(nms_dragon, text);
                    Object nms_packet = packetPlayOutSpawnEntityLiving.newInstance(nms_dragon);
                    Object nms_player = getPlayerHandle.invoke(p);
                    Object nms_connection = playerConnection.get(nms_player);
                    sendPacket.invoke(nms_connection, nms_packet);
            } catch (Exception e) {
                    e.printStackTrace();
            }
    }
    
    
    public static void removeBossBar(Player p){
    	playerTextDragon.remove(p.getName());
    	try {
            Object nms_dragon = getEnderDragon(p);
            setLocation.invoke(nms_dragon, p.getLocation().getX(), -5000, p.getLocation().getZ(), 0F, 0F);
            setCustomName.invoke(nms_dragon," ");
            setHealth.invoke(nms_dragon,0);
            setInvisible.invoke(nms_dragon,true);
            changeWatcher(nms_dragon, " ");
            Object nms_packet = packetPlayOutSpawnEntityLiving.newInstance(nms_dragon);
            Object nms_player = getPlayerHandle.invoke(p);
            Object nms_connection = playerConnection.get(nms_player);
            sendPacket.invoke(nms_connection, nms_packet);
    } catch (Exception e) {
            e.printStackTrace();
    }

    }
    
    
	public void removehorligneD(Player p) {
			playerDragons.remove(p.getName());
			playerTextDragon.remove(p.getName());	
		}
	


    private static void changeWatcher(Object nms_entity, String text) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            Object nms_watcher = getDatawatcher.invoke(nms_entity);
            Map<?, ?> map = (Map<?, ?>) d.get(nms_watcher);
            map.remove(10);
            a.invoke(nms_watcher, 10, text);
    }
    
    private static Class<?> getMCClass(String name) throws ClassNotFoundException {
            String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
            String className = "net.minecraft.server." + version + name;
            return Class.forName(className);
    }

    private static Class<?> getCraftClass(String name) throws ClassNotFoundException {
            String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
            String className = "org.bukkit.craftbukkit." + version + name;
            return Class.forName(className);
    }
    
    
    
    public static String getCardinalDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 180) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if (0 <= rotation && rotation < 22.5) {
            return "N";
        } else if (22.5 <= rotation && rotation < 67.5) {
            return "NE";
        } else if (67.5 <= rotation && rotation < 112.5) {
            return "E";
        } else if (112.5 <= rotation && rotation < 157.5) {
            return "SE";
        } else if (157.5 <= rotation && rotation < 202.5) {
            return "S";
        } else if (202.5 <= rotation && rotation < 247.5) {
            return "SW";
        } else if (247.5 <= rotation && rotation < 292.5) {
            return "W";
        } else if (292.5 <= rotation && rotation < 337.5) {
            return "NW";
        } else if (337.5 <= rotation && rotation < 360.0) {
            return "N";
        } else {
            return null;
        }
    }
    
    public static Location getPlayerLoc(Player p) {
    Location loc = p.getLocation();
    switch (getCardinalDirection(p)) {
      case ("N") :
          loc.add(0, 0, -150);
          break;
      case ("E") :
          loc.add(150, 0, 0);
          break;
      case ("S") :
          loc.add(0, 0, 150);
          break;
      case ("W") :
          loc.add(-150, 0, 0);
          break;
      case ("NE") :
          loc.add(150, 0, -150);
          break;
      case ("SE") :
          loc.add(150, 0, 150);
          break;
      case ("NW") :
          loc.add(-150, 0, -150);
          break;
    case ("SW") :
          loc.add(-150, 0, 150);
          break;
    }
    
    return loc;
    
    }

    

    
    

}
