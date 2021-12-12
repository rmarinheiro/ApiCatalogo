package br.com.rafael.catalogo.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.rafael.catalogo.dto.CategoryDTO;
import br.com.rafael.catalogo.dto.ProductDTO;
import br.com.rafael.catalogo.entities.Category;
import br.com.rafael.catalogo.entities.Product;
import br.com.rafael.catalogo.factory.Factory;
import br.com.rafael.catalogo.repository.CategoryRepository;
import br.com.rafael.catalogo.repository.ProductRepository;
import br.com.rafael.catalogo.services.exceptions.DataBaseException;
import br.com.rafael.catalogo.services.exceptions.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServicesTests {
	
	@InjectMocks
	private ProductService productService;
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	private long existingId ;
	
	private long nonExistingId; 
	
	private long dependtId;
	
	private PageImpl<Product> page;
	
	private Product product; 
	
	private Category category;
	
	private ProductDTO productDTO;
	
	private CategoryDTO categoryDTO;
	
	@BeforeEach
	void setUp() throws Exception {
		
		existingId = 1L;
		
		nonExistingId = 1000L;
		
		dependtId = 4L;
		
		product = Factory.createProduct();
		
		page = new PageImpl<>(List.of(product));
		
		productDTO = Factory.createProductDTO();
		
		//categoryDTO = Factory.createCategoryDTO();
		
		category = Factory.createCategory();
		
		
		Mockito.doNothing().when(productRepository).deleteById(existingId);
		
		Mockito.doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(nonExistingId);
		
		Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependtId);
		
		Mockito.when(productRepository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		
		Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);
		
		Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
		
		Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.when(productRepository.getById(existingId)).thenReturn(product);
		
		Mockito.when(productRepository.getById(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(categoryRepository.getById(existingId)).thenReturn(category);
		
		Mockito.when(categoryRepository.getById(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		
		
		//Mockito.when(productService.findById(existingId)).thenReturn(productDTO);
		
		
	}
	
	@Test
	public void findAllPagedShouldReturnPage() {
		
		PageRequest pageable = PageRequest.of(0, 10);
		Page<ProductDTO> result = productService.findAllPaged(pageable);
		
		Assertions.assertNotNull(result);
		
		Mockito.verify(productRepository).findAll(pageable);
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(()->{
			productService.delete(existingId);
		});
		
		Mockito.verify(productRepository,Mockito.times(1)).deleteById(existingId);
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(EntityNotFoundException.class,() ->{
			productService.delete(nonExistingId);
		});
		
		Mockito.verify(productRepository,Mockito.times(1)).deleteById(nonExistingId);
	}
	
	@Test
	public void deleteShouldThrowDataBaseExceptionWhenDependentId() {
		Assertions.assertThrows(DataBaseException.class, ()->{
			productService.delete(dependtId);
		});
		
		Mockito.verify(productRepository).deleteById(dependtId);
	}
	
	@Test
	public void findByIdShouldProductDTOWhenIdExists() {
		
		ProductDTO productDTO = productService.findById(existingId);
		
		Assertions.assertNotNull(productDTO);
		
		Mockito.verify(productRepository).findById(existingId);
	}
	
	@Test
	public void findByIdShouldThrowEntityNotFoundExceptionWhenIdNotExists() {
		Assertions.assertThrows(EntityNotFoundException.class, ()->{
			productService.findById(nonExistingId);
		});
		
		Mockito.verify(productRepository).findById(nonExistingId);
	}
	
	@Test
	public void updateShoulReturnProductWhenIdExists() {
		
		productDTO = Factory.createProductDTO();
		
		ProductDTO productdto = productService.update(productDTO, existingId);
		
		Assertions.assertNotNull(productdto);
		
		//Mockito.verify(productRepository).getById(existingId);

		//Mockito.verify(categoryRepository).getById(existingId);
		
	}
	
	@Test
	public void updateShouldThrowEntityNotFoundExceptionWhenIdNotExists() {
		Assertions.assertThrows(EntityNotFoundException.class, ()->{
			productService.update(productDTO, nonExistingId);
		});
		
		//Mockito.verify(categoryRepository).getById(nonExistingId);
	}
	

}
