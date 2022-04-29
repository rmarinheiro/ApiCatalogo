package br.com.rafael.catalogo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.rafael.catalogo.entities.Category;
import br.com.rafael.catalogo.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

	@Query(  "Select DISTINCT obj "
			+ "From Product obj INNER JOIN obj.categories cats "
			+ "where (COALESCE(:categories) IS NULL OR cats IN :categories) AND "
			+ "(LOWER(obj.name) LIKE LOWER(CONCAT('%', :name, '%')) )")
	Page<Product> find(List<Category> categories, String name, Pageable pageable);
	
	@Query("Select obj FROM Product obj JOIN FETCH obj.categories WHERE obj IN :products")
	List<Product> findProductsWithCategories(List<Product> products);

}
