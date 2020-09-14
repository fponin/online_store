package com.jm.online_store.service.interf;

import com.jm.online_store.model.Product;
import com.jm.online_store.model.User;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    Optional<Product> findProductById(Long productId);

    Optional<Product> findProductByName(String productName);

    Long saveProduct(Product product);

    void deleteProduct(Long idProduct);

    void restoreProduct(Long idProduct);

    List<Product> findAll();

    void importFromXMLFile(String fileName);

    void importFromCSVFile(String fileName) throws FileNotFoundException;
}
