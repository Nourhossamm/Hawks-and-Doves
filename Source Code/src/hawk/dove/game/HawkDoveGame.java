package hawk.dove.game;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sim.engine.*;
import sim.util.distribution.Uniform;
import sim.util.distribution.Normal;
import java.util.PriorityQueue;


public class HawkDoveGame extends SimState {
    public List<Battle> BattleRooms = new ArrayList();
    public List<BattleReport> BattleReports = new ArrayList();
    public List<PlayerAgent> Players = new ArrayList();

    public Normal valueNormalDistributer = new Normal(100, 10, this.random);
    public Uniform costUniformDistributer = new Uniform(10, 60, this.random);
    public Normal costNormalDistributer = new Normal(30, 5, this.random);
    public Uniform ExperimintationUniformDistributer = new Uniform(0.4, 0.6, this.random);
    public Uniform ForgettingUniformDistributer = new Uniform(0.7, 0.9, this.random);
    
    public CostCase Cost = CostCase.NormalDistribution;
    public ValueCase Value = ValueCase.NormalDistribution;
    public LearningMethod learningMethod = LearningMethod.Both;
    
    final public int battlesPerStep = 100;
    final public int numberOfSteps = 50;
    public int stepCounter = 1;
    final public float socialConformity = (float) 0.01;
    public boolean stopPlaying = false;
    @Override
    public void start()
    {
        super.start();
        try {
            BattleReport.generateBattleReportsLogFile_CSV();
        } catch (IOException ex) {
            Logger.getLogger(HawkDoveGame.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
        for(int i = 0; i < 100; i++)
        {
            float forgetting = (float)this.ForgettingUniformDistributer.nextDouble();
            float experimenting = (float)this.ExperimintationUniformDistributer.nextDouble();
            PlayerAgent p = new PlayerAgent(
                    "Player (" + Integer.toString(i + 1) + ")",
                    forgetting,
                    experimenting
                    );
            Players.add(p);
            schedule.scheduleRepeating(p);
        }  
    }
    
    public void PSO_Learn(){
        float socialAverage = 0;
        float I;
        float C;
        PriorityQueue PlayersQueue = new PriorityQueue(100, new PlayerComparator());
        PlayerAgent[] PlayersArray = new PlayerAgent[100];
        int numberOfPlayers = this.Players.size();
        
        //Arranging players using priority queue
        for(int i = 0; i < numberOfPlayers; i++)
        {
            PlayerAgent p = this.Players.get(i);
            socialAverage += p.getHawkPropability();
            PlayersQueue.add(p);
        }
        
        socialAverage /= numberOfPlayers;

        PlayersArray[0] = (PlayerAgent) PlayersQueue.poll();
        PlayersArray[1] = (PlayerAgent) PlayersQueue.poll();
        float demonestratorPercentage = PlayersArray[0].getHawkPropability();
        I  = PlayersArray[1].getHawkPropability() - demonestratorPercentage;
        C = PlayersArray[1].getHawkPropability() - socialAverage;
        
        PlayersArray[1].setHawkPropability(PlayersArray[1].getHawkPropability() + PlayersArray[1].getHawkPropabilityChange() + I + socialConformity * C);
        for(int i = 2; i < 100; i++)
        {
            PlayersArray[i] = (PlayerAgent) PlayersQueue.poll();
            demonestratorPercentage = PlayersArray[this.random.nextInt(i - 1)].getHawkPropability();
            I  = PlayersArray[i].getHawkPropability() - demonestratorPercentage;
            C = PlayersArray[i].getHawkPropability() - socialAverage;
            PlayersArray[i].setHawkPropability(
                    PlayersArray[i].getHawkPropability() 
                + 
                    PlayersArray[i].getHawkPropabilityChange()
                + 
                    I 
                + 
                    socialConformity * C);
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
