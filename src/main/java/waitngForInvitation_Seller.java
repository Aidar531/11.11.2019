import jade.core.AID;
import jade.core.ServiceException;
import jade.core.behaviours.Behaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Random;

public class waitngForInvitation_Seller extends Behaviour {

    AID topic;
    public boolean stop=false;
    private Random r = new Random();
    private double myPrice = 2.0 + r.nextDouble()*4;

    @Override
    public void action() {

        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        ACLMessage msg = myAgent.receive(mt);

        if (msg != null) {

//            System.out.println("Отправил ставкку " + myAgent.getLocalName() + " " +myPrice*2);
            topic = subscribeTopic(msg.getContent());
//            System.out.println(topic.getLocalName());

            ACLMessage msg1 = new ACLMessage(ACLMessage.PROPOSE);
            msg1.setContent(Double.toString(myPrice*2));
//            msg1.setProtocol("myBet");
            msg1.addReceiver(topic);
            myAgent.send(msg1);

            myAgent.addBehaviour((new waitingForEnding_Seller(topic)));

            System.out.println("I'm in" + " -> " + myAgent.getLocalName());
            myAgent.addBehaviour(new listenToTopic_Seller(topic,2*myPrice));
            stop = true;
        }
        else block();
    }

    @Override
    public boolean done() {
        return stop;
    }

    private AID subscribeTopic(String topicName) {
        TopicManagementHelper topicHelper;
        AID jadeTopic = null;
        try {
            topicHelper = (TopicManagementHelper)
                    myAgent.getHelper(TopicManagementHelper.SERVICE_NAME);
            jadeTopic = topicHelper.createTopic(topicName);
            topicHelper.register(jadeTopic);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return jadeTopic;
    }
}
