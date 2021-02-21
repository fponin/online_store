package com.jm.online_store.service.interf;

import com.jm.online_store.model.Characteristic;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CharacteristicService {

    Characteristic saveCharacteristic(Characteristic characteristic);

    Optional<Characteristic> findCharacteristicById(Long id);

    Optional<Characteristic> findCharacteristicByName(String cahracteristicName);

    List<Characteristic> findByCategoryId(Long categoryId);

    List<Characteristic> findByCategoryName(String category);

    List<Characteristic> findAll();

    Characteristic updateCharacteristic(Characteristic characteristic);

    void deleteByID(Long id);

    @Transactional
    void deleteByIDInSelectedCategory(Long id, String category);

    Optional<Characteristic> findByCharacteristicName(String characteristicName);

    Characteristic getCharacteristicById(Long id);

    List<Characteristic> getAllCharacteristicsExceptSelectedCategory(String categoryName);

    List<Characteristic> addCharacteristicsToCategory(List<Characteristic> characteristics, String categoryName);
}
