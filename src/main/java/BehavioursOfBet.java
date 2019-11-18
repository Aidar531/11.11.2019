import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.WakerBehaviour;

public class BehavioursOfBet extends ParallelBehaviour {

    private Behaviour a,b;

    public BehavioursOfBet(Behaviour a, Behaviour b) {
        super(WHEN_ANY);
        this.a =a;
        this.b =b;
    }

    @Override
    public void onStart() {
        addSubBehaviour(a);
        addSubBehaviour(b);
    }

    @Override
    public int onEnd() {
        if (a.done()){
            System.out.println("behaviour ended successfully");
            return 1;
        } else if (b.done()){
            return 2;
        } else {
            return 0;
        }

    }

}
