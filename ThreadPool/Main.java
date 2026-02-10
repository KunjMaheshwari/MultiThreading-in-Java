package ThreadPool;
//Lets say we are building a ride matching application similar to the Uber.
//Every time a ride reuqest comes in, you spin a new Thread to match the rider with the Driver.

class RideMatchingService {
    public void requestRide(String riderId) {
        Thread matchThread = new Thread(() -> {
            // runnable function because it has no return type
            System.out.println("Matching rider " + riderId + " to the driver");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Ride matched successfully: " + riderId);
        });
        matchThread.start();
    }
}

// this will lead to Thread explostion, memory leak, thread termination logic,
// context switching.
// better approach is to use a Thread pool.

public class Main {
    public static void main(String args[]) {

        RideMatchingService rs1 = new RideMatchingService();
        RideMatchingService rs2 = new RideMatchingService();

        rs1.requestRide("Kunj");
        System.out.println("Task 1 is running");
        rs2.requestRide("Suhani");
        System.out.println("Task 2 is running");
    }
}