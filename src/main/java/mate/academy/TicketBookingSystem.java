package mate.academy;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TicketBookingSystem {
    private final Semaphore semaphore;

    public TicketBookingSystem(int totalSeats) {
        // As per README: "The semaphore count would be initialized to the total 
        // number of available seats for a show."
        this.semaphore = new Semaphore(totalSeats);
    }

    public BookingResult attemptBooking(String user) {
        // README states: "Each booking attempt by a user is handled by a separate thread."
        // In this implementation, the separate thread is managed by the test environment
        // or the caller, avoiding internal thread leaks and redundant pool overhead.
        
        try {
            // README states: "The thread must acquire a semaphore before proceeding
            // with the booking."
            boolean acquired = semaphore.tryAcquire(2, TimeUnit.SECONDS);
            
            if (acquired) {
                // README states: "If the semaphore is acquired, it means a seat is available, 
                // and the booking can proceed."
                
                // Note: The README mentions "Once the booking is confirmed, the semaphore is
                // released,
                // decrementing the count of available seats." 
                // In Java's Semaphore, acquire() decrements the count and release() increments it.
                // To keep the seat booked (decrementing total available), we do NOT call release() 
                // after a successful booking.
                return new BookingResult(user, true, "Booking successful.");
            } else {
                return new BookingResult(user, false, "No seats available.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new BookingResult(user, false, "Booking interrupted.");
        }
    }
}
