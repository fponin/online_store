package com.jm.online_store.controller.rest;

import com.jm.online_store.model.Categories;
import com.jm.online_store.model.Product;
import com.jm.online_store.model.User;
import com.jm.online_store.service.interf.CategoriesService;
import com.jm.online_store.service.interf.ProductService;
import com.jm.online_store.service.interf.UserService;
import com.jm.online_store.util.Transliteration;
import com.jm.online_store.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import netscape.javascript.JSObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Рест контроллер главной страницы.
 */
@RestController
@RequestMapping("/")
@Slf4j
@RequiredArgsConstructor
public class MainPageRestController {

    private final UserService userService;
    private final CategoriesService categoriesService;
    private final ProductService productService;

    @PostMapping("/registration")
    @ResponseBody
    public ResponseEntity registerUserAccount(@ModelAttribute("userForm") @Validated User userForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.debug("BindingResult in registerUserAccount hasErrors: {}", bindingResult);
            return new ResponseEntity("Binding error", HttpStatus.OK);
        }
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())) {
            log.debug("Passwords do not match : passwordConfirmError");
            return ResponseEntity.badRequest().body("passwordError");
        }
        if (!ValidationUtils.isValidPassword(userForm.getPassword())) {
            log.debug("Passwords do not match : passwordValidError");
            return ResponseEntity.badRequest().body("passwordValidError");
        }
        if (userService.isExist(userForm.getEmail())) {
            log.debug("User with same email already exists");
            return new ResponseEntity("duplicatedEmailError", HttpStatus.OK);
        }
        if (!ValidationUtils.isValidEmail(userForm.getEmail())) {
            log.debug("Wrong email! Не правильно введен email");
            return ResponseEntity.ok("notValidEmailError");
        }
        userService.regNewAccount(userForm);
        return new ResponseEntity("success", HttpStatus.OK);
    }

    /**
     * Создаёт мапу - ключ - название категории, значение - мапа с названиями подкатегории.
     * Во внутренних мапах - ключ - подкатегория кириллицей и значение - латиницей.
     *
     * @return Пример: {"Компьютеры":{"Комплектующие":"Komplektuyushchiye",
     * "Компьютеры":"Kompʹyutery",
     * "Ноутбуки":"Noutbuki"},
     * "Смартфоны и гаджеты":{"Планшеты":"Planshety",
     * "Смартфоны":"Smartfony"}}
     *
     *
     * ^^^^^^^ Этот коммент от прошлой реализации возможно понадобится при рефакторинге страниц отображения подкатегорий ^^^^^^
     *
     * Сейчас метод возвращает список корневых категорий для дальнейшего формирования ссылок на подкатегории
     *
     */
    @GetMapping("api/categories")
    public ResponseEntity<List<Categories>> getMainCategories() {
        return ResponseEntity.ok(categoriesService.getCategoriesByParentCategoryId(0L));
    }

    /**
     * Возвращает список первых N продуктов - N передаётся в метод сервиса .findNumProducts(N)
     */
    @GetMapping("api/products")
    public ResponseEntity<List<Product>> getSomeProducts() {
        return ResponseEntity.ok(productService.findNumProducts(15));
    }
}
