import java.io.IOException;

import league.League;
import league.Player;
import league.Players;

public class LeagueManager {

  public static void main(String[] args) throws IOException {
    Player[] players = Players.load();
    System.out.printf("There are currently %d registered players.%n%n", players.length);

    League league = new League(players);

    league.run();
  }

}

