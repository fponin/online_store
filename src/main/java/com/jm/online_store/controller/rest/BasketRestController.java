package com.jm.online_store.controller.rest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jm.online_store.exception.ProductNotFoundException;
import com.jm.online_store.exception.ProductsNotFoundException;
import com.jm.online_store.exception.UserNotFoundException;
import com.jm.online_store.model.SubBasket;
import com.jm.online_store.service.interf.BasketService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Рест контроллер для корзины.
 */
@AllArgsConstructor
@RestController
public class BasketRestController {
    private final BasketService basketService;

    /**
     * контроллер для получения товаров в корзине для авторизованного User.
     *
     * @return ResponseEntity<> список товаров данного User + статус ответа.
     */
    @GetMapping(value = "/customer/basketGoods")
    public ResponseEntity<List<SubBasket>> getBasket() {
        List<SubBasket> subBaskets = basketService.getBasket();
        return new ResponseEntity<>(subBaskets, HttpStatus.OK);
    }

    /**
     * контроллер для формирования заказа из корзины.
     *
     * @param id адрес с формы
     * @return ResponseEntity.ok()
     */
    @PostMapping(value = "/customer/basketGoods")
    public ResponseEntity<String> buildOrderFromBasket(@RequestBody Long id) {
        basketService.buildOrderFromBasket(id);
        return ResponseEntity.ok().build();
    }

    /**
     * контроллер для удаления сущности SubBasket из списка подкорзин User.
     *
     * @param id идентификатор подкорзины
     * @return ResponseEntity.ok()
     */
    @DeleteMapping(value = "/customer/basketGoods")
    public ResponseEntity<String> deleteBasket(@RequestBody Long id) {
        basketService.deleteBasket(basketService.findBasketById(id));
        return ResponseEntity.ok().build();
    }

    /**
     * контроллер для обновления количества товара в подкорзине.
     *
     * @param json json из 2-х параметров
     * @return ResponseEntity.ok()
     */
    @PutMapping(value = "/customer/basketGoods")
    public ResponseEntity<String> updateUpBasket(@RequestBody ObjectNode json) {
        Long id = json.get("id").asLong();
        int difference = json.get("count").asInt();
        try {
            basketService.updateBasket(basketService.findBasketById(id), difference);
            return ResponseEntity.ok().build();
        }catch (ProductsNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping("/api/basket/add/{id}")
    public ResponseEntity<String> addProductToBasket(@PathVariable Long id) {
        try {
            basketService.addProductToBasket(id);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException | ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ProductsNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
