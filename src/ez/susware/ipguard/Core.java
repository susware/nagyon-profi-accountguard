package ez.susware.ipguard;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin implements CommandExecutor, Listener {

	public void onEnable() {
		this.getConfig().addDefault("ip", 0);
		this.getConfig().options().copyDefaults(true);
		saveConfig();
		
        this.getServer().getPluginManager().registerEvents(this, this);
	}
	
	public void onDisable() {
		saveConfig();
	}
	
	Core pl;
	FileConfiguration config = this.getConfig();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("ip")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				String path = "ip." + p.getName().toLowerCase();
				if(args.length == 0) {
					if(config.contains(path)) {
						p.sendMessage("§cYou've already protected your ip");
						return true;
					} else {
						String ip = p.getAddress().toString().replace("/", "");
						config.set(path, ip);
						p.sendMessage("§aSuccessfully protected your ip address");
					}
				} else if(args.length == 1) {
					if(p.isOp()) {
						Player t = Bukkit.getPlayer(args[0]);
						if(t == null) {
							p.sendMessage("§cThis player is currently offline");
							return true;
						} else {
							String pathx = "ip." + t.getName().toLowerCase();
							String ipx = t.getAddress().toString().replace("/", "");
							if(config.contains(pathx)) {
								p.sendMessage("§cThis player already protected his ip");
								return true;
							} else {
								config.set(pathx, ipx);
								p.sendMessage("§aSuccessfully protected §7" + t.getName() + "§a's address");
							}
						}
					} else {
						p.sendMessage("§cUsage: /ip");
						return true;
					}
				} else {
					if(p.isOp()) {
						p.sendMessage("§cUsage: /ip (player)");
						return true;
					} else {
						p.sendMessage("§cUsage: /ip");
						return true;
					}
				}
			} else {
				if(args.length != 1) {
					sender.sendMessage("§cUsage: /ip (player)");
				} else {
					Player t = Bukkit.getPlayer(args[0]);
					if(t == null) {
						sender.sendMessage("§cThis player is currently offline");
						return true;
					} else {
						String pathx = "ip." + t.getName().toLowerCase();
						String ipx = t.getAddress().toString().replace("/", "");
						if(config.contains(pathx)) {
							sender.sendMessage("§cThis player already protected his ip");
							return true;
						} else {
							config.set(pathx, ipx);
							sender.sendMessage("§aSuccessfully protected §7" + t.getName() + "§a's address");
						}
					}
				}
			}
		}
		if(cmd.getName().equalsIgnoreCase("removeip")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				String path = "ip." + p.getName().toLowerCase();
				if(args.length == 0) {
					if(config.contains(path)) {
						config.set(path, null);
						p.sendMessage("§aSuccessfully removed your ip address");
					} else {
						p.sendMessage("§cYou did not protect his ip yet");
						return true;
					}
				} else if(args.length == 1) {
					if(p.isOp()) {
						String pathx = "ip." + args[0].toLowerCase();
						if(config.contains(pathx)) {
							config.set(pathx, null);
							sender.sendMessage("§aSuccessfully removed §7" + args[0] + "§a's address");
						} else {
							sender.sendMessage("§cThis player did not protect his ip yet");
							return true;
						}
					} else {
						p.sendMessage("§cUsage: /removeip");
						return true;
					}
				} else {
					if(p.isOp()) {
						p.sendMessage("§cUsage: /removeip (player)");
						return true;
					} else {
						p.sendMessage("§cUsage: /removeip");
						return true;
					}
				}
			} else {
				if(args.length != 1) {
					sender.sendMessage("§cUsage: /removeip (player)");
				} else {
					String pathx = "ip." + args[0].toLowerCase();
					if(config.contains(pathx)) {
						config.set(pathx, null);
						sender.sendMessage("§aSuccessfully removed §7" + args[0] + "§a's address");
					} else {
						sender.sendMessage("§cThis player did not protect his ip yet");
						return true;
					}
				}
			}
		}
		return false;	
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@EventHandler
	public void onLog(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		String path = "ip." + p.getName().toLowerCase();
		String ip = config.getString("ip." + p.getName().toLowerCase());
		if(config.contains(path)) {
			if(p.getAddress().equals(ip.toString())) {
				return;
			} else {
				p.kickPlayer("§cYour current ip doesn't match with the protected one. Contact an admin");
				return;
			}
		}
	}
}
