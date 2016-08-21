package services;

import java.util.LinkedList;
import java.util.Queue;

public class ConsumerServiceImpl extends Thread implements ConsumerService {
    /*
    * hasNewData - flag which mean that producer sent all messages
     */
    private  boolean hasNewData = true;
    private String name ="";
    private Queue store = new LinkedList<String>();


    public ConsumerServiceImpl() {
    }

    public ConsumerServiceImpl( Queue store, String name) {
        this.store = store;
        this.name = name;
    }

    public void consume() {
        try {
            /*
            * Sleep added for test reason, can be commented
             */
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (hasNewData || !store.isEmpty()){
            String msg= "";

            synchronized (store){

                try {
                    if (hasNewData && store.isEmpty()){
                        /*
                        * wait if not all producer sent messages
                         */
                        store.wait(100);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                msg =  (String)store.poll();
                store.notify();

                if(msg != null){
                    System.out.println(this.name + " <- " + msg);
                }
            }
        }

    }

    public void run() {
        consume();
        System.out.println( this.name + "  is finished");
    }

    public void setHasNewData (boolean data){
        this.hasNewData = data;
    }
}
