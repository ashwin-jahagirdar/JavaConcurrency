import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GenericStack<T extends Number> {
    private T[] container;
    private int top;
    private Lock lock;
    private Condition isEmpty;
    private Condition isFull;

    public GenericStack(int capacity) {
        container = (T[]) new Number[capacity];
        top = -1;
        lock = new ReentrantLock();
        isEmpty = lock.newCondition();
        isFull = lock.newCondition();
    }

    public T pop() {
        lock.lock();
        System.out.printf("%s trying to pop...%n", Thread.currentThread().getName());
        T poppedValue = null;
        try {
            while (isStackEmpty()) {
                System.out.printf("Stack is empty, %s will wait...%n", Thread.currentThread().getName());
                isEmpty.await();
            }
            poppedValue = container[top];
            container[top] = null;
            top--;
            isFull.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return poppedValue;
    }

    public void push(T value) {
        lock.lock();
        System.out.printf("%s trying to push...%n", Thread.currentThread().getName());
        try {
            while (isStackFull()) {
                System.out.printf("Stack is full, %s will wait...%n", Thread.currentThread().getName());
                isFull.await();
            }
            container[++top] = value;
            isEmpty.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private boolean isStackEmpty() {
        return top == -1;
    }

    private boolean isStackFull() {
        return top == (container.length - 1);
    }
}

class Pusher implements Runnable {
    private GenericStack<Integer> stack;

    public Pusher(GenericStack<Integer> stack) {
        this.stack = stack;
    }

    @Override
    public void run() {
        for (int i=1; i <= 100; ++i) {
            System.out.printf("Pushing - %d%n", i);
            stack.push(i);
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Popper implements Runnable {
    private GenericStack<Integer> stack;

    public Popper(GenericStack<Integer> stack) {
        this.stack = stack;
    }

    @Override
    public void run() {
        for (int i=1; i <= 100; ++i) {
            System.out.printf("Popped value - %d%n", stack.pop());
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class StackApplication {
    public static void main(String[] args) {
        GenericStack<Integer> stack = new GenericStack<>(20);
        new Thread(new Pusher(stack), "Pusher").start();
        new Thread(new Popper(stack), "Popper").start();
    }
}