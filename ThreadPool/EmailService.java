package ThreadPool;

import java.util.concurrent.*;

public class EmailService {
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void sendEmail(String recipient){

        executor.execute(()->{ // Runnable always has a void function.
            System.out.println("Sending email to the "+ recipient + " on "+ Thread.currentThread().getName());

            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
            System.out.println("Email send to "+ recipient);
        });
    }

    public static void main(String args[]){
        for(int i=1;i<=100;i++){
            sendEmail("user "+ i+ "@gmail.com");
        }
        executor.shutdown();
    }
}

// Executor framework ->
/*

Its a java framework, that is used for manually managing the Threads.
It executes Threads in queues ( bunch of threads, say 10)

Uses newFixedThreadPool()

*/

class FutureExample{
    public static void main(String args[]) throws InterruptedException, ExecutionException{
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<Integer> future = executor.submit(()->{
            //callable -> has a return type
            Thread.sleep(1000);
            return 42;
        });

        System.out.println("Doing other work....");

        Integer result = future.get();
        System.out.println("Result: "+ result);

        executor.shutdown();
    }
}

/*
submit takes a callable.
*/