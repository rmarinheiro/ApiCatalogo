package br.com.rafael.catalogo.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import br.com.rafael.catalogo.dto.ProductDTO;
import br.com.rafael.catalogo.repository.ProductRepository;
import br.com.rafael.catalogo.services.exceptions.EntityResourceNotFoundException;

@SpringBootTest
@Transactional
public class ProductServiceIT {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductRepository productRepository;
	
	private Long existingId;
	
	private Long noExistingId;
	
	private Long counTotalProducts;
	
	
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		noExistingId = 1000L;
		counTotalProducts = 25L;
		
	}
	
	@Test
	public void deleteShouldDeleteIDWhenIdExists() {
		
		productService.delete(existingId);
		Assertions.assertEquals(counTotalProducts - 1, productRepository.count());
		
	}
	
	@Test
	public void deleteShouldThrowEntityNotFoundExceptionWhenIdNotExists() {
		Assertions.assertThrows(EntityResourceNotFoundException.class, ()->{
			productService.delete(noExistingId);
		});
		
		
	}
	
	@Test
	public void findallPaggedShouldReturnPageWhenPage0Size10() {
		PageRequest pageRequest = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = productService.findAllPaged(pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(10, result.getSize());
		Assertions.assertEquals(counTotalProducts, result.getTotalElements());
	}
	
	@Test
	public void findallPaggedShouldReturnEmptyWhenPageDoesExist() {
		PageRequest pageRequest = PageRequest.of(50, 10);
		
		Page<ProductDTO> result = productService.findAllPaged(pageRequest);
		
		Assertions.assertTrue(result.isEmpty());
		
	}
	
	@Test
	public void findAllPagedShouldReturnSortedPageWhenSortByName() {
		PageRequest pageRequest = PageRequest.of(0, 10,Sort.by("name"));
		
		Page<ProductDTO> result = productService.findAllPaged(pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
		
	}

}
