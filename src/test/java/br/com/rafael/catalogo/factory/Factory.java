package br.com.rafael.catalogo.factory;

import java.time.Instant;

import br.com.rafael.catalogo.dto.CategoryDTO;
import br.com.rafael.catalogo.dto.ProductDTO;
import br.com.rafael.catalogo.entities.Category;
import br.com.rafael.catalogo.entities.Product;

public class Factory {
	
	
	public static Product createProduct() {
		Product product = new Product(1L, "IPHONE 8", "O melhor Iphone de todos os tempos", 100.00, "https://www.google.com", Instant.parse("2021-12-07T01:00:00Z"));
		product.getCategories().add(new Category(1L,"Electronics"));
		return product;
	}
	
	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
	}
	
	public static Category createCategory() {
		Category category = new Category(1L, "Tvs");
		return category;
	}
	
	public static CategoryDTO createCategoryDTO() {
		Category category = createCategory();
		return new CategoryDTO(category);
	}

}
