package Me.Teenaapje.Referral.Utils;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import Me.Teenaapje.Referral.ReferralCore;

public class ConfigManager {
	ReferralCore core = ReferralCore.core;
	public FileConfiguration config;
	
	public ConfigurationSection rewards;

	public String 	noPerm, playerOnly, tooManyArgs, notOnline, missingPlayer, missingArgs,		// default
					allReset, allRemoved, refEachOther,
					playerTotal,  																// reftotal
					acceptSelf, alreadyRefed, didntRef, playerRef, 								// refaccept
					playerRemoved, playerRemovedFailed, playerReset, playerResetFailed,			// refAdmin
					rejectSomeone, playerRej, 													// refReject
					referSelf, referring, alreadyRefedSelf, referTimeOut, referMinPlay, maxIP,	// Refplayer
					alreadySendRef, youGotRefer, youSendRequest, accept, decline, playerGotRej,	// StoredPlayerData
					playerAcceptedRef, referNotification;
	
	public static boolean 	placeholderAPIEnabled, enableNotification, canReferEachOther, useReferralTimeLimit, useReferralMinPlay, 
							useSameIPLimit, usePlayerConfirm, useMileStoneRewards;
	
	public static int		referralTimeLimit, referralMinPlay, maxSameIP;
	
	public static List<?> 	playerRefers, playerReferd;
	
	public ConfigManager() {
		config = core.getConfig();
		
		// default
		noPerm 				= config.getString("noPerm");
		playerOnly 			= config.getString("playerOnly");
		tooManyArgs 		= config.getString("tooManyArgs");
		notOnline 			= config.getString("notOnline");
		missingPlayer 		= config.getString("missingPlayer");
		missingArgs 		= config.getString("missingaArgs");
		allReset 			= config.getString("resetAll");
		allRemoved 			= config.getString("removeAll");
		refEachOther 		= config.getString("referEachOther");

		// reftotal
		playerTotal 		= config.getString("playerTotal");
		
		// refaccept
		acceptSelf 			= config.getString("acceptSelf");
		alreadyRefed 		= config.getString("alreadyRefed");
		didntRef 			= config.getString("didntRef");
		playerRef 			= config.getString("playerRef");
		
		// refAdmin
		playerRemoved 		= config.getString("playerRemoved");
		playerRemovedFailed = config.getString("playerRemovedFailed");
		playerReset 		= config.getString("playerReset");
		playerResetFailed 	= config.getString("playerResetFailed");
		
		// refReject
		rejectSomeone 		= config.getString("rejectSomeone");
		playerRej 			= config.getString("playerRej");
		
		// Refplayer
		referSelf 			= config.getString("referSelf");
		referring 			= config.getString("referring");
		alreadyRefedSelf 	= config.getString("alreadyRefedSelf");
		referTimeOut 		= config.getString("referTooLate");
		referMinPlay 		= config.getString("referMinPlay");
		maxIP 				= config.getString("maxIP");	

		// ReferralInvites
		alreadySendRef 		= config.getString("alreadySendRef");
		youGotRefer 		= config.getString("youGotRefer");
		youSendRequest 		= config.getString("youSendRequest");
		accept 				= config.getString("accept");
		decline 			= config.getString("decline");
		playerGotRej 		= config.getString("playerGotRej");
		playerAcceptedRef 	= config.getString("playerAcceptedRef");
		referNotification 	= config.getString("referNotification");

		// papi
		placeholderAPIEnabled = core.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
		
		// booleans
		// notification on join
		enableNotification 		= config.getBoolean("enableNotification");
		canReferEachOther 		= config.getBoolean("canReferEachOther");
		useReferralTimeLimit 	= config.getBoolean("useReferralTimeLimit");
		useReferralMinPlay 		= config.getBoolean("useReferralMinPlay");
		useSameIPLimit 			= config.getBoolean("useSameIPLimit");
		usePlayerConfirm 		= config.getBoolean("usePlayerConfirm");
		useMileStoneRewards 	= config.getBoolean("useMileStoneRewards");
		
		// ints
		referralTimeLimit 	= config.getInt("referralTimeLimit");
		referralMinPlay		= config.getInt("referralMinPlay");
		maxSameIP			= config.getInt("maxSameIP");
		
		// get rewards
		rewards = config.getConfigurationSection("rewards");
		
		// list
		playerRefers = config.getList("playerRefers");
		playerReferd = config.getList("playerReferd");
	}
}
