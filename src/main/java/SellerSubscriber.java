import com.sun.media.sound.AiffFileReader;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.Behaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Random;

public class SellerSubscriber extends Agent {
    private Random r = new Random();
    private double minPrice = 2.0 + r.nextDouble()*4;
    public AID topic;
    @Override
    protected void setup() {
        addBehaviour(new Behaviour() {
            @Override
            public void action() {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                ACLMessage msg = myAgent.receive(mt);

                if (msg != null) {
                    topic = subscribeTopic(msg.getContent());
                    System.out.println("I'm in" + " -> " + getLocalName());
                }
            }

            @Override
            public boolean done() {
                return false;
            }
        });

    }

    private AID subscribeTopic(String topicName) {
        TopicManagementHelper topicHelper;
        AID jadeTopic = null;
        try {
            topicHelper = (TopicManagementHelper)
                    getHelper(TopicManagementHelper.SERVICE_NAME);
            jadeTopic = topicHelper.createTopic(topicName);
            topicHelper.register(jadeTopic);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return jadeTopic;
    }
}
