package mate.academy;

import java.util.concurrent.Semaphore;

public class TicketBookingSystem {

    private final Semaphore tickets;

    public TicketBookingSystem(int totalSeats) {
        tickets = new Semaphore(totalSeats);
    }

    public BookingResult attemptBooking(String user) {
        if (tickets.tryAcquire()) { // пытаемся купить билет
            System.out.println("Клиент " + user + " купил билет");
            return new BookingResult(user, true, "The ticket acquired!");
        } else {
            System.out.println("Клиент " + user + " НЕ смог купить билет (нет мест)");
            return new BookingResult(user, false, "No free seats!");
        }
    }
}
