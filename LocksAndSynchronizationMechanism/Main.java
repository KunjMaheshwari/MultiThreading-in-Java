package LocksAndSynchronizationMechanism;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/*

Locks ->
1. General term for mutual exclusion (mutex) mechanisms that allow only one thread to access a resource at a time.
2. Not always enforced.
3. Synchronized keyword in Java is a common way to implement locks.
4. In programing, may allow another thread to unlock.
5. Example -> Public washroom.

Mutex ->

1. A specific type of lock that ensures only one thread can access a resource at a time.
2. Only the thread that acquired the mutex can release it.
3. Only the thread locked it can unlock it.
4. Example -> Home door lock. Only the person who locked the door can unlock it.

Reentrant Lock ->
A Reentrant Lock is a synchronization mechanism in Java that allows a thread to acquire the same lock multiple times without getting blocked.


*/

class TicketBooking {
    private int availableSeats = 1;
    private final ReentrantLock lock = new ReentrantLock();

    public void bookTickets(String user) {
        System.out.println(user + " is trying to book tickets");
        lock.lock();
        // this will ensure that if any thread already acquired a lock then it will wait
        // until the lock is unlocked.
        try {
            System.out.println(user + " acquired lock");
            if (availableSeats > 0) {
                System.out.println(user + " successfully booked the ticket");
                availableSeats--;
            } else {
                System.out.println(user + " could not book the ticker. No seat left.");
            }
        } catch (Error e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println(user + " is releasing the lock");
            lock.unlock();
        }
    }
}

class ExpiringReentrantLock {
    private final ReentrantLock lock = new ReentrantLock();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private volatile boolean isLocked = false;

    public boolean tryLockWithExpiry(long timeoutMillis) {
        boolean acquired = lock.tryLock();

        if (acquired) {
            isLocked = true;

            scheduler.schedule(() -> {
                if (lock.isHeldByCurrentThread() || isLocked) {
                    System.out.println("Auto releasing the lock");
                    unlockSafely();
                }
            }, timeoutMillis, TimeUnit.MILLISECONDS);
        }
        return acquired;
    }

    public void unlockSafely() {
        if (lock.isHeldByCurrentThread() || isLocked) {
            isLocked = false;
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                System.out.println("Lock released ✅");
            }
        }
    }
}

// this try lock prevents the thread to get stuck forever.
class TicketBookingTryLock {
    private int availableSeats = 1;
    private final ReentrantLock lock = new ReentrantLock();

    public void bookTicket(String user) throws InterruptedException {
        System.out.println(user + " is trying to book the ticket");

        boolean lockAcquired = false;

        try {
            lockAcquired = lock.tryLock(2, TimeUnit.MILLISECONDS);

            if (lockAcquired) {
                System.out.println(user + " acquired lock");
                if (availableSeats > 0) {
                    System.out.println(user + " successfully booked the ticket");
                    availableSeats--;
                } else {
                    System.out.println(user + " coulr not book the ticket. No seat is left");
                }
            } else {
                System.out.println(user + " could not acquire look. Try again later.");
            }
        } catch (Error e) {
            System.out.println(e.getMessage());
        } finally {
            if (lockAcquired) {
                System.out.println(user + " is releasing the lock");
                lock.unlock();
            }
        }
    }
}

class StockData {
    private Double price = 100.0;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    // when thread is writing , no one can read and write, everyone is blocked.
    public void updatePrice(double newPrice) {
        lock.writeLock().lock();

        try {
            System.out.println(Thread.currentThread().getName() + " updating price to " + newPrice);
            price = newPrice;
        } finally {
            lock.writeLock().unlock();
        }
    }

    // when i am reading, no one is blocked.
    public void readPrice() {
        lock.readLock().lock();

        try {
            System.out.println(Thread.currentThread().getName() + " read price: " + price);
        } finally {
            lock.readLock().unlock();
        }
    }
}

// Semaphores -> Helpes us in limiting the number of threads accessing a shared
// piece of resource.

class Account {
    private final Semaphore deviceSlots;

    public Account(int maxDevice) {
        this.deviceSlots = new Semaphore(maxDevice);
    }

    public boolean login(String user) {
        System.out.println(user + " is trying to login");

        if (deviceSlots.tryAcquire()) {
            System.out.println(user + " successfully logged in");
            return true;
        } else {
            System.out.println(user + " denied login");
            return false;
        }
    }

    public void logout(String user) {
        System.out.println(user + " logged out successfully");
        deviceSlots.release();
    }
}

public class Main {
    public static void main(String args[]) {

    }
}
