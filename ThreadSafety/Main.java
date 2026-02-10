package ThreadSafety;

import java.util.concurrent.atomic.AtomicInteger;

/*
 * 
 * Thread safety means that a peiece of code, object or method behaves correctly
 * and predictably when accessed by mulitple threads at the same time, without
 * corrupting the data or producing incorrect results.
 * 
 * Thread safety ensures correctness and concurrent access.
 * 
 * Synchronized keyword
 * 
 * Helpes in dealing with the problem of race condition, by blocking the method for one thread. 
 * Locks the critical section, where other threads can read from data but cannot perform write operation in the data.
 * 
 * 
 * Volatile Keyword
 * 
 * Ensures visibility, not atomicity.
 * 
 * Volatile keyword only ensures that the latest value of count is visible 
 * across threads.
 * 
 * By using volatile keyword, we always write into the main memory and 
 * read the data from the main memory, so that in case of synchronized keyword
 *  if the data gets changed by another thread, the first thread can read it easily using the 
 * volatile keyword.
 * 
 * Use it always only when one thread is writing and another thread is reading.
 * Power of Volatile keyword -> 1. Immediately shows the changes.
 * 2. No caching, 3. No atomicity ( values will not be stored )
 * 
 * Example ->Stock App, where users can see the changes immediately.
 * 
 * 
 * Atomic Variable ->
 * 
 * 1. Atomic Integer
 * 2. Atomic Boolean
 * 
 * Both of the, uses Compare-And-Swap method. 
 * 
 * CAS Concept -> Think of it like "If the value is what I expect, then set it to new values"
 * Prevents the race condition without locking.
 * 
 * Example -> Instagram likes.
 */

class PurchaseCounter{
    private int count = 0;

    public void increment(){
        //read, updating, writing -> not thread safe.
        count++;
    }

    public int getCount(){
        return count;
    }
}

// This is the synchronized method and is Thread safe.
class PurchasedCounterSynchronized{
    private int count = 0;

    public synchronized void increment(){
        count++;
    }

    public int getCount(){
        return count;
    }
}

//This is using the volatile keyword in java

class PurchasedCounterVolatile{
    private volatile int count = 0;

    public void increment(){
        count++;
    }

    public int getCount(){
        return count;
    }
}

//This is using the Atomic Integer

class PurchaseAtomicCounter{
    private AtomicInteger likes = new AtomicInteger(0);

    public void incrementLikes(){
        int prev, next;
        do{
            prev = likes.get();
            next = prev+1;
        }while(!likes.compareAndSet(prev, next));
    }
}

class RaceConditionDemo{
    public static void main(String args[])throws InterruptedException{
        //PurchaseCounter counter = new PurchaseCounter();
        //PurchasedCounterSynchronized counterSynchronized = new PurchasedCounterSynchronized();
        PurchasedCounterVolatile counterVolatile = new PurchasedCounterVolatile();

        Runnable task = () ->{
            for(int i=0;i<1000;i++){
                //counter.increment();
                //counterSynchronized.increment();
                counterVolatile.increment();
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();

        t2.join();
        t2.join();

        //System.out.println("Final count: "+ counter.getCount());
        //System.out.println("Synchronized method count: "+ counterSynchronized.getCount());
        System.out.println("Volatile keyword using count: "+ counterVolatile);
    }
}