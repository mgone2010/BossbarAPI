BossbarAPI
==========

Work with Bukkit/SPIGOT 1.7 and 1.8
​
Features:
- AutoMessage
- Player name support
- Custom API

Commands:
/BossBar reload - bossbarapi.reload

Installation:
Simply put `BossBArAPI.jar` in Bukkit/spigot plugins Folder.
Restart/reload the server , then edit the config.

To Do
- Sppport icon
- Support more Variables: World, Onlive player,Rank, server, Economy....

API
import me.mgone.bossbarapi.BossbarAPI

BossbarAPI.setBarDragon(Player p, String text)
BossbarAPI.setBarDragonHealth(Player p, String text, float health)
BossbarAPI.setBarDragonTimer(Player p, String text, int seconds)
BossbarAPI.removeBarDragon(Player p)
BossbarAPI.hasBarDragon(Player p)


BossbarAPI.setBarWither(Player p, String text)
BossbarAPI.setBarWitherHealth(Player p, String text, float health)
BossbarAPI.setBarWitherTimer(Player p, String text, int seconds)
BossbarAPI.removeBarWither(Player p)
BossbarAPI.hasBarWither(Player p)

you cas use the both Wither and Enderdragon with the API, But i suggest you to use only the enderdragon for 1.7 client and the both for 1.8 client
BossbarAPI.McVersion(Player p) return true when player using 1.8 or more and false if not

Bug
Bossbar API doesn't work perfectly for client 1.8, but it's stable.
for client 1.7 there is no problem and work with ender dragon bar like charm.
client 1.8 work with the both enderdragon and wither bar, sometime time the bar disappear, because player don't look (the invisible) enderdragon /wither.
to prevent this problem i must teleport the wither front of the player every time he move or change direction , teleport package is broken with this spigot/bukkit version, then i must resend all package to client when and change location of wither, i dont do this with hight frequency to prevent lag, but i will find a way to resolve this , stay update ;)

Source
https://github.com/mgone2010/BossbarAPI

Metrics
This plugin uses Metrics, to track anonymous data about servers using this plugin. It will help the future developement, and has no impact on your server's performance. If you really want to disable it, go to the folder /plugins/PluginMetrics and set opt-out: true.
