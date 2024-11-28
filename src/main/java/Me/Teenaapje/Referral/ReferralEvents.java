package Me.Teenaapje.Referral;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import Me.Teenaapje.Referral.Utils.ConfigManager;
import Me.Teenaapje.Referral.Utils.Utils;

public class ReferralEvents  implements Listener{
	ReferralCore core = ReferralCore.core;
	
	public ReferralEvents() {
		Bukkit.getPluginManager().registerEvents(this, core);
	}
	
	@EventHandler
	public void Onjoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		
		
		if (!player.hasPlayedBefore() && ConfigManager.enableNotification) {
			Utils.SendMessage(player, core.config.referNotification);
		}
	}
	
}