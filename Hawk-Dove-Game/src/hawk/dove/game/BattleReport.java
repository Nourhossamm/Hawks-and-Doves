package hawk.dove.game;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;


public class BattleReport {
    private final PlayerAgent p1;
    private final PlayerAgent p2;
    private final PlayerAgent winner;
    private final Strategy p1_strategy;
    private final Strategy p2_strategy;
    private final float value;
    private final float cost;
    private final String battleName;
    public static PrintWriter writer;
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
            return false;
        writer.write("battle name, player 1, player 2, result, winner, loser, strategy of winner, strategy of loser, value, cost");
        return true;
    }
    
    
    public static void logBattle(BattleReport report) throws IOException{
        String result = "";
        if(report.winner == report.getP1())
            result = report.getP1().getName() + " wins";
        else if(report.winner == report.getP2())
            result = report.getP2().getName() + " wins";
        else
            result = "Draw";
        writer.write(
                "\n" +
                report.getBattleName()
                + "," + 
                report.getP1().getName()
                + "," + 
                report.getP2().getName()
                + "," + 
                result
                + "," + 
                (("Draw".equals(result))? "N/A" : report.getWinner().getName())
                + "," + 
                (("Draw".equals(result))? "N/A" : report.getLoser().getName())
                + "," + 
                (("Draw".equals(result))? "N/A" : (report.winnerStrategy() == Strategy.Hawk)? "Hawk" : "Dove")
                + "," +  
                (("Draw".equals(result))? "N/A" : (report.loserStrategy()== Strategy.Hawk)? "Hawk" : "Dove")
                + "," +  
                report.getValue()
                + "," + 
                report.getCost()
        );
    }
}
