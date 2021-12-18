package Me.Teenaapje.Referral.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Me.Teenaapje.Referral.Utils.Utils;

public class RefAdmin extends CommandBase {
	// init class
	public RefAdmin() {
		 permission = "RefAdmin";
		 command = "Admin";
		 forPlayerOnly = false;
		 subCommands = new String[] {"Reset", "Remove"};
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// check arguments
		if (args.length > 3) {
	        Utils.SendMessage(sender, core.config.tooManyArgs);
	        return false;
	    } else if (args.length >= 2 && args.length < 3) {
	        Utils.SendMessage(sender, core.config.missingPlayer);
	        return false;
	    } else if (args.length < 2) {
	        Utils.SendMessage(sender, core.config.missingArgs);
	        return false;
	    } 
		
		
		switch (args[1].toLowerCase()) {
		case "remove":
			// remove player from database
			if (args[2].compareTo("*") == 0) {
				core.db.RemoveAll();
				Utils.SendMessage(sender, core.config.allRemoved);
			} else {
				Player player = core.GetPlayer(args[2]);

				if (player != null && RemovePlayer(player)) {
					// Player removed
					Utils.SendMessage(sender, core.config.playerRemoved, player);
				} else {
					// Player removed failed
					Utils.SendMessage(sender, core.config.playerRemovedFailed, player);
				}
			}
			break;
		case "reset":
			// reset player
			if (args[2].compareTo("*") == 0) {
				core.db.ResetAll();
				Utils.SendMessage(sender, core.config.allReset);
			} else {
				Player player = core.GetPlayer(args[2]);

				if (player != null && ResetPlayer(player)) {
					// Player reset
					Utils.SendMessage(sender, core.config.playerReset, player);
				} else {
					// Player reset failed
					Utils.SendMessage(sender, core.config.playerResetFailed, player);
				}
			}
			break;
		default:
			Utils.SendMessage(sender, "&cIncorrect use of command");
			break;
		}
		return true;
	}
	
	private boolean RemovePlayer(Player player) {
		return core.db.PlayerRemove(player.getUniqueId().toString());
	}
	
	private boolean ResetPlayer(Player player) {
	    return core.db.PlayerReset(player.getUniqueId().toString());
	}

}
