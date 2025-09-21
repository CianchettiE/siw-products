package it.uniroma3.siw.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.model.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {
    
    public List<Product> findByType(String type);
    
    public boolean existsByNameAndType(String name, String type);
}
