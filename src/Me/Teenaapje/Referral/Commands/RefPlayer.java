package Me.Teenaapje.Referral.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Me.Teenaapje.Referral.Utils.ConfigManager;
import Me.Teenaapje.Referral.Utils.Utils;

public class RefPlayer extends CommandBase {
	// init class
	public RefPlayer() {
		 permission = "ReferPlayer";
		 command = "";
		 forPlayerOnly = true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player)sender;

		// check arguments
		if (args.length > 1) {
	        Utils.SendMessage(player, core.config.tooManyArgs);
	        return false;
	    } else if (args.length < 1) {
	        Utils.SendMessage(player, core.config.missingPlayer);

	        return false;
	    } 
		
		// Check if player already referred a player
		if (core.db.PlayerReferrald(player.getUniqueId().toString(), player.getName())) {
	        Utils.SendMessage(player, core.config.alreadyRefedSelf);
	        return false;
		}
		
		// check if the player is online
		Player target = core.GetPlayer(args[0]);
	    if (target == null) {
	        Utils.SendMessage(player, core.config.notOnline, target);
	        return false;
	    }
	    
		// check if the player wants to referral him self
	    if (Utils.IsPlayerSelf(player, target)) {
			Utils.SendMessage(player, core.config.referSelf);
			return false;
		}
	    
	    
	    // Check if player is referred
 		if (!ConfigManager.canReferEachOther && core.db.PlayerReferrald(target.getUniqueId().toString(), target.getName())) {
 			String refedUUID = core.db.PlayerReferraldBy(target.getUniqueId().toString());
 			// Check if the player try to refer each other
 			if (refedUUID != null && refedUUID.equalsIgnoreCase(player.getUniqueId().toString())) {			
 				Utils.SendMessage(player, core.config.refEachOther);
 	            return false;
 			}
 		}
 		
 		float playTime = (player.getLastPlayed() - player.getFirstPlayed()) / 60000;
 		
 		// Check if server uses time limit if so is player in time?
        if (ConfigManager.useReferralTimeLimit && playTime > ConfigManager.referralTimeLimit) {
			Utils.SendMessage(player, core.config.referTimeOut);
			return false;
		}
 		
        // Check if server uses time limit if so did the player play enough
        if (ConfigManager.useReferralMinPlay && playTime < ConfigManager.referralMinPlay) {
			Utils.SendMessage(player, core.config.referMinPlay);
			return false;
		}
 		
        // Check if server uses max same ip
        if (ConfigManager.useSameIPLimit) {
        	String hostName = player.getAddress().getHostName();
        	
	        // Check if server uses time limit if so is player in time?
	        if (ConfigManager.maxSameIP == 0) {
	        	// check if users have the same ip
	        	if (hostName.compareTo(target.getAddress().getHostName()) == 0) {
	        		// cant use the same network
	    			Utils.SendMessage(player, core.config.maxIP);
					return false;
		        }
			} else if (ConfigManager.maxSameIP <= core.db.GetUsedRefIP(player.getUniqueId().toString(), hostName)) {
				// cant use the same network
    			Utils.SendMessage(player, core.config.maxIP);
				return false;
			}
        }
 		
        // Need to use confirm?
	    if (ConfigManager.usePlayerConfirm) {
		    // Add player to list send a notification
		    core.rInvites.AddToList(target.getName(), player.getName());
		} else { // just five
		    try { 
		    	core.db.ReferralPlayer(player, target);
		    	
    			Utils.SendMessage(target, core.config.referring);

		    	// give the player their rewards
			    core.UseCommands(ConfigManager.playerRefers, target);
			    core.UseCommands(ConfigManager.playerReferd, player); 
			    
			    
			    if (ConfigManager.useMileStoneRewards) {
				    // get the targets info
				    String playerUUID = target.getUniqueId().toString();
				    String playerName = target.getName();
				    
				    int playerLastReward = core.db.GetLastReward(playerUUID, playerName);
				    int playerReferrals = core.db.GetReferrals(playerUUID, playerName);
				    
				    // check if he has a new milestone reward
				    if (core.milestone.HasAReward(playerLastReward, playerReferrals)) {
				    	core.UseCommands(core.milestone.GetRewards(playerReferrals), target);
					}
			    }
			} catch (Exception e) {
				e.fillInStackTrace();
			}  	
		}
 		
		return true;
	}
}
