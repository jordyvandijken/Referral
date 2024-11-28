package Me.Teenaapje.Referral.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Me.Teenaapje.Referral.Utils.ConfigManager;
import Me.Teenaapje.Referral.Utils.Utils;

public class RefAccept extends CommandBase {
	public RefAccept() {
		 permission = "RefAccept";
		 command = "Accept";
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
		
		Player player = (Player)sender;
		Player target = core.GetPlayer(args[1]);
				
		// is it the same
		if (Utils.IsPlayerSelf(player, target)) {
			Utils.SendMessage(player, core.config.acceptSelf);
			return false;
		}
		
		// check if in list
		if (!core.rInvites.IsInList(player.getName(), args[1])) {
			Utils.SendMessage(player, core.config.didntRef, target);
			return false;
		}
		
		// check if online
		if (!target.isOnline()) {
			Utils.SendMessage(player, core.config.notOnline, target);
	        return false;
		}	
		
		// Check if player already referred a player 
		if (core.db.PlayerReferrald(target.getUniqueId().toString(), target.getName())) {
			Utils.SendMessage(player, core.config.alreadyRefed);
	        return false;
		}
		
		try { 
	    	core.db.ReferralPlayer(target, player);
	    	
	    	// send msg to the one that send request
			Utils.SendMessage(player, core.config.playerRef, target);
			
			// send the accept msg
			Utils.SendMessage(target, core.config.playerAcceptedRef, player);

			// give rewards
		    core.UseCommands(ConfigManager.playerRefers, player);
		    core.UseCommands(ConfigManager.playerReferd, target);

		    // check if the server wants to user milestone rewards
			if (ConfigManager.useMileStoneRewards) {
				// get the players info
				String playerUUID = player.getUniqueId().toString();
				String playerName = player.getName();

				int playerLastReward = core.db.GetLastReward(playerUUID, playerName);
				int playerReferrals = core.db.GetReferrals(playerUUID, playerName);

				// check if he has a new milestone reward
				if (core.milestone.HasAReward(playerLastReward, playerReferrals)) {
					core.UseCommands(core.milestone.GetRewards(playerReferrals), player);
				}
			}
			core.rInvites.RemoveFromList(player.getName(), args[1]);
		} catch (Exception e) {
			e.fillInStackTrace();
		}
		
		return true;
	}
}