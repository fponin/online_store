package com.jm.online_store.repository;

import com.jm.online_store.model.FavouritesGroup;
import com.jm.online_store.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FavouritesGroupRepository extends JpaRepository<FavouritesGroup, Long> {
    void deleteById(Long id);
    Optional<FavouritesGroup> findByName(String name);
    @Query("from FavouritesGroup f where f.user = :user")
    List<FavouritesGroup> findAllByUser(User user);
}
