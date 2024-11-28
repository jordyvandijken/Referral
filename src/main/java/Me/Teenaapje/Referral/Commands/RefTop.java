package Me.Teenaapje.Referral.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import Me.Teenaapje.Referral.Utils.TopPlayer;
import Me.Teenaapje.Referral.Utils.Utils;

public class RefTop extends CommandBase {
	// init class
	public RefTop() {
		 permission = "RefTop";
		 command = "top";
		 forPlayerOnly = false;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// get top players
		List<TopPlayer> topPlayers = core.db.GetTopPlayers(0,9);
		
		for (TopPlayer topPlayer : topPlayers) {
			Utils.SendMessage(sender, topPlayer.playerPos + " - " + topPlayer.playerName + " : " + topPlayer.totalRefers);
		}
		
		return true;
	}
}