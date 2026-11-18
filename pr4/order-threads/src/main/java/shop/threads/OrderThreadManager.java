package shop.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public final class OrderThreadManager implements AutoCloseable {
    private final ExecutorService executor;

    public OrderThreadManager(int threadCount) {
        if (threadCount <= 0) {
            throw new IllegalArgumentException("Кількість потоків повинна бути більшою за нуль.");
        }
        executor = Executors.newFixedThreadPool(threadCount);
    }

    public int processAll(List<Runnable> tasks) {
        List<Future<?>> futures = new ArrayList<>();
        for (Runnable task : tasks) {
            futures.add(executor.submit(task));
        }
        int successful = 0;
        for (Future<?> future : futures) {
            try {
                future.get();
                successful++;
            } catch (ExecutionException e) {
                System.err.println("Не вдалося обробити замовлення: " + e.getCause().getMessage());
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Очікування обробки перервано.", e);
            }
        }
        return successful;
    }

    @Override
    public void close() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.err.println("Не всі потоки завершили роботу.");
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
