import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;

public class Behaviour_1 extends Behaviour {
    AID topic;
    private ArrayList<String> listOfPrices = new ArrayList<>();
    public Behaviour_1(AID topic) {
        this.topic = topic;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchTopic(topic);
        ACLMessage msg = getAgent().receive(mt);
        if (msg != null) {
            listOfPrices.add(msg.getContent());
        }
        else block();

        if (listOfPrices.size()==3){
            for ()
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
