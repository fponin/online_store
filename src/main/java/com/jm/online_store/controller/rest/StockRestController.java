package com.jm.online_store.controller.rest;

import com.google.gson.Gson;
import com.jm.online_store.model.Stock;
import com.jm.online_store.service.interf.StockService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

/**
 * Рест контроллер для crud операций с акциями
 */
@RestController
@AllArgsConstructor
public class StockRestController {
    private final StockService stockService;

    /**
     *  Загружает картинку для акции
     * @param file
     * @return
     */
    @PostMapping("/uploadFile")
    public void updateStockImage(@RequestParam("file") MultipartFile file) {
        stockService.updateStockImage(file);
    }

    /**
     * Метод выводит все акции
     *
     * @return List<Stock> список всех акций
     */
    @GetMapping(value = "/rest/allStocks")
    public List<Stock> findAll() {
        return stockService.findAll();
    }

    /**
     * Метод, ищет акции по id
     *
     * @param id идентификатор акции
     * @return Optiona<Stock> возвращает акцию
     */
    @GetMapping(value = "/rest/{id}")
    public Stock findStockById(@PathVariable("id") Long id) {
        return stockService.findStockById(id);
    }

    /**
     * Метод добавляет акцию
     *
     * @param stock акиця для добавления
     * @return ResponseEntity<Stock> Возвращает добавленную акцию с кодом ответа
     */
    @PostMapping(value = "/rest/addStock", consumes = "application/json")
    public ResponseEntity<Stock> addStockM(@RequestBody Stock stock) {
        stockService.addStock(stock);
        return ResponseEntity.ok().body(stock);
    }

    /**
     * Редактирует акцию
     *
     * @param stock акция для редактирования
     * @return ResponseEntity<Stock> Возвращает отредактированную акцию с кодом овтета
     */
    @PutMapping("/rest/editStock")
    public ResponseEntity<Stock> editStockM(String stock) {
        Stock newStock = new Gson().fromJson(stock, Stock.class);
        stockService.addStock(newStock);
        return ResponseEntity.ok().body(newStock);
    }

    /**
     * Метод удаления акции по идентификатору
     *
     * @param id идентификатор акции
     */
    @DeleteMapping(value = "/rest/{id}")
    public void deleteStockById(@PathVariable("id") Long id) {
        stockService.deleteStockById(id);
    }
}
