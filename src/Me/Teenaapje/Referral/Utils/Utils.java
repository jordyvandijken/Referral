package Me.Teenaapje.Referral.Utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import me.clip.placeholderapi.PlaceholderAPI;
import Me.Teenaapje.Referral.ReferralCore;

public class Utils {	
	// send message to player
	public static boolean SendMessage(CommandSender sendTo, String text) {
		return SendMessage(sendTo, text, null);
	}
	public static boolean SendMessage(CommandSender sendTo, String text, Player playerPlaceholder) {
		try {
			// get placeholder of other player
			if (playerPlaceholder != null) {
				text = SetPlaceHolders(playerPlaceholder, text);
			}
			
			// colorcode and send
			sendTo.sendMessage(ColorCode(text));
			
			return true;
		} catch (Exception e) {
			// TODO: handle exception?
			return false;
		}
	}
	
	public static boolean SendMessage(Player player, String text) {
		return SendMessage(player, text, null);
	}
	public static boolean SendMessage(Player player, String text, Player playerPlaceholder) {
		try {
			// get placeholder of other player
			if (playerPlaceholder != null) {
				text = SetPlaceHolders(playerPlaceholder, text);
			} else {
				text = SetPlaceHolders(player, text);			
			}
			
			// colorcode and send
			player.sendMessage(ColorCode(text));
			
			return true;
		} catch (Exception e) {
			// TODO: handle exception?
			return false;
		}
	}
	
	public static String SetPlaceHolders(Player player, String text) {
		if (ConfigManager.placeholderAPIEnabled) {
			return PlaceholderAPI.setPlaceholders(player, text);
		}
		
		return text.replace("%referral_total%", Integer.toString(ReferralCore.core.db.GetReferrals(player.getUniqueId().toString(), player.getName())))
				   .replace("%referral_refed%", String.valueOf(ReferralCore.core.db.PlayerReferrald(player.getUniqueId().toString(), player.getName())));
	}
	
	/// Color code text
	public static String ColorCode(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}
	
	// create text component
	public static TextComponent CreateTextComponent(String text, ChatColor color, boolean bold, ClickEvent.Action action, String runCommand) {
		TextComponent component = new TextComponent(text);
		component.setColor(color);
		component.setBold(bold);
		component.setClickEvent(new ClickEvent(action, runCommand));
		return component;
	}
	
	// check if its the same player
	public static boolean IsPlayerSelf(Player player, String name) {
		return player.getName().toLowerCase().compareTo(name.toLowerCase()) == 0;
	}
	public static boolean IsPlayerSelf(Player a, Player b) {
		return a == b;
	}
	
	public static boolean IsConsole(CommandSender sender) {
		return !(sender instanceof Player);
	}
	
	public static void Console(String text) {
		System.out.print(ChatColor.translateAlternateColorCodes('&', text));
	}
	
	
	
}
