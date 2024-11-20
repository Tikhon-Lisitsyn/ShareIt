package ru.practicum.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
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

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
            "FROM Booking b WHERE b.item.id = :itemId AND b.booker.id = :userId " +
            "AND b.status = :status AND b.end < :currentDate")
    boolean existsByItemIdAndBookerIdAndStatusAndEndDateBefore(Long itemId, Long userId,
                                                               BookingStatus status, LocalDateTime currentDate);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.end < :currentDate " +
            "AND b.status = :status ORDER BY b.end DESC")
    Booking findFirstByItemIdAndEndBeforeAndStatusOrderByEndDesc(
            Long itemId, LocalDateTime currentDate, BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.start > :currentDate " +
            "AND b.status = :status ORDER BY b.start ASC")
    Booking findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(
            Long itemId, LocalDateTime currentDate, BookingStatus status);


}