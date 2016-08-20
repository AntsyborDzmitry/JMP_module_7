package services;

import java.util.*;

public class ProducerServiceImpl extends Thread implements ProducerService {

    private String name ="";
    private List<String> data = new ArrayList<String>();
    private Queue store = new LinkedList<String>();

    public ProducerServiceImpl() {
    }

    public ProducerServiceImpl(Queue store, List<String> data, String name) {
        this.store = store;
        this.name = name;
        this.data.addAll(data);
    }

    public void produce() {
        Random r = new Random();
        int index = 0;
        String msg = "";

        while (data.size()>0) {
            /*
             * randomly choose message from receiving list;
             * data.size() -> max generated int value,
             * based on this index will  put  message to queue and deleted from list
             */
            index = r.nextInt(data.size());

            synchronized (store){
                msg = data.remove(index);
                store.add(msg);
                store.notifyAll();
                System.out.println(this.name + " -> " + msg);
            }

            try {
                /*
                * Sleep added for test reason, can be commented
                 */
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        produce();
        System.out.println( this.name + " is finished");
    }
}
