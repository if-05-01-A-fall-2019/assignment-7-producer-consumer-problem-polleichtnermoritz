import java.util.LinkedList;
import java.util.Random;

public class ProducerConsumer {
    LinkedList<Integer> buffer = new LinkedList<>();
    static final int size = 5;
    Random random = new Random();


    public void produce() throws InterruptedException
    {
        int item = 0;
        while (true) {
            synchronized (this)
            {
                item = random.nextInt();
                Thread.sleep(1000);
                if (buffer.size() == size){
                    wait();
                }
                System.out.println("Producer produced " + item);
                buffer.add(item);
                if (buffer.size() == 1)
                    notify();
            }
        }
    }

    public void consume() throws InterruptedException
    {
        int item;
        while (true) {
            synchronized (this)
            {
                item = random.nextInt();
                if(buffer.size() == 0) {
                    wait();
                }

                item = buffer.remove(buffer.size() - 1);
                System.out.println("Consumer consumed " + item);
                if (buffer.size() == size - 1)
                    notify();
            }
            Thread.sleep(1000);
        }
    }
}