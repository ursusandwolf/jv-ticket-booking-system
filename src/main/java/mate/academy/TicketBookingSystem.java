package mate.academy;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TicketBookingSystem {

    private final Semaphore tickets;

    public TicketBookingSystem(int totalSeats) {
        tickets = new Semaphore(totalSeats);
    }

    public BookingResult attemptBooking(String user) {
        try {
            if (tickets.tryAcquire(100, TimeUnit.MILLISECONDS)) { // пытаемся купить билет
                System.out.println(user + ": The ticket acquired!");
                Thread.sleep(10);
                tickets.release();
                return new BookingResult(user, true, "Booking successful.");
            } else {
                System.out.println(user + ": No free seats!");
                return new BookingResult(user, false, "No seats available.");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
