import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class ExecutorAndCallable {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<Future<Long>> results = new ArrayList<>();
        Random random = new Random();
        for (int i=0; i < 10; ++i) {
            Integer number = random.nextInt(15);
            System.out.println("Calculating factorial for : " + number);
            Future<Long> factorial = executorService.submit(() -> {
                long result = 1L;
                for (int j = 2; j <= number; ++j) {
                    result *= j;
                }
                TimeUnit.MILLISECONDS.sleep(10);
                return result;
            });
            results.add(factorial);
        }
        System.out.println("All tasks submitted, now executing some other logic");
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Now processing results of submitted tasks");
        for (Future<Long> result : results) {
            System.out.println("Processing complete : " + result.isDone());
            try {
                System.out.println("Result : " + result.get(100, TimeUnit.MILLISECONDS));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                System.out.println("Timed out...");
                e.printStackTrace();
            }
        }
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            executorService.shutdownNow();
        }
    }
}
