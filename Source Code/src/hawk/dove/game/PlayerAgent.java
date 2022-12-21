package hawk.dove.game;
import java.util.List;
import sim.engine.SimState;
import sim.engine.Steppable;
import ec.util.MersenneTwisterFast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PlayerAgent implements Steppable {
    private static final MersenneTwisterFast randomNumberGenerator = new MersenneTwisterFast();
    private float payOff;
    private float prevPayOff;
    private final String name;
    private float HawkPropability = 50;
    private float prev_HawkPropability;
    private Strategy strategy;
    private Strategy prev_strategy;
    public Boolean isPlaying;
    public float forgetting;
    public float experimenting;
    public float HawkPropensity;
    public float DovePropensity;
    
    public PlayerAgent(String name, float forgetting, float experimenting) {
        this.name = name;
        this.payOff = 0;
        this.prevPayOff = 0;
        this.HawkPropensity = 1;
        this.DovePropensity = 1;
        this.isPlaying = false;
        this.forgetting = forgetting;
        this.experimenting = experimenting;
        if(randomNumberGenerator.nextInt(5000) % 2 == 0)
            this.strategy = Strategy.Hawk;
        else
            this.strategy = Strategy.Dove;
        this.prev_strategy = this.strategy;
    }

    public float getPayOff() {
        return this.payOff;
    }

    public String getName() {
        return name;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public float getHawkPropability() {
        return HawkPropability;
    }

    public void setHawkPropability(float HawkPropability) {
        this.HawkPropability = HawkPropability;
    }
    
    public float getHawkPropabilityChange() {
        return HawkPropability - prev_HawkPropability;
    }

    public float getPrevPayOff() {
        return prevPayOff;
    }
    
    public float updatePayOff(Strategy OpponentStrategy,int value, int cost){
        this.prevPayOff = this.payOff;
        float payoff = 0;
        if(this.strategy == Strategy.Hawk && OpponentStrategy == Strategy.Hawk)
             payoff = value / 2 - cost;
        else if(this.strategy == Strategy.Hawk && OpponentStrategy == Strategy.Dove)
        {
            payoff = value;
        }
        else if(this.strategy == Strategy.Dove && OpponentStrategy == Strategy.Dove)
            payoff = value / 2;
        else if(this.strategy == Strategy.Dove && OpponentStrategy == Strategy.Hawk)
            payoff = 0;
        this.payOff = payoff;
        return payoff;
    }
    
    public boolean changeStrategy(float payoff, LearningMethod method){
        if(method == LearningMethod.Both || method == LearningMethod.Individual_Roth_Erve)
        {
            this.HawkPropensity = this.HawkPropensity * (1 - this.forgetting) + payoff * (1 - this.experimenting) * (this.strategy == Strategy.Hawk? 1 : 0);
            this.DovePropensity = this.DovePropensity * (1 - this.forgetting) + payoff * (1 - this.experimenting) * (this.strategy == Strategy.Dove? 1 : 0);
            this.prev_HawkPropability = this.HawkPropability;
            this.HawkPropability= this.HawkPropensity / (this.HawkPropensity + this.DovePropensity) * 100;
        }
        
        int RandomValue = PlayerAgent.randomNumberGenerator.nextInt(100);
        boolean res = this.prev_strategy != this.strategy;
        this.strategy = (RandomValue > (100 - this.HawkPropability))? Strategy.Hawk : Strategy.Dove;
        this.prev_strategy = this.strategy;
        return res;
    }
    
    @Override
    public String toString() {
        return this.getName() + " { " + ((this.getStrategy() == Strategy.Hawk)? "Hawk" : "Dove") + " }";
    }

    public boolean requestToEnterBattle(Battle battle){
        if(battle.isBattleRoomFull())
            return false;
        battle.Players.push(this);
        return true;
    }
    
    @Override
    public void step(SimState state) {
        HawkDoveGame game = (HawkDoveGame) state;
        List<Battle> battleRooms = game.BattleRooms;
        if(game.stopPlaying)
            return;
        for(int i = 0; i < battleRooms.size(); i++)
        {
            Battle battleRoom = battleRooms.get(i);
            if(battleRoom.isBattleRoomFull() == false)
            {
                if(this.requestToEnterBattle(battleRoom) == false)
                    continue;
                battleRoom.Battle(game.learningMethod);
                BattleReport battleReport = new BattleReport(battleRoom);
                game.BattleReports.add(battleReport);
                BattleReport.logBattle(battleReport);
                if(game.BattleReports.size() >= game.battlesPerStep) // was using > instead of >= which lead to an additional step
                {
                    game.stopPlaying = true;
                    if(game.learningMethod == LearningMethod.PSO_Social_Learning || game.learningMethod == LearningMethod.Both)
                        game.PSO_Learn();
                    try {
                    BattleReport.logBattle(game.Players);
                    game.BattleReports = new ArrayList<>();
                    if(game.stepCounter == game.numberOfSteps)
                    {
                        BattleReport.closeBattleReportsLogFile_CSV();
                        System.exit(0);
                    }
                    else
                        game.stepCounter++;
                    game.stopPlaying = false;
                } catch (IOException ex) {
                    Logger.getLogger(PlayerAgent.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
                battleRooms.remove(battleRoom);
                return;
            }
        }
        int cost = 0;
        int value = 0;
        switch(game.Cost)
            {
                case Constant:
                    cost = 10;
                    break;
                case UniformDistribution:
                    cost = game.costUniformDistributer.nextInt();
                    break;
                case NormalDistribution:
                    cost = game.costNormalDistributer.nextInt();
                    break;
            }
            
            switch(game.Value)
            {
                case Constant:
                    value = 100;
                    break;
                case NormalDistribution:
                    value = game.costNormalDistributer.nextInt();
                    break;
            }
        Battle newBattle = new Battle(value, cost, this);
        battleRooms.add(newBattle);
        
    }
}
