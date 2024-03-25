package Me.Teenaapje.Referral.Commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import Me.Teenaapje.Referral.ReferralCore;

public class CommandManager implements CommandExecutor, TabExecutor {
	ReferralCore core = ReferralCore.core;
	
	String defaultcommand = "ref";
	
	private RefPlayer mainCommand;
	private List<CommandBase> commands;
	
	public CommandManager() {
		core.getCommand(defaultcommand).setExecutor(this);
		
		commands = new ArrayList<CommandBase>();
		
		// initialize commands
		// the main command
		mainCommand = new RefPlayer();
		// all other commands
		commands.add(new RefCount());
		commands.add(new RefTop());
		commands.add(new RefAdmin());
		commands.add(new RefAccept());
		commands.add(new RefReject());
		commands.add(new RefHelp());
		commands.add(new RefReload());
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (args.length >= 1) {
					// go through all command
					for (Iterator<CommandBase> iterator = commands.iterator(); iterator.hasNext();) {
						CommandBase commandBase = (CommandBase) iterator.next();
						
						if (commandBase.command.compareToIgnoreCase(args[0]) == 0 && commandBase.CanUse(sender, true)) {
							commandBase.onCommand(sender, cmd, label, args);
							return;
						}
					}
				}
				if (mainCommand.CanUse(sender, true)) {
					mainCommand.onCommand(sender, cmd, label, args);
				}
			}
		}.runTaskAsynchronously(core);
		
		
		return true;
	}
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// make a list
    	ArrayList<String> availableCommands = new ArrayList<String>();
    	
    	// go through commands
    	for (Iterator<CommandBase> iterator = commands.iterator(); iterator.hasNext();) {
			CommandBase commandBase = (CommandBase) iterator.next();
			
			if (commandBase.CanUse(sender, false)) {
				// if args fits command
				if (args.length <= 1 && commandBase.command.toLowerCase().contains(args[0].toLowerCase())) {
					availableCommands.add(commandBase.command);
				// if args[0] == command and args [1] fits subcommand
				} else if (args.length == 2 && commandBase.command.compareToIgnoreCase(args[0]) == 0) {
					for (int i = 0; i < commandBase.subCommands.length; i++) {
						if (commandBase.subCommands[i].toLowerCase().contains(args[1].toLowerCase())) {
							availableCommands.add(commandBase.subCommands[i]);
						}
					}
				}
			}
		}
		
    	// return player names if there are no options
    	if (availableCommands.isEmpty() || args.length < 1) {
    		for (Player p : core.getServer().getOnlinePlayers()) {
        		if (p.getName().toString().toLowerCase().contains(args[args.length - 1].toLowerCase())) {
        			availableCommands.add(p.getName());
        		}
			}
		}
    	
    	return availableCommands;


        //return null;
    }
}
