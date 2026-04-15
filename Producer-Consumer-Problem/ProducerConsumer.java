class CoffeeMachine{
    private boolean isCoffeeReady = false;

    public synchronized void makeCoffee() throws InterruptedException {
        while(isCoffeeReady){
            wait();
        }

        System.out.println("Coffee is being prepared...");
        Thread.sleep(1000);

        isCoffeeReady = true;
        notifyAll();
    }

    public synchronized void drinkCoffee() throws InterruptedException {
        while(!isCoffeeReady){
            wait();
        }

        System.out.println("Drinking the coffee...");
        Thread.sleep(1000);

        isCoffeeReady = false;
        notifyAll();
    }
}

public class ProducerConsumer{
    public static void main(String args[]){
        CoffeeMachine coffeeMachine = new CoffeeMachine();

        Thread producerThread = new Thread(() -> {
            while(true){
                try{
                    coffeeMachine.makeCoffee();
                }catch(InterruptedException e){
                    System.out.println(e.getMessage());
                }
            }
        });

        Thread consumerThread = new Thread(() -> {
            while(true){
                try{
                    coffeeMachine.drinkCoffee();
                }catch(InterruptedException e){
                    System.out.println(e.getMessage());
                }
            }
        });

        producerThread.start();
        consumerThread.start();
    }
}

//Note that the while loop in the makeCoffee() and drinkCoffee() methods protects against spurious wake-ups and re-checks condition after every resume.