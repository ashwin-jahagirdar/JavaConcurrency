import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

// Both ArrayBlockingQueue or SynchronousQueue can be used.
public class PCWithBlockingQ {
    public static void main(String[] args) {
        //BlockingQueue<String> container = new ArrayBlockingQueue<>(1);
        BlockingQueue<String> container = new SynchronousQueue<>();
        new Thread(new Producer(container)).start();
        new Thread(new Consumer(container)).start();
    }
}

class Producer implements Runnable {
    private BlockingQueue<String> container;

    Producer(BlockingQueue container) {
        this.container = container;
    }

    @Override
    public void run() {
        for (int i=1; i <= 100; ++i) {
            try {
                boolean isMsgSent = container.offer("Test Message - " + i, 1000, TimeUnit.MILLISECONDS);
                System.out.printf("Was Message sent - %b%n", isMsgSent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Consumer implements Runnable {
    private BlockingQueue<String> container;

    Consumer(BlockingQueue container) {
        this.container = container;
    }

    @Override
    public void run() {
        for (int i=1; i <= 100; ++i) {
            try {
                System.out.println("Trying to retrieve message...");
                String msg = container.poll(3000, TimeUnit.MILLISECONDS);
                if (msg == null) {
                    System.out.println("No message arrived...");
                } else {
                    System.out.printf("Message : %s%n", msg);
                }
                TimeUnit.MILLISECONDS.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}