package it.uniroma3.siw.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Product;
import it.uniroma3.siw.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Transactional
    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }
    
    @Transactional
    public List<Product> findByType(String type) {
        return productRepository.findByType(type);
    }

    @Transactional
    public void save(Product product) {
        productRepository.save(product);
    }
    
    @Transactional
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional
    public List<Product> findProductsNotInSimilar(Product product) {
        List<Product> allProducts = StreamSupport.stream(findAll().spliterator(), false).collect(Collectors.toList());
        allProducts.removeAll(product.getSimilarProducts());
        allProducts.remove(product); // Rimuove il prodotto stesso dalla lista
        return allProducts;
    }
}
