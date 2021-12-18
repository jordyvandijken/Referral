package Me.Teenaapje.Referral.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Me.Teenaapje.Referral.Utils.Utils;

public class RefCount extends CommandBase {
	// init class
	public RefCount() {
		 permission = "RefCount";
		 command = "Total";
		 forPlayerOnly = false;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// check arguments
		if (args.length > 2) {
	        Utils.SendMessage(sender, core.config.tooManyArgs);
	        return false;
	    } else if (args.length < 2) {
	    	// check if is player
			if (Utils.IsConsole(sender)) {
		        Utils.SendMessage(sender, core.config.missingPlayer);
				return false;
			}
			
			Utils.SendMessage((Player)sender, core.config.playerTotal);
	    } else {
	    	// check if the player is online
			Player target = core.GetPlayer(args[1]);
	    	
			if (target == null) {
				Utils.SendMessage(sender, core.config.notOnline);
				return false;
			} else {
				Utils.SendMessage(sender, core.config.playerTotal, target);
				
			}
	    }	
		
		return true;
	}
}