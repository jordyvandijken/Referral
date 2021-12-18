package Me.Teenaapje.Referral;

import java.util.ArrayList;
import java.util.List;

public class ReferralMilestone {
	ReferralCore core = ReferralCore.core;
	
	public List<Reward> rewards;

	public ReferralMilestone() {
		rewards = new ArrayList<Reward>();
		LoadRewards();
	}
	
	public void LoadRewards() {
		rewards.clear();
		
		for (String key : core.config.rewards.getKeys(false)) {
			Reward reward = new Reward (
				core.config.config.getInt("rewards."+key+".min"),
				core.config.config.getStringList("rewards."+key+".commands")
			);
			rewards.add(reward);
		}
	}
	
	public boolean HasAReward(int lastReward, int referd) {
		for (Reward reward : rewards) {
			if (reward.min == referd && lastReward < referd) {
				return true;
			} else if (reward.min == referd && lastReward >= referd) {
				return false;
			}
		}		
		return false;
	}
	
	public List<String> GetRewards (int referTotal) {
		for (Reward reward : rewards) {
			if (reward.min == referTotal) {
				return reward.commands;
			}
		}
		// this should not be able to return null if so the HasAReward function isnt working correct
		return null;
	}
}

class Reward {
    public int min;
	public List<String> commands;
 
	public Reward (int min,	List<String> commands){
        this.min = min;
        this.commands = commands;
    }
}