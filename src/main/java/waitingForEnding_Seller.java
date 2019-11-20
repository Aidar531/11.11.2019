import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import sun.management.Agent;

public class waitingForEnding_Seller extends Behaviour {
    AID topic;
    public String winner;
    private boolean Stop = false;

    public waitingForEnding_Seller(AID topic) {
        this.topic = topic;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchTopic(topic),
                MessageTemplate.MatchProtocol("Winner"));
        ACLMessage msg = myAgent.receive(mt);

        if (msg!=null) {
            if (msg.getContent().equals("Stop")) {
                Stop = true;
                if (getAgent().getLocalName().equals(winner)) {
                    ACLMessage reply = new ACLMessage(ACLMessage.AGREE);
                    reply.addReceiver(topic);
                    myAgent.send(reply);
                }
            } else {
                winner = msg.getContent();
            }
        }
        else block();
    }

    @Override
    public int onEnd() {
        myAgent.addBehaviour(new waitngForInvitation_Seller());
        return super.onEnd();
    }

    @Override
    public boolean done() {
        return Stop;
    }
}
