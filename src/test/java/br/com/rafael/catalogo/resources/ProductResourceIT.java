package br.com.rafael.catalogo.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.rafael.catalogo.resources.tests.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.rafael.catalogo.dto.ProductDTO;
import br.com.rafael.catalogo.factory.Factory;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIT {
	
	@Autowired
	private MockMvc mockMVC;
	
	
	private Long existingId;
	
	private Long noExistingId;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private Long countTotalProducts;

	@Autowired
	private TokenUtil tokenUtil;
	
	@BeforeEach
	void setUp() throws Exception{
		
		
		existingId = 1L;
		noExistingId = 100L;
		countTotalProducts = 25L;
		
	}
	
	@Test
	public void findAllShouldReturnSortedPageWhenSortyByName() throws Exception{
		ResultActions result = mockMVC.perform(get("/producties?page=0"));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.totalElements").value(countTotalProducts));
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
		result.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
		result.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));
		
		
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
		
		ProductDTO productDTO = Factory.createProductDTO();
		// converto meu objeto java num objeto JSON
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		String nomeExpectedName = productDTO.getName(); 
		String expectedDescription = productDTO.getDescription();

		ResultActions result = mockMVC.perform(
				tokenUtil.authenticatedRequest(mockMVC,
				put("/producties/{id}", existingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)));

		result.andExpect(status().isOk());

		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.name").value(nomeExpectedName));
		result.andExpect(jsonPath("$.description").value(expectedDescription));

	}

	@Test
	public void updateShouldReturnNotFoundWhenIdNotExists() throws Exception {
		
		ProductDTO productDTO = Factory.createProductDTO();
		// converto meu objeto java num objeto JSON
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		

		ResultActions result = mockMVC.perform(tokenUtil.authenticatedRequest(mockMVC,put("/producties/{id}", noExistingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)));

		result.andExpect(status().isNotFound());


	}

}
