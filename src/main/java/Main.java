
import services.ConsumerService;
import services.ConsumerServiceImpl;
import services.ProducerServiceImpl;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        /*
         * Messages for different thread
         */
        List<String> data1  =  Arrays.asList("Hello Gomel-1","Hello Minsk-1","Hello Brest-1","Hello Grodno-1","Hello Vitebsk-1");
        List<String> data2 =  Arrays.asList("Hello Gomel-2","Hello Minsk-2","Hello Brest-2","Hello Grodno-2","Hello Vitebsk-2");
        List<String> data3 =  Arrays.asList("Hello Gomel-3","Hello Minsk-3","Hello Brest-3","Hello Grodno-3","Hello Vitebsk-3");

        /*
        * reciever/transmitter for messages
         */
        Queue messagesStore = new LinkedList<String>();

        /*
        * Thread pool of producers
         */
        ProducerServiceImpl producersThreads [] = {
               new ProducerServiceImpl(messagesStore, data1, "Prod_T-1"),
               new ProducerServiceImpl(messagesStore, data2, "Prod_T-2"),
               new ProducerServiceImpl(messagesStore, data3, "Prod_T-3")
        };

        /*
        * Thread pool of consumers
         */
        ConsumerServiceImpl consumersThread [] = {
               new ConsumerServiceImpl(messagesStore, "Cons_T-1"),
               new ConsumerServiceImpl(messagesStore, "Cons_T-2"),
               new ConsumerServiceImpl(messagesStore, "Cons_T-3")
        };

        startAllThreads(producersThreads);
        startAllThreads(consumersThread);

        /*
         * Separate thread verify if all messages sent and work could be finish
         */
        new Thread(() -> {
            boolean isSentAllMsg = checkStateAndSwitch(producersThreads, consumersThread);
            while (isSentAllMsg) {
                isSentAllMsg = checkStateAndSwitch(producersThreads, consumersThread);
            }
        }).start();
    }

    private static void startAllThreads (Thread [] tt) {
        if (tt.length > 0){
            for(Thread t : tt ){
                t.start();
            }
        }
    }
    /**
     * Check if all producer already finished messages sending, set flag for consumer that they also could finish they work
     */
    private static boolean checkStateAndSwitch(ProducerServiceImpl [] producers, ConsumerService [] consumers){
        boolean isProducersFinishedTask = true;
        if (producers.length > 0 && consumers.length > 0){
            isProducersFinishedTask = false;
            for(ProducerServiceImpl producer : producers ){
                if(producer.isAlive()){
                    isProducersFinishedTask = true;
                }
            }
            if (!isProducersFinishedTask) {
                for(ConsumerService consumer : consumers ){
                    consumer.setHasNewData(false);
                }
            }
        }
        return isProducersFinishedTask;
    }
}
