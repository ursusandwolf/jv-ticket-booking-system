package mate.academy;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TicketBookingSystem {

    public static final int THREAD_POOL = 4;
    private final Semaphore tickets;
    private final ExecutorService executor;

    public TicketBookingSystem(int totalSeats) {
        tickets = new Semaphore(totalSeats);
        executor = Executors.newFixedThreadPool(THREAD_POOL);
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
            System.out.println(name + " acquired the access");
            return true;
        }
    }
}
