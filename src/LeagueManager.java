import java.io.IOException;

import League.League;
import League.Player;
import League.Players;

public class LeagueManager {

  public static void main(String[] args) throws IOException {
    Player[] players = Players.load();
    System.out.printf("There are currently %d registered players.%n%n", players.length);

    League league = new League(players);

    league.run();
  }

}

