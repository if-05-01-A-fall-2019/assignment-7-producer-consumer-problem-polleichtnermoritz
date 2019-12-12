package ProducerConsumer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

public class ProducerConsumer {

    static Semaphore producer = new Semaphore(1);
    static Semaphore consumer = new Semaphore(0);
    static LinkedList<Integer> buffer = new LinkedList<Integer>();
    Random random = new Random();

    public void produce() throws InterruptedException{
        int item;
        while(true){
            item = random.nextInt();
            Thread.sleep(1000);
            try {
                producer.acquire();
            } catch (InterruptedException ex) {
                System.out.println("An error occured:"+ex.getMessage());
            }

            System.out.println("Producer produced " + item);

            buffer.add(item);
            consumer.release();
        }
    }

    public void consume() throws InterruptedException{
        while(true){
            try {
                consumer.acquire();
            } catch (InterruptedException ex) {
                System.out.println("An error occured:"+ex.getMessage());
            }

            System.out.println("Consumer consumed " + buffer.remove(buffer.size() -1));

            producer.release();
            Thread.sleep(1000);
        }


    }

    public static void main(String[] args) {
        ProducerConsumer pc = new ProducerConsumer();
        ExecutorService es = Executors.newFixedThreadPool(2);;

        es.execute(new Runnable() {
            @Override
            public void run()
            {
                try {
                    pc.produce();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        es.execute(new Runnable() {
            @Override
            public void run()
            {
                try {
                    pc.consume();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        es.shutdown();
    }

}
