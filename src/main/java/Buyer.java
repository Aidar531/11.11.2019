import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

public class Buyer extends Agent {
    private int interCounter;
    private String nameOfTopic;
    private ArrayList<AID> listOfSellers = new ArrayList<>();
    @Override
    protected void setup() {
        listOfSellers.add(new AID("Seller1",false));
        listOfSellers.add(new AID("Seller2",false));
        listOfSellers.add(new AID("Seller3",false));
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                nameOfTopic = "Test" + interCounter;
                AID bet = createTopic(nameOfTopic);
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setContent(nameOfTopic);
                for (AID i: listOfSellers) {
                    msg.addReceiver(i);
                }
                getAgent().send(msg);
            }
        });
//    addBehaviour(new TickerBehaviour( this, 20000){
//        @Override
//        protected void onTick() {
//
//            ACLMessage msg=new ACLMessage(ACLMessage.INFORM);
//            msg.addReceiver(bet);
//            interCounter+=1;
//            msg.setContent("Hello"+ interCounter);
//            getAgent().send(msg);
//            msg.setContent("Yep");
//            getAgent().send(msg);
//        }
//    });
    }

    private AID createTopic(String topicName) {
        TopicManagementHelper topicHelper;
        AID jadeTopic = null;
        try {
            topicHelper = (TopicManagementHelper)
                    getHelper(TopicManagementHelper.SERVICE_NAME);
            jadeTopic = topicHelper.createTopic(topicName);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return  jadeTopic;

    }
}
