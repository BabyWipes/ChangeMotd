package code.husky;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ChangeMotd extends JavaPlugin implements Listener {

	YamlConfiguration config = YamlConfiguration.loadConfiguration(new File("plugins/ChangeMotd/config.yml"));
	List<String> motd = null;
	int cur = 0;
	int len = 0;

	public void onEnable() {
		createConfig();
		motd = config.getStringList("motd-changeto");
		len = motd.size();
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){

			@Override
			public void run() {
				cur++;
			}
		}, (config.getInt("motd-change-interval") * 20), 20L);
		
		getServer().getPluginManager().registerEvents(this,this);
	}

	public void onDisable() {}

	private void createConfig() {
		motd = new ArrayList<String>();
		File f = new File("plugins/ChangeMotd/config.yml");
		if (!f.exists()) {
			motd.add("This is a motd");
			config.options().header("-- ChangeMotd By Husky --");
			config.set("motd-change-interval", 120);
			config.set("motd-changeto", motd);
			try {
				config.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String setMOTD() {
		if(cur < len) {
			return motd.get(cur);
		} else if(cur == len) {
			cur = 0;
			return motd.get(cur);
		}
		return "Error";
	}

	@EventHandler
	public void onServerListPing(ServerListPingEvent e) {
		e.setMotd(setMOTD());
	}
}