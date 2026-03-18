package mate.academy;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TicketBookingSystem {

    private final Semaphore tickets;
    private final ExecutorService executor;

    public TicketBookingSystem(int totalSeats) {
        tickets = new Semaphore(totalSeats);
        executor = Executors.newFixedThreadPool(totalSeats);
    }

    public BookingResult attemptBooking(String user) {
        Customer customer = new Customer(user, tickets);
        Future<Boolean> future = executor.submit(customer);
        try {
            if (future.get()) {
                return new BookingResult(user, true, "Booking successful.");
            } else {
                return new BookingResult(user, false, "No seats available.");
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public class Customer implements Callable<Boolean> {
        private String name;
        private Semaphore semaphore;

        public Customer(String name, Semaphore semaphore) {
            this.name = name;
            this.semaphore = semaphore;
        }

        @Override
        public Boolean call() throws InterruptedException {
            boolean acquired = semaphore.tryAcquire(2, TimeUnit.SECONDS);
            if (!acquired) {
                System.out.println(name + " failed to acquire");
                return false;
            }
            try {
                System.out.println(name + " acquired the access");
                // critical section
                return true;
            } finally {
                System.out.println(name + " is going to release the access");
                semaphore.release();
            }
        }
    }
}
