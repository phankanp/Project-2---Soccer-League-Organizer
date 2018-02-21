package League;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class League {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	private Player[] mPlayers;
	private Team mTeams;
	private Set<Player> allAvailablePlayers;
	private Set<Team> availableTeams;
	private Set<Player> waitingList;
	private final int MAX_TEAMS = 3;
	private final int MAX_PLAYERS = 11;

	private Map<String, String> mMenu;

	public League(Player[] players) {
		mPlayers = players;
		allAvailablePlayers = new TreeSet<>();
		availableTeams = new TreeSet<>();
		waitingList = new LinkedHashSet<>();

		getAllAvailablePlayers();

		mMenu = new LinkedHashMap<String, String>();
		mMenu.put("1", "Add a new team to the league.");
		mMenu.put("2", "Add a player to a team.");
		mMenu.put("3", "Remove a player from a team");
		mMenu.put("4", "View the height report for a team");
		mMenu.put("5", "View the league blance report");
		mMenu.put("6", "Print roster of all players on a team");
		mMenu.put("7", "Automatically build teams");
		mMenu.put("8", "Add a player to the wait list");
		mMenu.put("9", "Replace player in league with player from wait list");
		mMenu.put("10", "Exit program");

	}

	// Iterates through menu map, prints key/value options, and reads in user choice
	private String promptAction() throws IOException {
		for (Map.Entry<String, String> option : mMenu.entrySet()) {
			System.out.printf("%s - %s %n", option.getKey(), option.getValue());
		}
		System.out.printf("%nWhat is your choice:%n");
		String choice = br.readLine();
		return choice.trim().toLowerCase();
	}

	// Prints a header for player selection
	private void printHeader() {
		String header1 = "Index No.", header2 = "First Name", header3 = "Last Name", header4 = "Height in Inches",
				header5 = "Has Experience(T/F)";
		System.out.format("%n%-11s %-17s %-12s %-21s %16s %n", header1, header2, header3, header4, header5);
	}

	// Checks user input and runs appropriate function
	public void run() {
		String choice = " ";

		do {
			try {
				choice = promptAction();
				switch (choice) {
				case "1":
					createTeam();
					System.out.println();
					break;
				case "2":
					addPlayersToTeam();
					System.out.println();
					break;
				case "3":
					removePlayersFromTeam();
					System.out.println();
					break;
				case "4":
					viewHeightReport();
					System.out.println();
					break;
				case "5":
					leagueBalanceReport();
					System.out.println();
					break;
				case "6":
					printRoster();
					System.out.println();
					break;
				case "7":
					autoBuildTeams();
					System.out.println();
					break;
				case "8":
					addPlayerToWaitList();
					System.out.println();
					break;
				case "9":
					replacePlayer();
					System.out.println();
					break;
				case "10":
					System.out.println("Exiting program");
					break;
				default:
					System.out.println("Problem with input, try again");
					System.out.println();
					break;
				}
			} catch (IOException ioe) {
				System.out.println("Problem with input");
				ioe.printStackTrace();
			}
		} while (!choice.equals("10"));
	}

	// Prompts user for team name and coach name. Return new Team object.
	private Team promptTeam() throws IOException {
		System.out.print("Enter the team name: ");
		String teamName = br.readLine();
		System.out.print("Enter the coach name: ");
		String coachName = br.readLine();

		return new Team(teamName, coachName);
	}

	// Checks if max team limit has been reached and calls promptTeam() and adds
	// team to availableTeams set.
	private void createTeam() throws IOException {
		if (availableTeams.size() == MAX_TEAMS) {
			System.out.printf("Can not add another team. Reached max team limit.%n%n");
			return;
		} else {
			Team addTeam = promptTeam();
			availableTeams.add(addTeam);
		}
	}

	// Iterates through the given array of players and adds each player to
	// allAvailablePlayers set.
	private void getAllAvailablePlayers() {
		for (Player player : mPlayers) {
			allAvailablePlayers.add(player);
		}
	}

	// Iterates through all available players in the league and adds players to a
	// list of strings. Uses helper function promptForIndex
	// to return an indexed list.
	public Player availableFreePlayers() throws IOException {
		List<Player> mAllAvailablePlayers = new ArrayList<>(allAvailablePlayers);
		List<String> mPlayers = new ArrayList<>();
		for (Player player : mAllAvailablePlayers) {
			mPlayers.add(String.format("%16s %16s %14d %20s %n", player.getFirstName(), player.getLastName(),
					player.getHeightInInches(), player.isPreviousExperience()));
		}

		int index = promptForIndex(mPlayers);
		return mAllAvailablePlayers.get(index);
	}

	// Iterates through all available teams in the league and adds teams to a list
	// of strings. Uses helper function promptForIndex
	// to return an indexed list.
	private Team availableTeams() throws IOException {
		List<Team> mAvailableTeams = new ArrayList<Team>(availableTeams);
		List<String> mTeams = new ArrayList<>();
		for (Team team : mAvailableTeams) {
			mTeams.add(team.getmTeamName());
		}

		System.out.printf("Available Teams to choose:%n%n");
		int index = promptForIndex(mTeams);
		return mAvailableTeams.get(index);
	}

	// Adds user choice of player to user choice of team, after checking if teams
	// and player space on teams are available
	private void addPlayersToTeam() throws IOException {

		if (availableTeams.size() == 0) {
			System.out.printf("There are no teams in the league. Please create a team. %n");
			return;
		}

		System.out.printf("Select a team to add the player too:%n%n");
		Team teamChoice = availableTeams();

		if (teamChoice.playersOnTeam().size() == MAX_PLAYERS) {
			System.out.printf(
					"The selected team has reached the max player limit of players. Please select another team or create another team. %n");
			return;
		} else {
			System.out.printf("Select a player to add:%n%n");
			printHeader();
			Player playerChoice = availableFreePlayers();

			teamChoice.addPlayer(playerChoice);

			System.out.printf("Added %s %s to %s %n%n", playerChoice.getFirstName(), playerChoice.getLastName(),
					teamChoice.getmTeamName());

			allAvailablePlayers.remove(playerChoice);
		}

	}

	// Iterates through players on a team and adds to a list of strings. Uses helper
	// function promptForIndex
	// to return an indexed list.
	private Player playersOnTeam(List<Player> players) throws IOException {
		List<Player> mPlayersOnTeam = new ArrayList<>(players);
		List<String> mPlayers = new ArrayList<>();

		for (Player player : mPlayersOnTeam) {
			mPlayers.add(String.format("%16s %16s %14d %20s %n", player.getFirstName(), player.getLastName(),
					player.getHeightInInches(), player.isPreviousExperience()));
		}

		int index = promptForIndex(mPlayers);
		return mPlayersOnTeam.get(index);
	}

	// Removes user choice of player from user choice of team, after checking if
	// there are teams in the league and player on teams
	private void removePlayersFromTeam() throws IOException {
		if (availableTeams.isEmpty()) {
			System.out.printf("There are no teams in the league.%n%n");
			return;
		}

		System.out.printf("Select a team to remove a player from:%n");
		Team teamChoice = availableTeams();

		if (teamChoice.playersOnTeam().isEmpty()) {
			System.out.printf("There are players on the selected team.%n%n");
			return;
		}

		System.out.printf("Select a player to remove:%n");
		printHeader();
		Player playerChoice = playersOnTeam(teamChoice.playersOnTeam());

		teamChoice.removePlayer(playerChoice);
		allAvailablePlayers.add(playerChoice);

		System.out.printf("Removed %s %s from %s %n%n", playerChoice.getFirstName(), playerChoice.getLastName(),
				teamChoice.getmTeamName());

	}

	// Prints player names and height on a team. Groups players by height.
	private void viewHeightReport() throws IOException {
		if (availableTeams.isEmpty()) {
			System.out.printf("There are no teams in the league.%n%n");
			return;
		}

		System.out.printf("Select a team to view height report:%n");
		Team teamChoice = availableTeams();

		Map<Integer, List<Player>> heightMap = teamChoice.heightForPlayers();

		int playerCount = 0;

		for (Map.Entry<Integer, List<Player>> players : heightMap.entrySet()) {
			System.out.printf("%d Inches %n", players.getKey());
			System.out.printf("---------%n");
			for (Player player : players.getValue()) {
				System.out.printf("%s %s %n", player.getFirstName(), player.getLastName());
				playerCount++;
			}
			System.out.printf("There are %s players that are %s inches%n%n", playerCount, players.getKey());
			playerCount = 0;
		}
	}

	// Prints balance report, containing team name, the number of
	// experienced/inexperience players, and percent of experienced players
	private void leagueBalanceReport() {
		if (availableTeams.isEmpty()) {
			System.out.printf("There are no teams in the league.%n%n");
			return;
		}

		List<Team> teams = new ArrayList<>(availableTeams);

		for (Team team : teams) {
			Map<String, Integer> previousExperience = team.previousExperience();
			System.out.printf("%nTeam name: %s%n", team.getmTeamName());
			for (Map.Entry<String, Integer> experience : previousExperience.entrySet()) {
				System.out.printf("%s %s", experience.getKey(), experience.getValue());
				System.out.printf("%n");
			}
			System.out.printf("%d percent of players on team have previous experience%n", team.avgTeamExperience());
		}

	}

	// Prints the first and last names of players on a specific team
	public void printRoster() throws IOException {
		if (availableTeams.isEmpty()) {
			System.out.printf("There are no teams in the league.%n%n");
			return;
		}

		System.out.printf("Select a team to print roster:%n");
		Team teamChoice = availableTeams();

		if (teamChoice.playersOnTeam().isEmpty()) {
			System.out.printf("There are players on the selected team.%n%n");
			return;
		}

		List<Player> playersOnRoster = teamChoice.playersOnTeam();
		List<String> printPlayers = new ArrayList<>();

		for (Player player : playersOnRoster) {
			printPlayers.add(String.format("%-10s %-12s", player.getFirstName(), player.getLastName()));
		}

		for (String players : printPlayers) {
			System.out.printf("%n" + players + "%n");
		}
	}

	// Helper function, which indexes a list and takes user input
	private int promptForIndex(List<String> options) throws IOException {
		int choice = 0;
		int returnChoice = 0;

		while (0 >= choice || choice > (options.size() + 1)) {
			int counter = 1;
			for (String option : options) {
				System.out.printf("%d.) %s %n", counter, option);
				counter++;
			}
			System.out.printf("%d.) Return to main menu%n", counter);
			System.out.print("Your numbered choice: ");
			String optionAsString = br.readLine();
			choice = Integer.parseInt(optionAsString.trim());

			
		}
		
		if (0 >= choice || choice > (options.size() + 1)) {
			System.out.printf("Plase select a choice from the list.%n%n");
		}

		if (choice > options.size()) {
			run();
		} else {
			returnChoice = choice - 1;
		}

		return returnChoice;

	}

	// Returns a set of all experienced players in the league
	public Set<Player> experiencedPlayers() {
		Set<Player> experiencedPlayers = new TreeSet<>();

		for (Player player : allAvailablePlayers) {
			if (player.isPreviousExperience()) {
				experiencedPlayers.add(player);
			}
		}

		return experiencedPlayers;
	}

	// Returns a set of all inexperienced players in the league
	private Set<Player> inExperiencedPlayers() {
		Set<Player> inExperiencedPlayers = new TreeSet<>();

		for (Player player : allAvailablePlayers) {
			if (!player.isPreviousExperience()) {
				inExperiencedPlayers.add(player);
			}
		}

		return inExperiencedPlayers;
	}

	// Automatically builds 3 teams and adds player from allAvailablePlayers to
	// teams, making teams as fair as possible based on previous experience
	private void autoBuildTeams() throws IOException {
		String teamName = "Team";
		String coachName = "Coach";

		Set<Player> experiencedPlayers = experiencedPlayers();
		Set<Player> inExperiencedPlayers = inExperiencedPlayers();

		if (allAvailablePlayers.isEmpty() || availableTeams.size() == MAX_TEAMS) {
			if (allAvailablePlayers.isEmpty()) {
				System.out.printf("No more available players too add. %n");
				return;
			} else if (availableTeams.size() == MAX_TEAMS) {
				System.out.printf("Can not create more teams. %n");
				return;
			}
		}

		for (int i = 1; i < 4; i++) {
			String team = teamName + " " + i;
			String coach = coachName + " " + i;
			Team mTeam = new Team(team, coach);
			availableTeams.add(mTeam);
		}

		for (Iterator<Team> teamIterator = availableTeams.iterator(); teamIterator.hasNext();) {
			for (Player player : experiencedPlayers) {
				if (!teamIterator.hasNext()) {
					teamIterator = availableTeams.iterator();
				}
				Team team = teamIterator.next();
				for (int playerCount = 0; playerCount < 11;) {
					team.addPlayer(player);
					playerCount++;
				}

			}

			for (Player player : inExperiencedPlayers) {
				if (!teamIterator.hasNext()) {
					teamIterator = availableTeams.iterator();
				}
				Team team = teamIterator.next();
				for (int playerCount = 0; playerCount < 11;) {
					team.addPlayer(player);
					playerCount++;
				}

			}
		}

	}

	// Takes user input for new player and adds player to the wait list
	private void addPlayerToWaitList() throws IOException {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter first name: ");
		String waitFirstName = scanner.nextLine();
		System.out.print("Enter last name: ");
		String waitLastName = scanner.nextLine();
		System.out.print("Enter height in inches: ");
		int waitHeight = scanner.nextInt();
		System.out.print("Enter previous expereince(True or False): ");
		Boolean waitExperience = scanner.nextBoolean();

		Player waitPlayer = new Player(waitFirstName, waitLastName, waitHeight, waitExperience);

		waitingList.add(waitPlayer);

	}

	// Iterates through players on the wait list and adds to a list of strings. Uses
	// helper function promptForIndex
	// to return an indexed list.
	private Player availableWaitListPlayers() throws IOException {
		List<Player> mAllAvailablePlayers = new ArrayList<>(waitingList);
		List<String> mPlayers = new ArrayList<>();
		for (Player player : mAllAvailablePlayers) {
			mPlayers.add(String.format("%16s %16s %14d %20s %n", player.getFirstName(), player.getLastName(),
					player.getHeightInInches(), player.isPreviousExperience()));
		}

		int index = promptForIndex(mPlayers);
		return mAllAvailablePlayers.get(index);
	}

	// Replaces player in the league with a player from the wait list.
	private void replacePlayer() throws IOException {

		if (waitingList.isEmpty()) {
			System.out.printf("There are no players currently in the waiting list, please add a player. %n%n");
			return;
		} else {

			System.out.println("Select a player from the wait list to add to league");
			printHeader();
			Player addPlayer = availableWaitListPlayers();

			System.out.println("Select a player to remove from the league");
			printHeader();
			Player removePlayer = availableFreePlayers();

			allAvailablePlayers.add(addPlayer);
			allAvailablePlayers.remove(removePlayer);

			System.out.printf("Removed %s %s and added %s %s to the league.%n", removePlayer.getFirstName(),
					removePlayer.getLastName(), addPlayer.getFirstName(), addPlayer.getLastName());
		}

	}

}
