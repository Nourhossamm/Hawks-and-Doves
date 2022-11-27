package hawk.dove.game;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sim.engine.*;
import sim.util.distribution.Uniform;
import sim.util.distribution.Normal;


public class HawkDoveGame extends SimState {
    public List<Battle> BattleRooms = new ArrayList();
    public List<BattleReport> BattleReports = new ArrayList();
    public int minCost;
    public int maxCost;
    public int minValue;
    public int maxValue;
    public Normal valueNormalDistributer = new Normal(100, 10, this.random);
    public Uniform costUniformDistributer = new Uniform(10, 60, this.random);
    public Normal costNormalDistributer = new Normal(30, 5, this.random);
    public CostCase Cost = CostCase.NormalDistribution;
    public ValueCase Value = ValueCase.NormalDistribution;
    public PrintWriter writer;
    
    @Override
    public void start()
    {
        super.start();
        File Fileright = new File("simulation.log");
        try {
            if(Fileright.createNewFile())
                writer = new PrintWriter(Fileright);
        } catch (IOException ex) {
            Logger.getLogger(HawkDoveGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            BattleReport.generateBattleReportsLogFile_CSV();
        } catch (IOException ex) {
            Logger.getLogger(HawkDoveGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(int i = 0; i < 100; i++)
        {
            schedule.scheduleRepeating(new PlayerAgent("Player (" + Integer.toString(i + 1) + ")"));
        }  
    }
    
    
    public static void main(String[] args) {
        doLoop(HawkDoveGame.class, args);

        System.exit(0);
    }

    public HawkDoveGame(long seed) {
        super(seed);

    }
    
}
