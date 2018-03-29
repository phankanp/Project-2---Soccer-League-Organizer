package league;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Team implements Comparable<Team> {
	public static final int MAX_PLAYERS = 11;
	private String mTeamName;
	private String mCoachName;
	private Set<Player> mPlayers;
	
	public Team(String teamName, String coachName) {
		mTeamName = teamName;
		mCoachName = coachName;
		mPlayers = new TreeSet<>();
	}

	public String getmTeamName() {
		return mTeamName;
	}

	public String getmCoachName() {
		return mCoachName;
	}
	
	public void addPlayer(Player player) {
		mPlayers.add(player);
	}
	
	public void removePlayer(Player player) {
		mPlayers.remove(player);
	}
	
	/**
	 * @return Returns a list of players on a specific team
	 */
	public List<Player> playersOnTeam() {
		List<Player> mPlayersOnTeams = new ArrayList<>();
		mPlayersOnTeams.addAll(mPlayers);
		
		return mPlayersOnTeams;
	}

	@Override
	public int compareTo(Team other) {
		
		// TODO Auto-generated method stub
		return this.getmTeamName().compareTo(other.getmTeamName());
	}
	
	/**
	 * @return Creates a map of player height to players with the same height
	 */
	public Map<Integer, List<Player>> heightForPlayers() {
		Map<Integer, List<Player>> heightForPlayers = new TreeMap<>();
		for (Player player : mPlayers) {
			List<Player> playersAtHeights = heightForPlayers.get(player.getHeightInInches());
			if(playersAtHeights == null) {
				playersAtHeights = new ArrayList<>();
				heightForPlayers.put(player.getHeightInInches(), playersAtHeights);
			}
			playersAtHeights.add(player);
		}
		return heightForPlayers;
	}
	
	/**
	 * @return Creates a map of experiences/inexperienced players and the number of players in each group
	 */
	public Map<String, Integer> previousExperience() {
		Map<String, Integer> previousExperience = new TreeMap<>();
		
		int hasExperienceCount = 0;
		int noExpereinceCount = 0;
		
		for (Player player : playersOnTeam()) {
			if (player.isPreviousExperience()) {
				hasExperienceCount++;
			} else {
				noExpereinceCount++;
			}
		}
		
		previousExperience.put("Experienced", hasExperienceCount);
		previousExperience.put("Inexperienced", noExpereinceCount);

		return previousExperience;

	}
	
	/**
	 * @return Returns the average for the number of experience players on a team
	 */
	public int avgTeamExperience() {
		double hasExperience = 0;
		double noExperience = 0;
		int zero = 0;
		
		if(!playersOnTeam().isEmpty()) {
		for (Player player : playersOnTeam()) {
			if(player.isPreviousExperience()) {
				hasExperience++;
			}
			else {
				noExperience++;
			}
		}
		
		double total = hasExperience + noExperience;
		double average = (hasExperience / total) * 100;
		
		return (int) Math.round(average);
		}
		else {
			return zero;
		}
	}
	
	/**
	 * @return Returns the average for the number of inexperience players on a team
	 */
	public int noTeamExperience() {
		double hasExperience = 0;
		double noExperience = 0;
		int zero = 0;
		
		if(!playersOnTeam().isEmpty()) {
		for (Player player : playersOnTeam()) {
			if(player.isPreviousExperience()) {
				hasExperience++;
			}
			else {
				noExperience++;
			}
		}
		
		double total = hasExperience + noExperience;
		double average = (noExperience / total) * 100;
		
		return (int) Math.round(average);
		}
		else {
			return zero;
		}
	}
	
	


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}