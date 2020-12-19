package com.jm.online_store.repository;

import com.jm.online_store.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>, JpaSpecificationExecutor<News> {

    List<News> findAll();

    Optional<News> findById(long id);

    boolean existsById(Long id);

    List<News> findAllByPostingDateBeforeAndArchivedEquals(LocalDate timeNow, boolean archived);

    List<News> findAllByPostingDateAfterAndArchivedEquals(LocalDate timeNow, boolean archived);

    List<News> findAllByArchivedEquals(boolean archived);
}