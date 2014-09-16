package me.mgone.bossbarapi;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;


public class FWither {
    

    private static Constructor<?> packetPlayOutSpawnEntityLiving;
    private static Constructor<?> entityEntityWither;
    
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
    
    
    private static Map<String, Object> playerWithers = new HashMap<String, Object>();
    private static Map<String, Object> playerWithers2 = new HashMap<String, Object>();
    private static Map<String, String> playerTextWither = new HashMap<String, String>();

    static {
            try {

                    packetPlayOutSpawnEntityLiving = getMCClass("PacketPlayOutSpawnEntityLiving").getConstructor(getMCClass("EntityLiving"));
                    entityEntityWither = getMCClass("EntityWither").getConstructor(getMCClass("World"));

                    
                    setLocation = getMCClass("EntityWither").getMethod("setLocation", double.class, double.class, double.class, float.class, float.class); 
                    setCustomName = getMCClass("EntityWither").getMethod("setCustomName", new Class<?>[] { String.class });
                    setHealth = getMCClass("EntityWither").getMethod("setHealth", new Class<?>[] { float.class });
                    setInvisible = getMCClass("EntityWither").getMethod("setInvisible", new Class<?>[] { boolean.class });


                    getWorldHandle = getCraftClass("CraftWorld").getMethod("getHandle");
                    getPlayerHandle = getCraftClass("entity.CraftPlayer").getMethod("getHandle");
                    playerConnection = getMCClass("EntityPlayer").getDeclaredField("playerConnection");
                    sendPacket = getMCClass("PlayerConnection").getMethod("sendPacket", getMCClass("Packet"));

                    getDatawatcher = getMCClass("EntityWither").getMethod("getDataWatcher");
                    a = getMCClass("DataWatcher").getMethod("a", int.class, Object.class);
                    d = getMCClass("DataWatcher").getDeclaredField("d");
                    d.setAccessible(true);
            } catch (Exception e) {
                    e.printStackTrace();
            }
    }


    public static Object getWither(Player p) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
            if (playerWithers.containsKey(p.getName())) {
                    return playerWithers.get(p.getName());
            } else {
                    Object nms_world = getWorldHandle.invoke(p.getWorld());
                    playerWithers.put(p.getName(), entityEntityWither.newInstance(nms_world));
                    return getWither(p);
            }
    }
    
    
    
    public static Object getWither2(Player p) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        if (playerWithers2.containsKey(p.getName())) {
                return playerWithers2.get(p.getName());
        } else {
                Object nms_world = getWorldHandle.invoke(p.getWorld());
                playerWithers2.put(p.getName(), entityEntityWither.newInstance(nms_world));
                return getWither2(p);
        }
}

    
    
    public static void setBossBartext(Player p, String text) {
        playerTextWither.put(p.getName(), text);
	    int xr = ThreadLocalRandom.current().nextInt(-3,3);
	    int xr2 = ThreadLocalRandom.current().nextInt(-3,3);
	    
        try {
            Object nms_wither = getWither(p);
            setLocation.invoke(nms_wither, getPlayerLoc(p).getX()+xr, getPlayerLoc(p).getY()-3, getPlayerLoc(p).getZ()+xr2, 0F, 0F);
            setCustomName.invoke(nms_wither,text);
            setHealth.invoke(nms_wither,300);
            setInvisible.invoke(nms_wither,true);
            changeWatcher(nms_wither, text);
            Object nms_packet = packetPlayOutSpawnEntityLiving.newInstance(nms_wither);
            Object nms_player = getPlayerHandle.invoke(p);
            Object nms_connection = playerConnection.get(nms_player);
            sendPacket.invoke(nms_connection, nms_packet);
    } catch (Exception e) {
            e.printStackTrace();
    }
    
    
 /*   try {
        Object nms_wither = getWither2(p);
        setLocation.invoke(nms_wither, getPlayerLoc(p).getX()+xr2, p.getLocation().getY()-10, getPlayerLoc(p).getZ()+xr, 0F, 0F);
        setCustomName.invoke(nms_wither,text);
        setHealth.invoke(nms_wither,300);
        setInvisible.invoke(nms_wither,true);
        changeWatcher(nms_wither, text);
        Object nms_packet = packetPlayOutSpawnEntityLiving.newInstance(nms_wither);
        Object nms_player = getPlayerHandle.invoke(p);
        Object nms_connection = playerConnection.get(nms_player);
        sendPacket.invoke(nms_connection, nms_packet);
} catch (Exception e) {
        e.printStackTrace();
} */
}
    
    
    public static void setBossBar(Player p, String text, float vie) {
            playerTextWither.put(p.getName(), text);
    	    int xr = ThreadLocalRandom.current().nextInt(0,2);
    	    int xr2 = ThreadLocalRandom.current().nextInt(0,2);

            try {
                    Object nms_wither = getWither(p);
                    setLocation.invoke(nms_wither, getPlayerLoc(p).getX()+xr, getPlayerLoc(p).getY()-3, getPlayerLoc(p).getZ()+xr2, 0F, 0F);
                    setCustomName.invoke(nms_wither,text);
                    setHealth.invoke(nms_wither,vie);
                    setInvisible.invoke(nms_wither,true);
                    changeWatcher(nms_wither, text);
                    Object nms_packet = packetPlayOutSpawnEntityLiving.newInstance(nms_wither);
                    Object nms_player = getPlayerHandle.invoke(p);
                    Object nms_connection = playerConnection.get(nms_player);
                    sendPacket.invoke(nms_connection, nms_packet);
            } catch (Exception e) {
                    e.printStackTrace();
            }
            
            
        /*    try {
                Object nms_wither = getWither2(p);
                setLocation.invoke(nms_wither, getPlayerLoc(p).getX()+xr2, p.getLocation().getY()-10, getPlayerLoc(p).getZ()+xr, 0F, 0F);
                setCustomName.invoke(nms_wither,text);
                setHealth.invoke(nms_wither,vie);
                setInvisible.invoke(nms_wither,true);
                changeWatcher(nms_wither, text);
                Object nms_packet = packetPlayOutSpawnEntityLiving.newInstance(nms_wither);
                Object nms_player = getPlayerHandle.invoke(p);
                Object nms_connection = playerConnection.get(nms_player);
                sendPacket.invoke(nms_connection, nms_packet);
        } catch (Exception e) {
                e.printStackTrace();
        } */
            
    }
    
    
    public static void removeBossBar(Player p){
    	playerTextWither.remove(p.getName());
    	try {
            Object nms_wither = getWither(p);
            setLocation.invoke(nms_wither, p.getLocation().getX(), -5000, p.getLocation().getZ(), 0F, 0F);
            setCustomName.invoke(nms_wither," ");
            setHealth.invoke(nms_wither,0);
            setInvisible.invoke(nms_wither,true);
            changeWatcher(nms_wither, " ");
            Object nms_packet = packetPlayOutSpawnEntityLiving.newInstance(nms_wither);
            Object nms_player = getPlayerHandle.invoke(p);
            Object nms_connection = playerConnection.get(nms_player);
            sendPacket.invoke(nms_connection, nms_packet);
    } catch (Exception e) {
            e.printStackTrace();
    }

    }
    

    
    
	public static void removehorligneW(Player p) {
			playerWithers.remove(p.getName());
			playerTextWither.remove(p.getName());	
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
        } else if (337.5 <= rotation && rotation <= 360.0) {
            return "N";
        } else {
        	return "N";
        }
    }
    
    public static Location getPlayerLoc(Player p) {
    Location loc = p.getLocation();
    switch (getCardinalDirection(p)) {
      case ("N") :
          loc.add(0, 0, -50);
          break;
      case ("E") :
          loc.add(50, 0, 0);
          break;
      case ("S") :
          loc.add(0, 0, 50);
          break;
      case ("W") :
          loc.add(-50, 0, 0);
          break;
      case ("NE") :
          loc.add(50, 0, -50);
          break;
      case ("SE") :
          loc.add(50, 0, 50);
          break;
      case ("NW") :
          loc.add(-50, 0, -50);
          break;
    case ("SW") :
          loc.add(-50, 0, 50);
          break;
    }
    
    return loc;
    
    }

    

    
    

}
