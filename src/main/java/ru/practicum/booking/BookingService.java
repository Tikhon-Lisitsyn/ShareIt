package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.item.Item;
import ru.practicum.item.ItemRepository;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Booking createBooking(Long userId, Long itemId, LocalDateTime start, LocalDateTime end) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        if (!item.getAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Item is not available");
        }

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setStatus(BookingStatus.WAITING);

        return bookingRepository.save(booking);
    }

    public Booking respondToBooking(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Item item = booking.getItem();

        if (!item.getOwner().equals(user)) {
            throw new IllegalArgumentException("Only the owner of the item can respond to the booking");
        }

        if (booking.getStatus() == BookingStatus.APPROVED || booking.getStatus() == BookingStatus.REJECTED) {
            throw new IllegalStateException("Booking has already been responded to");
        }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        }
        else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        bookingRepository.save(booking);

        return booking;
    }

    public Booking getBookingInfo(Long userId, Long bookingId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        Item item = booking.getItem();
        if (!(booking.getBooker().equals(user) || item.getOwner().equals(user))) {
            throw new IllegalArgumentException("Only the owner and the booker of the item can respond to the booking");
        }

        return booking;
    }

    public List<Booking> getBookings(Long userId) {
        return bookingRepository.findBookingsByUserId(userId);
    }


    public List<Booking> getBookingsByOwner(Long userId, String state) {
        return bookingRepository.getBookingsByOwner(userId,state);
    }
}