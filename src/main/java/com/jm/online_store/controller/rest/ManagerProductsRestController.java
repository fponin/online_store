package com.jm.online_store.controller.rest;


import com.jm.online_store.model.Product;
import com.jm.online_store.service.interf.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Optional;

/**
 * Контроллер для работы с добавлением/изменением товаров
 */
@RestController
@AllArgsConstructor
@Slf4j
public class ManagerProductsRestController {

    private final ProductService productService;

    /**
     * Метод обрабатывает загрузку файла с товарами на сервер
     * Вызывает соответствующий сервисный метод в зависимости от типа файла(CSV или XML)
     * @param file файл с данными
     * @return
     */
    @PostMapping(value = "/rest/products/uploadProductsFile")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) throws FileNotFoundException {
        try {
            byte[] bytes = file.getBytes();
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(new File("uploads/import/" + file.getOriginalFilename())));
            stream.write(bytes);
            stream.close();
        } catch (Exception e) {
            log.error("Ошибка сохранения файла");
            e.printStackTrace();
        }
        log.debug("тип файла" + getFileExtension(file.getOriginalFilename()));
        if(getFileExtension(getFileExtension(file.getOriginalFilename())).equals(".xml")){
            productService.importFromXMLFile(file.getOriginalFilename());
        }else {
            productService.importFromCSVFile(file.getOriginalFilename());
        }
       return ResponseEntity.ok().body("sucess");
    }

    /**
     * Метод-сепаратор, возвращающий расширение файла
     * @param myFileName имя файла
     */
    private static String getFileExtension(String myFileName) {
        int index = myFileName.indexOf('.');
        return index == -1? null : myFileName.substring(index);
    }

    /**
     * Метод выводит список всех товаров
     * @return List<Product> возвращает список товаров
     */
    @GetMapping(value = "/rest/products/allProducts")
    public List<Product> findAll() {
        return productService.findAll();
    }

    /**
     * Метод, ищет акции по id
     * @param productId идентификатор товара
     * @return Optiona<Product> возвращает товар
     */
    @GetMapping(value = "/rest/products/{id}")
    public Optional<Product> findProductById(@PathVariable("id") Long productId) {
        return productService.findProductById(productId);
    }

    /**
     * Метод добавляет акцию
     * @param product акиця для добавления
     * @return ResponseEntity<Product> Возвращает добавленную акцию с кодом ответа
     */
    @PostMapping(value = "/rest/products/addProduct", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Product> addProductM(@RequestBody Product product) {
        productService.saveProduct(product);
        return ResponseEntity.ok().body(product);
    }

    /**
     * Редактирует акцию
     * @param product акция для редактирования
     * @return ResponseEntity<Product> Возвращает отредактированный товар с кодом ответа
     */
    @PutMapping("/rest/products/editProduct")
    public ResponseEntity<Product> editProductM(@RequestBody Product product) {
        productService.saveProduct(product);
        return ResponseEntity.ok().body(product);
    }

    /**
     * Метод удаления акции по идентификатору
     * @param id идентификатор акции
     */
    @DeleteMapping(value = "/rest/products/{id}")
    public void deleteStockById(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
    }
}
