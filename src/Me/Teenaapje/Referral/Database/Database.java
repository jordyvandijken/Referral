package Me.Teenaapje.Referral.Database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import Me.Teenaapje.Referral.ReferralCore;
import Me.Teenaapje.Referral.Utils.TopPlayer;


public class Database {

	public String host, database, username, password, table, parameters, dboption;
	public int port;
	private Connection connection;
	
	ReferralCore core = ReferralCore.core;
	
	public Database() {
		host = core.getConfig().getString("host");
		port = core.getConfig().getInt("port");
		database = core.getConfig().getString("database");
		username = core.getConfig().getString("username");
		password = core.getConfig().getString("password");
		table = core.getConfig().getString("table");
		parameters = core.getConfig().getString("databaseParameters");
		dboption = core.getConfig().getString("db");
		
		if (dboption.compareTo("local") == 0) {
			sqlLiteSetup();
		} else if (dboption.compareTo("mysql") == 0) {
			mysqlSetup();
		} else {
			core.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + core.getDescription().getName() + " Incorrect selected database!");
			return;
		}
		
		createTable();
	}
	
	public void sqlLiteSetup() {
	  	File dir = core.getDataFolder();
		
		// look if directory exists and create
		if (!dir.exists()) {
			if (!dir.mkdir()) {
				System.out.println("Could not create directory for plugin: " + core.getDescription().getName());
			}
		}
		
		// check and create file if it doesn't exist
		File file = new File(dir, database + ".db");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
        try {
            if(connection!=null&&!connection.isClosed()){
                return;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + file);
			
            
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Connected to " + core.getConfig().getString("db") + " database");

            return;
        } catch (SQLException ex) {
            core.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            core.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        } 
	}
	
	public void mysqlSetup() {
		try {
			synchronized (this) {
				if (getConnection() != null && !getConnection().isClosed()) {
					return;
				}
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
		        } catch (Exception ex) {
					Class.forName("com.mysql.jdbc.driver");
		        }

				setConnection(DriverManager.getConnection("jdbc:mysql://" + 
															this.host + ":" + 
															this.port + "/" + 
															this.database +
															this.parameters, 
															this.username, 
															this.password));
				Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Database Connected to " + core.getConfig().getString("db") + " for: " + core.getDescription().getName());
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	private void createTable() {
		String CreateTable = "CREATE TABLE IF NOT EXISTS " + table + "(" + 
				"  `UUID` varchar(40) NOT NULL," +
				"  `NAME` varchar(40) NOT NULL," +
				"  `REFERRED` varchar(40) DEFAULT NULL," +
				"  `LASTREWARD` int(255) NOT NULL DEFAULT 0," +
				"  `USERIP`	varchar(255) DEFAULT NULL)";
	    
	    try {
            Statement s = connection.createStatement();
            s.executeUpdate(CreateTable);
            s.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
	    
	}
	
	public boolean PlayerExists (String uuid) {
		try {
			PreparedStatement statement = getConnection().prepareStatement("select * from " + table + " where UUID=?");
			
			statement.setString(1, uuid);
			
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				return true;
			}
			
			statement.close();
			
		} catch (SQLException e) {
			System.out.print("Error Function PlayerExists");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public void CreatePlayer(String playerUUID, String playerName) {
		try {
			if (PlayerExists(playerUUID)) {
				return;
			}
			PreparedStatement insert = getConnection().prepareStatement("insert into " + table + " (UUID, NAME, REFERRED) values (?,?,?)");

			insert.setString(1, playerUUID);
			insert.setString(2, playerName);
			insert.setString(3, null);

			insert.executeUpdate();
			
			insert.close();

		} catch (SQLException e) {
			System.out.print("Error Function CreatePlayer");
			e.printStackTrace();
		}
	}

	public void ReferralPlayer(Player ref, Player refed) {
		try {
			// check if the both players are added to the database
			CreatePlayer(ref.getUniqueId().toString(), ref.getName());
			CreatePlayer(refed.getUniqueId().toString(), refed.getName());
			
			PreparedStatement update = getConnection()
					.prepareStatement("update " + table + " set REFERRED=?, USERIP=? where UUID=?");
			
			update.setString(1, refed.getUniqueId().toString());
			update.setString(2, ref.getAddress().getHostName());
			update.setString(3, ref.getUniqueId().toString());
			update.executeUpdate();
			
			update.close();

		} catch (SQLException e) {
			System.out.print("Error Function ReferralPlayer");
			e.printStackTrace();
		}
	}
	
	public boolean PlayerReferrald (String playerUUID, String playerName) {
		try {
			if (!PlayerExists(playerUUID)) {
				CreatePlayer(playerUUID, playerName);
			}	
			
			PreparedStatement statement = getConnection().prepareStatement("select * from " + table + " where UUID=?");
			
			statement.setString(1, playerUUID);
			
			ResultSet result = statement.executeQuery();
						
			if (result.next() && result.getString("REFERRED") == null) {
				return false;
			}
			
			statement.close();
			
		} catch (SQLException e) {
			System.out.print("Error Function playerReferrald");
			e.printStackTrace();
		}
		
		return true;
	}
	
	public String PlayerReferraldBy (String playerUUID) {
		try {
			if (!PlayerExists(playerUUID)) {
				return null;
			}	
			
			PreparedStatement statement = getConnection().prepareStatement("select REFERRED from " + table + " where UUID=?");
			
			statement.setString(1, playerUUID);
			
			ResultSet result = statement.executeQuery();
						
			if (result.next() && result.getString("REFERRED") != null) {
				return result.getString("REFERRED");
			}
			
			statement.close();
			
		} catch (SQLException e) {
			System.out.print("Error Function playerReferrald");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean PlayerReset(String player) {
		try {
			if (!PlayerExists(player)) {
				return true;
			}	
			
			PreparedStatement statement = getConnection().prepareStatement("update " + table + " set REFERRED=null, LASTREWARD=0, USERIP=null where UUID=?");
			
			statement.setString(1, player);
			
			statement.executeUpdate();
			
			statement.close();
			
			return true;
		} catch (SQLException e) {
			System.out.print("Error Function PlayerReset");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean PlayerRemove(String player) {
		try {
			if (!PlayerExists(player)) {
				return true;
			}	
			
			PreparedStatement statement = getConnection().prepareStatement("delete from " + table + " where UUID=?");
			
			statement.setString(1, player);
			
			statement.executeUpdate();
			
			statement.close();
			
			return true;
		} catch (SQLException e) {
			System.out.print("Error Function PlayerRemove");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public int GetReferrals (String playerUUID, String playerName) {
		try {
			if (!PlayerExists(playerUUID)) {
				return 0;
			}	
			
			PreparedStatement statement = getConnection().prepareStatement("SELECT count(*) as total from user WHERE REFERRED=?");
			
			statement.setString(1, playerUUID);
			
			ResultSet result = statement.executeQuery();
						
			if (result.next()) {
				return result.getInt("total");
			}
			
			statement.close();
			
		} catch (SQLException e) {
			System.out.print("Error Function GetReferrals");
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public List<TopPlayer> GetTopPlayers (int min, int max) {
		List<TopPlayer> topPlayer = new ArrayList<TopPlayer>();
		try {
			PreparedStatement statement;
			
			if (dboption.compareTo("mysql") == 0) {
				statement = getConnection().prepareStatement("SELECT U.UUID, U.NAME, (SELECT count(*)from " + table + " US WHERE US.REFERRED=U.UUID) as REFTOTAL "
						+ " FROM " + table + " U ORDER BY REFTOTAL DESC, NAME ASC LIMIT ?, ?");
			} else {
				statement = getConnection().prepareStatement("SELECT U.UUID, U.NAME, (SELECT count(*)from [" + table + "] US WHERE US.REFERRED=U.UUID) as REFTOTAL "
						+ " FROM " + table + " U ORDER BY REFTOTAL DESC, NAME ASC LIMIT ?, ?");
			}
				
			statement.setInt(1, min);
			statement.setInt(2, max);
			
			
			ResultSet result = statement.executeQuery();
			
			int position = min + 1;
			while (result.next()) {
				// create and add top player
				topPlayer.add(new TopPlayer(result.getString("UUID"), result.getString("NAME"), position, result.getInt("REFTOTAL")));
				position++;
			}
			
			statement.close();
			
			return topPlayer;
			
		} catch (SQLException e) {
			System.out.print("Error Function GetReferrals");
			e.printStackTrace();
		}
		
		return topPlayer;
	}

	public int GetLastReward (String playerUUID, String playerName) {
		try {
			if (!PlayerExists(playerUUID)) {
				return 0;
			}	
			
			PreparedStatement statement = getConnection().prepareStatement("SELECT LASTREWARD as amount from user WHERE REFERRED=?");
			
			statement.setString(1, playerUUID);
			
			ResultSet result = statement.executeQuery();
						
			if (result.next()) {
				return result.getInt("amount");
			}
			
			statement.close();
			
		} catch (SQLException e) {
			System.out.print("Error Function GetLastReward");
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int GetUsedRefIP (String playerUUID, String ip) {
		try {
			if (!PlayerExists(playerUUID)) {
				return 0;
			}	
			
			PreparedStatement statement = getConnection().prepareStatement("SELECT count(*) as total from user WHERE USERIP=? and not UUID=?");
			
			statement.setString(1, ip);
			statement.setString(2, playerUUID);
			
			ResultSet result = statement.executeQuery();
						
			if (result.next()) {
				return result.getInt("total");
			}
			
			statement.close();
			
		} catch (SQLException e) {
			System.out.print("Error Function GetUsedRefIP");
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public boolean ResetAll() {
		try {
			PreparedStatement statement = getConnection().prepareStatement("update " + table + " set REFERRED=null, LASTREWARD=0, USERIP=null");
						
			statement.executeUpdate();
			
			statement.close();
			
			return true;
		} catch (SQLException e) {
			System.out.print("Error Function ResetAll");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean RemoveAll() {
		try {
			PreparedStatement statement = getConnection().prepareStatement("delete from " + table);
						
			statement.executeUpdate();
			
			statement.close();
			
			return true;
		} catch (SQLException e) {
			System.out.print("Error Function RemoveAll");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public void LastRewardUpdate(Player player, int lastReward) {
		try {
			// check if the both players are added to the database
			CreatePlayer(player.getUniqueId().toString(), player.getName());
			
			PreparedStatement update = getConnection()
					.prepareStatement("update " + table + " set LASTREWARD=? where UUID=?");
			
			update.setInt(1, lastReward);
			update.setString(2, player.getUniqueId().toString());
			update.executeUpdate();
			
			update.close();

		} catch (SQLException e) {
			System.out.print("Error Function ReferralPlayer");
			e.printStackTrace();
		}
	}
	
	public int GetPlayerPosition(String name) {
		try {		
			PreparedStatement statement = getConnection()
					.prepareStatement("SELECT (SELECT COUNT(*) FROM " + table + " US WHERE US.NAME <= U.NAME) AS position FROM " + table + " U WHERE U.NAME=?");
			
			statement.setString(1, name);

			ResultSet result = statement.executeQuery();
			
			if (result.next()) {
				return result.getInt("position");
			}
			
			statement.close();

		} catch (SQLException e) {
			System.out.print("Error Function ReferralPlayer");
			e.printStackTrace();
		}
		
		return 999999;
	}
	
	public void CloseConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
