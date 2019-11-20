import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class contractSign_Buyer extends Behaviour {
    private boolean Stop = false;

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.AGREE);
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            System.out.println("Контракт подписан с " + msg.getSender().getLocalName());
            Stop = true;
        } else block();
    }

    @Override
    public int onEnd() {
        System.out.println("Начинаем новый аукцион");
        myAgent.addBehaviour(new InvitationBehaviour());
        return super.onEnd();
    }

    @Override
    public boolean done() {
        return Stop;
    }
}
