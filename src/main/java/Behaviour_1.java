import com.sun.org.apache.xpath.internal.objects.XNumber;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.Comparator;

public class Behaviour_1 extends Behaviour {
    AID topic;
    public double min = 100;
    public boolean flag = true;
    public int numberOfParticipants;
    public  String Winner;

    private ArrayList<Double> listOfPrices = new ArrayList<>();
    public ArrayList<String> listOfParticipants = new ArrayList<>();
    private boolean StopAuction=false;

    public Behaviour_1(AID topic, int number) {
        this.topic = topic;
        this.numberOfParticipants = number;
    }

//    @Override
//    public void onStart() {
//        System.out.println(min);
//    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchTopic(topic),
                MessageTemplate.MatchPerformative(ACLMessage.PROPOSE));

        ACLMessage msg = getAgent().receive(mt);

//        System.out.println("жду ставок");
        if (msg != null) {
//            System.out.println(numberOfParticipants);
            System.out.println("Писльмо пришло от:"+ msg.getSender().getLocalName() + " " + msg.getContent());
            // Проверка не слился ли участник, если слился удаляем его из игры
            if (msg.getContent().equals("I'm out")) {
                numberOfParticipants = numberOfParticipants - 1;
//                System.out.println(numberOfParticipants);
            }
            // Проверка сколько участников осталось, если больше одного, то ведем расчет минимумма
            if (numberOfParticipants > 1) {
                // Формирование массива значений ставок и массива из участников
                if (!msg.getContent().equals("I'm out")) {
                    if (!msg.getContent().equals("I'll pass")) {
                        listOfPrices.add(Double.parseDouble(msg.getContent()));
                    } else  {
                        listOfPrices.add(100000.0);
                    }
                    listOfParticipants.add(msg.getSender().getLocalName());
                }
                //System.out.println("количество участников" + numberOfParticipants);
                //Проверка если массив из необходимого количества участников наполненн
                if (listOfPrices.size() == numberOfParticipants) {
//                listOfPrices.forEach(el -> Double.parseDouble(el));
//                Double min = listOfPrices.stream().map(el -> Double.parseDouble(el)).min(Comparator.naturalOrder());
                    for (Double i : listOfPrices) {
                        if (i < min) {
                            min = i;
//                            System.out.println(min + " " +listOfPrices.indexOf(min));
                            Winner = listOfParticipants.get(listOfPrices.indexOf(min));
                        }
                    }
                    //Отправка предварительного миниумму и победителя
                    System.out.println("Предварительный минимумм " + min);
                    ACLMessage msg1 = new ACLMessage(ACLMessage.INFORM);
                    msg1.setContent(Double.toString(min));
                    msg1.setProtocol("minBet");
                    msg1.addReceiver(topic);
                    myAgent.send(msg1);

                    System.out.println("Предварительный победитель " + Winner);
                    msg1.setProtocol("Winner");
                    msg1.setContent(Winner);
                    myAgent.send(msg1);

                    listOfPrices.clear();
                    listOfParticipants.clear();
                }
            }
            else {
                System.out.println("Победитель выбран" + Winner);
                StopAuction = true;
            }
        }
        else block();
    }

    @Override
    public int onEnd() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(topic);
        msg.setProtocol("Winner");
        msg.setContent("Stop");
        myAgent.send(msg);
        return super.onEnd();
    }

    @Override
    public boolean done() {
        return StopAuction;
    }
}
