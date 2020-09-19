package com.jm.online_store.model.dto;

import com.jm.online_store.enums.StockFilterType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockFilterDto {
    StockFilterType type;
    LocalDate currentDate;
}
