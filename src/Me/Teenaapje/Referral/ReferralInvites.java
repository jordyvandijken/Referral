package Me.Teenaapje.Referral;

import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.entity.Player;

import Me.Teenaapje.Referral.Utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;


public class ReferralInvites {
	// list of invites
	ArrayList<Refer> referInvites;
	// the core
	ReferralCore core = ReferralCore.core;
	
	// init ReferralInvites
	public ReferralInvites () {
		referInvites = new ArrayList<Refer>();
	}
	
	// add and send invite
	public boolean AddToList(String to, String from) {
		Player fromPlayer = core.GetPlayer(from);
		
		// check if already exists
		if (IsInList(to, from)) {
			Utils.SendMessage(fromPlayer, core.config.alreadySendRef);
			return false;
		}
		
		// check if it is the same player
		Player toPlayer = core.GetPlayer(to);
		if (fromPlayer == toPlayer) {
			//refp.sendMessage(Utils.chatConsole(main.util.referSelf));
			Utils.SendMessage(fromPlayer, core.config.alreadySendRef);
			return false;
		}
		
		// add to list
		referInvites.add(new Refer(to, from));
		
		// get the buttons
		TextComponent accept  = Utils.CreateTextComponent(core.config.accept, ChatColor.GREEN, true, ClickEvent.Action.RUN_COMMAND, "/ref accept " + from);
		TextComponent decline = Utils.CreateTextComponent(core.config.decline,  ChatColor.RED, true, ClickEvent.Action.RUN_COMMAND, "/ref reject " + from);
		
		// send invite
		Utils.SendMessage(toPlayer, core.config.youGotRefer, fromPlayer);
		toPlayer.spigot().sendMessage(accept, decline);
		
		// notify that it has been sned
		Utils.SendMessage(fromPlayer, core.config.youSendRequest, toPlayer);		
		
		return true;
	}
	
	// remove from list
	public boolean RemoveFromList (String ref, String refer) {
		Iterator<Refer> itr = referInvites.iterator();  
        while(itr.hasNext()){  
        	Refer st=(Refer)itr.next();     
            if (st.ref.contains(ref) && st.refer.contains(refer)) {
            	itr.remove();
            	return true;
			}
        }   
        
        return false;
	}
	
	// is in list
	public boolean IsInList (String ref,String refer) {
	    Iterator<Refer> itr = referInvites.iterator();  
        while(itr.hasNext()){  
        	Refer st=(Refer)itr.next();              
            if (st.ref.toLowerCase().compareTo(ref.toLowerCase()) == 0 && st.refer.toLowerCase().compareTo(refer.toLowerCase()) == 0) {
				return true;
			}
        }     
        return false;
	}
}


class Refer {
	String ref;
	String refer;
	
	Refer (String _ref, String _refer) {
		this.ref = _ref;
		this.refer = _refer;
	}
}
