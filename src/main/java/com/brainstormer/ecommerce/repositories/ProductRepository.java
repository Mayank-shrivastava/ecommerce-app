package com.brainstormer.ecommerce.repositories;

import com.brainstormer.ecommerce.schema.Category;
import com.brainstormer.ecommerce.schema.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryName(String name);

    @Query("SELECT DISTINCT p.category FROM Product p")
    List<Category> findDistinctCategory();

//    @Query(nativeQuery = true,
//            value =
//                    "SELECT p.*, c.name as category FROM products p INNER JOIN categories c on p.category_id = c.id where p.id=:id")
//    List<Product> findProductWithDetailsById(Long id);

    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.id = :id") // hibernate query language is being used
    List<Product> findProductWithDetailsById(Long id);

    // we can't write direct table name in case of hibernate query language we need write the relational object name
}
