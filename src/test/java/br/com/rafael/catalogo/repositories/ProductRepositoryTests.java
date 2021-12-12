package br.com.rafael.catalogo.repositories;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import br.com.rafael.catalogo.entities.Product;
import br.com.rafael.catalogo.factory.Factory;
import br.com.rafael.catalogo.repository.ProductRepository;


@DataJpaTest
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository productRepository;
	
	private Long existingId;
	
	private Long nonExistsId;
	
	private Long countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistsId = 30L;
		countTotalProducts = 25L;
		
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		productRepository.deleteById(existingId);
		
		Optional<Product> prodId =  productRepository.findById(1L);
		
		Assertions.assertFalse(prodId.isPresent());
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdNotExists() {
		Assertions.assertThrows(EmptyResultDataAccessException.class, () ->{
			productRepository.deleteById(nonExistsId);
		});
	}
	
	@Test
	public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
		Product product = Factory.createProduct();
		
		product.setId(null);
		
		product = productRepository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
	}
	
	@Test
	public void findByIdShouldObjectWhenIdExists() {
		
		Optional<Product> prodId = productRepository.findById(existingId);
		Assertions.assertTrue(prodId.isPresent());
	}
	
	@Test
	public void findByIdShouldEmptyOptionalWhenIdNotExists() {
		Optional<Product> prodId = productRepository.findById(nonExistsId);
		Assertions.assertTrue(prodId.isEmpty());
		
	}
	
	

}
