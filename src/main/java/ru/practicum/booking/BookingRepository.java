package ru.practicum.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId ORDER BY b.start DESC")
    List<Booking> findBookingsByUserId(@Param("userId") Long userId);



    @Query("SELECT b FROM Booking b " +
            "JOIN b.item i " +
            "WHERE i.owner = :userId AND " +
            "(:state = 'ALL' OR b.status = :state) " +
            "ORDER BY b.start DESC")
    List<Booking> getBookingsByOwner(@Param("userId") Long userId, @Param("state") String state);

}