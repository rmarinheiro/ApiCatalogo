package br.com.rafael.catalogo.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.rafael.catalogo.dto.ProductDTO;
import br.com.rafael.catalogo.entities.Category;
import br.com.rafael.catalogo.factory.Factory;
import br.com.rafael.catalogo.repository.CategoryRepository;
import br.com.rafael.catalogo.services.ProductService;
import br.com.rafael.catalogo.services.exceptions.DataBaseException;
import br.com.rafael.catalogo.services.exceptions.EntityNotFoundException;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

	@Autowired
	private MockMvc mocMVC;

	@MockBean
	private ProductService productService;

	private PageImpl<ProductDTO> page;

	private ProductDTO productDTO;

	private Long existingId;

	private Long nonExistingId;

	@Autowired
	private ObjectMapper objectMapper;

	private Long dependentId;

	private Category category;

	@BeforeEach
	void setUp() throws Exception {

		existingId = 1L;

		nonExistingId = 1000L;
		
		dependentId = 3L;

		productDTO = Factory.createProductDTO();

		category = Factory.createCategory();

		page = new PageImpl<>(List.of(productDTO));

		when(productService.findAllPaged(any())).thenReturn(page);

		when(productService.findById(existingId)).thenReturn(productDTO);

		when(productService.findById(nonExistingId)).thenThrow(EntityNotFoundException.class);

		when(productService.update(any(), eq(existingId))).thenReturn(productDTO);

		when(productService.update(any(), eq(nonExistingId))).thenThrow(EntityNotFoundException.class);
		
		doNothing().when(productService).delete(existingId);
		
		doThrow(EntityNotFoundException.class).when(productService).delete(nonExistingId);
		
		when(productService.insert(any())).thenReturn(productDTO);
		
		//doThrow(DataBaseException.class).when(productService).delete(dependentId);

	}

	@Test
	public void findAllShouldReturnPage() throws Exception {
		ResultActions result = mocMVC.perform(get("/producties").accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
	}

	@Test
	public void findByIdShouldReturnProductWhenIdExists() throws Exception {
		ResultActions result = mocMVC.perform(get("/producties/{id}", existingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());

		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}

	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {

		ResultActions result = mocMVC
				.perform(get("/producties/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());

	}

	@Test
	public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
		// converto meu objeto java num objeto JSON
		String jsonBody = objectMapper.writeValueAsString(productDTO);

		ResultActions result = mocMVC.perform(put("/producties/{id}", existingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());

		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());

	}

	@Test
	public void updateShouldReturnNotFoundWhenIdNotExists() throws Exception {

		// converto meu objeto java num objeto JSON
		String jsonBody = objectMapper.writeValueAsString(productDTO);

		ResultActions result = mocMVC.perform(put("/producties/{id}", nonExistingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());

		

	}
	
	@Test
	public void deleteShouldNothingWhenIdExists() throws Exception {
		ResultActions result = mocMVC.perform(delete("/producties/{id}",existingId).accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNoContent());
	}
	
	@Test
	public void deleteShouldThrowEntityNotFoundExceptionWhenIdNotExists() throws Exception {
		ResultActions result = mocMVC.perform(delete("/producties/{id}",nonExistingId).accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void insertShouldReturnProductDTO()throws Exception{
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mocMVC.perform(post("/producties").content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
		
	
				
							 	
	}

}
