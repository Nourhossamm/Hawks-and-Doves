package hawk.dove.game;
import java.io.PrintWriter;
import java.util.Stack;


public class Battle {
    private static int roomNumber = 1;
    public String BattleRoomName;
    public Stack<PlayerAgent> Players;
    private final int value;
    private final int cost;
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
    
    
    
    public void Battle(PrintWriter writer)
    {
        writer.write("\nA battle in " + this.BattleRoomName + " has started");
        writer.write("\nValue: " + Integer.toString(this.value));
        writer.write("\nCost: " + Integer.toString(this.cost));
        PlayerAgent p1 = this.Players.pop();
        PlayerAgent p2 = this.Players.pop();
        writer.write("\n" + p1.toString() + " VS " + p2.toString());
        boolean p1_result = p1.updatePayOff(p2.getStrategy(), this.value, this.cost);
        boolean p2_result =p2.updatePayOff(p1.getStrategy(), this.value, this.cost);
        writer.write("\n" + p1.getName() + " : " + "Before battle => " + Float.toString(p1.getPrevPayOff()) + " After battle => " + Float.toString(p1.getPayOff()));
        writer.write("\n" + p2.getName() + " : "+ "Before battle => " + Float.toString(p2.getPrevPayOff()) + " After battle => " + Float.toString(p2.getPayOff()));
        Strategy s1 = p1.getStrategy();
        Strategy s2 = p2.getStrategy();
        boolean willp1Change = p1.changeStrategy(s2);
        boolean willp2Change = p2.changeStrategy(s1);
        if(willp1Change)
            writer.write("\n" + p1.getName() + " will change his strategy to " + p1.getStrategy());
        if(willp2Change)
            writer.write("\n" + p2.getName() + " will change his strategy to " + p2.getStrategy());
        if(p1_result)
            writer.write("\n" + p1.toString() + " wins");
        else if(p2_result)
            writer.write("\n" + p2.toString() + " wins");
        else
            writer.write("\nDraw");
        writer.write("\n" + p1.getName() + " has left " + this.BattleRoomName);
        writer.write("\n" + p2.getName() + " has left " + this.BattleRoomName);
        writer.write("\n-----------------------------------------------------");
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
