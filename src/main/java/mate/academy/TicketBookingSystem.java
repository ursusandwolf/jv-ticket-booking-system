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
                return new BookingResult(user, true, "The ticket acquired!");
            } else {
                System.out.println(user + ": No free seats!");
                return new BookingResult(user, false, "No free seats!");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            tickets.release();
        }
    }
}
