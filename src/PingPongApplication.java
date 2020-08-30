// Print Ping/Pong in order.
public class PingPongApplication {
    public static void main(String[] args) {
        PingPong pinging = new PingPong();
        new Thread(() -> {
            while (true) {
                pinging.ping();
            }
        }).start();
        new Thread(() -> {
            while (true) {
                pinging.pong();
            }
        }).start();
    }
}

class PingPong {
    boolean ping = true;

    synchronized public void ping() {
        while (!ping) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Ping");
        ping = false;
        notifyAll();
    }

    synchronized public void pong() {
        while (ping) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Pong");
        ping = true;
        notifyAll();
    }
}
