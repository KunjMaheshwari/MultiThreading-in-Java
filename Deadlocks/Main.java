package Deadlocks;

import java.util.Arrays;

/*
Deadlocks is a situtation in multithreading where two or more threads are blocked forever
each writing for the other to release the lock.

Real life analogy: Train crossing a single lane bridge from opposite sides. Both trains are waiting for the other to cross first, resulting in a deadlock.

CONDITIONS -> 
1. Mutual exclusion.
2. Hold and Wait 
3. No preemption
4. Circular wait

PREVENTION TECHNIQUES - 
1. Lock ordering( Breaks cricular wait )
2. Using tryLock() with timeout (breaks hold and wait)
3. minimize nested locking (breaks hold and wait)
4. recovery strategies ( more relevant to Databases)
*/

class BankAccount {
    private final String name;
    private int balance;

    public BankAccount(String name, int balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public synchronized void deposite(int amount) { // one operation at a time.
        balance += amount;
    }

    public synchronized void withdraw(int amount) {
        balance -= amount;
    }

    public int getBalance() {
        return balance;
    }
}

class TaskTransfer implements Runnable {
    private final BankAccount from;
    private final BankAccount to;
    private final int amount;

    public TaskTransfer(BankAccount from, BankAccount to, int amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    @Override
    public void run() {
        synchronized (from) {
            System.out.println(Thread.currentThread().getName() + " locked " + from.getName());

            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }

            synchronized (to) {
                System.out.println(Thread.currentThread().getName() + " locked " + to.getName());

                from.withdraw(amount);
                to.deposite(amount);
                System.out.println("Transferred " + amount + " from " + from.getName() + " to " + to.getName());
            }
        }
    }
}

class DeadlockSimpleExecution {
    public static void main(String args[]) throws InterruptedException {
        BankAccount accountA = new BankAccount("Account-A", 1000);
        BankAccount accountB = new BankAccount("Account-b", 1000);

        Thread t1 = new Thread(new TaskTransfer(accountA, accountB, 100), "t1");
        Thread t2 = new Thread(new TaskTransfer(accountB, accountA, 200), "t2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Finished");
    }
}

class LockOrderingSimple{
    static class Resource{
        int id;
        int value;
        public Resource(int id, int value) {
            this.id = id;
            this.value = value;
        }
    }

    public static void main(String args[]){
        Resource r1 = new Resource(101, 500);
        Resource r2 = new Resource(102, 1000);

        Runnable task1 = () -> transfer(r1, r2, 100);
        Runnable task2 = () -> transfer(r2, r1, 200);

        new Thread(task1).start();
        new Thread(task2).start();
    }

    public static void transfer(Resource a, Resource b, int amount){
        Resource[] locks = new Resource[]{a, b};
        Arrays.sort(locks, (x, y)-> Integer.compare(x.id, y.id)); //lock prevention technicque ( breaks the circular wait)
        synchronized (locks[0]) {
            synchronized (locks[1]) {
                System.out.println("Transferring " + amount + " from " + a.id + " to " + b.id);
            }
        }
    }
}



public class Main {

}
