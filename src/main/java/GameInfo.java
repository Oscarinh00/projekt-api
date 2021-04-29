import java.util.List;

public class GameInfo {
    private int players;
    private List cards;

    public GameInfo(int players, List cards){
        this.players = players;
        this.cards = cards;
    }

    public int getPlayers() {
        return players;
    }

    public List getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return "GameInfo{" +
                "players=" + players +
                ", cards=" + cards +
                '}';
    }
}
