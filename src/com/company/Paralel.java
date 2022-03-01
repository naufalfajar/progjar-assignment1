package com.company;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Paralel {
    private final Collection<Runnable> tasks = new ArrayList<Runnable>();

    public void add(final Runnable task) {
        tasks.add(task);
    }

    public void go() throws InterruptedException {
        final ExecutorService threads = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try {
            final CountDownLatch latch = new CountDownLatch(tasks.size());
            for (final Runnable task : tasks)
                threads.execute(new Runnable() {
                    public void run() {
                        try {
                            task.run();
                            System.out.println("Sedang mengunduh file");
                        } finally {
                            latch.countDown();
                            System.out.println("file selesai diunduh");
                        }
                    }
                });
            latch.await();
        } finally {
            threads.shutdown();
        }
    }
}
