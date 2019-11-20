import jade.core.AID;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

public class InvitationBehaviour extends OneShotBehaviour {

    private String nameOfTopic;

    @Override
    public void action() {
        nameOfTopic = "Test "+System.currentTimeMillis();
//        nameOfTopic = "Test" + System.currentTimeMillis();
        AID bet = createTopic(nameOfTopic);
        System.out.println("Название аукциона: " + nameOfTopic);

        AID topic = subscribeTopic(nameOfTopic);

        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd  = new ServiceDescription();
        sd.setType("Auction");
        dfd.addServices(sd);

        DFAgentDescription[] foundAgents = new DFAgentDescription[0];
        try {
            foundAgents = DFService.search(getAgent(),dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
//        System.out.println("Количество участников"+(foundAgents.length-1));

        myAgent.addBehaviour(new BehavioursOfBet(new Behaviour_1(topic,foundAgents.length-1),new Behaviour_2(getAgent(),7000)));

        myAgent.addBehaviour(new contractSign_Buyer());

        for (DFAgentDescription foundAgent : foundAgents) {
            if (!foundAgent.getName().getLocalName().equals(getAgent().getLocalName())) {
                System.out.println("Отправил приглашение -> " + foundAgent.getName().getLocalName());
                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                msg.setContent(nameOfTopic);
                msg.addReceiver(foundAgent.getName());
                myAgent.send(msg);
            }
        }
    }

    private AID createTopic(String topicName) {
        TopicManagementHelper topicHelper;
        AID jadeTopic = null;
        try {
            topicHelper = (TopicManagementHelper) myAgent.getHelper(TopicManagementHelper.SERVICE_NAME);
            jadeTopic = topicHelper.createTopic(topicName);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return jadeTopic;
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

