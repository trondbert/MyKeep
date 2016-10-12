package future.completable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author trond.
 */
public class Futurum {

    public static void main(final String[] args) {
        final CompletableFuture<String> future = new CompletableFuture<String>();

        System.out.println("Starting new thread...");
        new Thread() {
            @Override
            public void run() {
                System.out.println("Going to sleep...");
                try {
                    Thread.sleep(4000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                future.complete("42");
                System.out.println("Completed future");
                System.out.println("Going to sleep...");
                try {
                    Thread.sleep(4000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Done sleeping a second time");
            }
        }.start();

        try {
            final long startTime = System.nanoTime();
            System.out.println("Getting future...");
            System.out.println(future.get() + " " + (System.nanoTime() - startTime));
        }
        catch (final InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
