package Me.Teenaapje.Referral.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Me.Teenaapje.Referral.ReferralCore;
import Me.Teenaapje.Referral.Utils.Utils;

public class CommandBase {
	ReferralCore core = ReferralCore.core;
	
	public String permission = "";
	public String command = "";
	public boolean forPlayerOnly = false;
	public String[] subCommands = {};
	
	public CommandBase () {
	}
	
	public boolean HasPermission (CommandSender sender, boolean sendmsg) {
		if (permission == "") {
			return true;
		}
		
		if (sender instanceof Player) {
			if (sender.hasPermission("Referral." + permission)) {
				return true;
			} else {
				if (sendmsg) {
					Utils.SendMessage((Player)sender, core.config.noPerm);
				}
				return false;
			}
		}
		return true;
	}
	
	public boolean CanUse (CommandSender sender, boolean sendmsg) {
		// check if is player
		if (forPlayerOnly && !(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can use this command.");
			return false;
		}
		
		// check perm
		return HasPermission(sender, sendmsg);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return true;
	}
}
