public class DeadlockDemo {
    public static void main(String[] args) {
        Object lock1 = new Object();
        Object lock2 = new Object();
        new Thread(new Task1(lock1, lock2)).start();
        new Thread(new Task2(lock1, lock2)).start();
    }
}

class Task1 implements Runnable {
    Object lock1;
    Object lock2;

    Task1(Object lock1, Object lock2) {
        this.lock1 = lock1;
        this.lock2 = lock2;
    }

    @Override
    public void run() {
        System.out.println("Starting Task 1");
        System.out.println("Acquiring Resource 1");
        synchronized (lock1) {
            System.out.println("Acquired Resource 1");
            System.out.println("Now acquiring Resource 2");
            try {
                Thread.sleep(3000);
                synchronized (lock2) {
                    System.out.println("Acquired Resource 2");
                    Thread.sleep(3000);
                    System.out.println("Task 1 completed");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Task2 implements Runnable {
    Object lock1;
    Object lock2;

    Task2(Object lock1, Object lock2) {
        this.lock1 = lock1;
        this.lock2 = lock2;
    }

    @Override
    public void run() {
        System.out.println("Starting Task 2");
        System.out.println("Acquiring Resource 2");
        synchronized (lock2) {
            System.out.println("Acquired Resource 2");
            System.out.println("Now acquiring Resource 1");
            try {
                Thread.sleep(3000);
                synchronized (lock1) {
                    System.out.println("Acquired Resource 1");
                    Thread.sleep(3000);
                    System.out.println("Task 2 completed");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
