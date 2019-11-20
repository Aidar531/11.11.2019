import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Test extends Behaviour {


    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchProtocol("Test");
        ACLMessage msg = myAgent.receive(mt);
        if (msg!=null) {
            System.out.println("Gotten msg" + msg.getContent());
        } else
            block();
    }

    @Override
    public boolean done() {
        return false;
    }
}
