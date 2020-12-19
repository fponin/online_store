package com.jm.online_store.repository;

import com.jm.online_store.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long>, JpaSpecificationExecutor<Stock> {

    Optional<Stock> findById(Long id);

    void deleteStockById(Long id);

    /**
     * Current and future stocks
     * @param beginningOfPeriod beginning of period
     * @param endOfPeriod end of period
     * @param currentDate current date
     * @return list of current and future stocks
     */
    List<Stock> findAllByStartDateBetweenAndEndDateIsAfter(LocalDate beginningOfPeriod, LocalDate endOfPeriod, LocalDate currentDate);

    /**
     * Current stocks
     * @param currentDate1 current date
     * @param currentDate2 current date
     * @return list of current stocks
     */
    List<Stock> findByStartDateLessThanEqualAndEndDateGreaterThanEqualOrEndDateEquals(LocalDate currentDate1, LocalDate currentDate2, LocalDate currentDate3);

    /**
     * Future stocks
     * @param currentDate current date
     * @return list of future stocks
     */
    List<Stock> findByStartDateAfter(LocalDate currentDate);

    List<Stock> findByEndDateBefore(LocalDate currentDate);

    List<Stock> findAllByPublishedIsTrueAndEndDateAfterOrEndDateEquals(LocalDate endDate, LocalDate endDate2);

    /**
     * Published stocks
     * @return list of published stocks
     */
    @Query("SELECT s FROM  Stock s WHERE s.published=true ")
    List<Stock> findPublishedStocks();
}
