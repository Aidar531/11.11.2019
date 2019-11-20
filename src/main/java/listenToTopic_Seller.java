import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class listenToTopic_Seller extends Behaviour {


    private boolean StopAuction = false;

    public listenToTopic_Seller(AID topic, double price) {
        this.topic = topic;
        this.myBet = price;
    }
    AID topic;
    public double myBet;
    public double minStavka;

    @Override
    public void onStart() {
        minStavka = myBet/2;
//        System.out.println(minStavka);
//        System.out.println(myAgent.getLocalName() +  " Жду минимальную ставку");
    }

    @Override
    public void action() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchTopic(topic),
                MessageTemplate.MatchProtocol("minBet"));

        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            if (Double.parseDouble(msg.getContent()) < myBet) {
                myBet = 0.9*myBet;
//                System.out.println(myAgent.getLocalName() +  " текущая ставка " + myBet);
                if (myBet > minStavka) {
                    ACLMessage msg1 = new ACLMessage(ACLMessage.PROPOSE);
                    msg1.setContent(Double.toString(myBet));
                    msg1.addReceiver(topic);
                    myAgent.send(msg1);
                }
                else {
                        ACLMessage msg1 = new ACLMessage(ACLMessage.PROPOSE);
                        msg1.setContent("I'm out");
                        msg1.addReceiver(topic);
                        myAgent.send(msg1);
                        StopAuction = true;
                }
            }
            else {
                ACLMessage msg1 = new ACLMessage(ACLMessage.PROPOSE);
                msg1.setContent("I'll pass");
                msg1.addReceiver(topic);
                myAgent.send(msg1);
            }
        }
        else block();
    }

    @Override
    public boolean done() {
        return StopAuction;
    }
}
