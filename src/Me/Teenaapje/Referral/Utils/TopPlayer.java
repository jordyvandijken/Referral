package Me.Teenaapje.Referral.Utils;

public class TopPlayer {
	public String playerUUID;
	public String playerName;
	public int playerPos;
	public int totalRefers;
	
	public TopPlayer(String pUUID, String p, int pos, int t) {
		playerUUID = pUUID;
		playerName = p;
		playerPos = pos;
		totalRefers = t;
	}
}
