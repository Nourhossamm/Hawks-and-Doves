package hawk.dove.game;
import java.util.Stack;


public class Battle {
    private static int roomNumber = 1;
    public String BattleRoomName;
    public Stack<PlayerAgent> Players;
    private final int value;
    private final int cost;
    private boolean will_p1_change = false;
    private boolean will_p2_change = false;

    public Battle(int value, int cost, PlayerAgent p1) {
        this.BattleRoomName = "Battle room (" + Integer.toString(roomNumber++) + ")";
        this.Players = new Stack();
        this.value = value;
        this.cost = cost;
        this.Players.push(p1);
    }
    
    public boolean isBattleRoomFull(){
        return this.Players.size() == 2;
    }

    public boolean isWill_p1_change() {
        return will_p1_change;
    }

    public boolean isWill_p2_change() {
        return will_p2_change;
    }
    
    
    
    
    public void Battle(LearningMethod method)
    {
        PlayerAgent p1 = this.Players.pop();
        PlayerAgent p2 = this.Players.pop();
        float p1_payoff = p1.updatePayOff(p2.getStrategy(), this.value, this.cost);
        float p2_payoff = p2.updatePayOff(p1.getStrategy(), this.value, this.cost);
        this.will_p1_change = p1.changeStrategy(p1_payoff, method);
        this.will_p2_change = p2.changeStrategy(p2_payoff, method);
        this.Players.push(p1);
        this.Players.push(p2);
    }

    public int getValue() {
        return value;
    }

    public int getCost() {
        return cost;
    }
    
    
}
