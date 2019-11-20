import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;

import java.util.Date;

public class Behaviour_2 extends WakerBehaviour{


    public Behaviour_2(Agent a, long timeout) {
        super(a, timeout);
    }

    @Override
    protected void onWake() {
        System.out.println("Аукцион - вышло время!");
    }

}
