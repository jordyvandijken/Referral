package Me.Teenaapje.Referral.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Me.Teenaapje.Referral.Utils.Utils;

public class RefReject extends CommandBase {
	public RefReject() {
		 permission = "RefReject";
		 command = "Reject";
		 forPlayerOnly = true;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// check arguments
		if (args.length > 2) {
	        Utils.SendMessage(sender, core.config.tooManyArgs);
	        return false;
	    } else if (args.length < 2) {
	        Utils.SendMessage(sender, core.config.missingPlayer);
	        return false;
	    } 
		
		// check if the player wants to reject him self
		if (Utils.IsPlayerSelf((Player)sender, args[1])) {
	        Utils.SendMessage(sender, core.config.rejectSomeone); // no need to reject yourself
	        return false;
		}
		
		Player reject = core.GetPlayer(args[1]);
		
		// check if is in list
		if (!core.rInvites.IsInList(sender.getName(), args[1])) {
	        Utils.SendMessage(sender, core.config.didntRef, reject);
			return false;
		}
		
		if (reject.isOnline()) {
			Utils.SendMessage(reject, core.config.playerGotRej, (Player)sender);
		}
		
		// rmove from list
		core.rInvites.RemoveFromList(((Player)sender).getName(), args[1]);
				
		Utils.SendMessage(sender, core.config.playerRej, reject);
		
		return true;
	}
}