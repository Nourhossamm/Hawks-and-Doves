package hawk.dove.game;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


public class BattleReport {
    private final PlayerAgent p1;
    private final PlayerAgent p2;
    private final Strategy p1_strategy;
    private final Strategy p2_strategy;
    private boolean will_p1_change = false;
    private boolean will_p2_change = false;
    private final float value;
    private final PlayerAgent winner;
    private final float cost;
    private final String battleName;
    public static PrintWriter writer;
    public static int stepCounter;
    public BattleReport(Battle battle){
        this.p1 = battle.Players.pop();
        this.p2 = battle.Players.pop();
        this.battleName = battle.BattleRoomName;
        this.p1_strategy = this.p1.getStrategy();
        this.p2_strategy = this.p2.getStrategy();
        this.value = battle.getValue();
        this.cost = battle.getCost();
        if(p1_strategy == Strategy.Hawk && p2_strategy == Strategy.Dove)
            this.winner = this.p1;
        else if(p2_strategy == Strategy.Hawk && p1_strategy == Strategy.Dove)
            this.winner = this.p2;
        else
            this.winner = null;
        this.will_p1_change = battle.isWill_p1_change();
        this.will_p2_change = battle.isWill_p2_change();
    }
    

        public PlayerAgent getWinner() {
        return winner;
    }

    public PlayerAgent getLoser() {
        if(this.winner == this.p1)
            return p2;
        else
            return p1;
    }
    
    public Strategy winnerStrategy() {
        if(this.winner == this.p1)
            return p1_strategy;
        else if(this.winner == this.p2)
            return p2_strategy;
        else return null;    
    }

    public Strategy loserStrategy() {
        if(this.winner == this.p1)
            return p2_strategy;
        else if(this.winner == this.p2)
            return p1_strategy;
        else return null;
    }

    public float getValue() {
        return value;
    }

    public float getCost() {
        return cost;
    }

    public String getBattleName() {
        return battleName;
    }

    public PlayerAgent getP1() {
        return p1;
    }

    public PlayerAgent getP2() {
        return p2;
    }
    
    
    
    
    
    public static boolean generateBattleReportsLogFile_CSV() throws IOException{
        File Fileright = new File("battleReports.csv");
        if(Fileright.createNewFile())
            writer = new PrintWriter(Fileright);
        else
            throw new IOException();
        writer.write("step , number of hawks, number of doves\n");
        return true;
    }
    
    
    public static void logBattle(List<PlayerAgent> Players) throws IOException{
        int length = Players.size();
        
        int num_hawks = 0;
        int num_doves = 0;
        
        for(int i =0; i < length; i++)
        {
            PlayerAgent player = Players.get(i);
            if(player.getStrategy() == Strategy.Hawk)
                num_hawks++;
            else
                num_doves++;
        }

        BattleReport.writer.write( 
                Integer.toString(BattleReport.stepCounter++) 
                        + ", " + 
                        Integer.toString(num_hawks)
                        + ", " + 
                        Integer.toString(num_doves)
                        + "\n"
        );
    }
    
    public static boolean closeBattleReportsLogFile_CSV() throws IOException{
        writer.close();
        return true;
    }
    
    
    public static void logBattle(BattleReport report){
        String result;
        if(report.winner == report.getP1())
            result = report.getP1().getName() + " wins";
        else if(report.winner == report.getP2())
            result = report.getP2().getName() + " wins";
        else
            result = "Draw";
        System.out.print(
                "\n" +
                report.getBattleName()
                + ": " + 
                report.getP1().getName() + " [ " + report.p1_strategy + " ]"
                + " VS " + 
                report.getP2().getName() + " [ " + report.p2_strategy + " ]"
                + " => " + 
                result
        );
    }
}
