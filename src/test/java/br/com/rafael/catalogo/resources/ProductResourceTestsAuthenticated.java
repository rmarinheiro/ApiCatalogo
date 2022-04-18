package br.com.rafael.catalogo.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.rafael.catalogo.dto.CategoryDTO;
import br.com.rafael.catalogo.dto.ProductDTO;
import br.com.rafael.catalogo.entities.Category;
import br.com.rafael.catalogo.entities.Product;
import br.com.rafael.catalogo.resources.tests.TokenUtil;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceTestsAuthenticated {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private String operatorUsername;
	private String operatorPassword;
	private String adminUser;
	private String adminPassword;
	
	@BeforeEach
	void setup() {
		operatorUsername = "alex@gmail.com";
		operatorPassword = "123456";
		adminUser = "maria@gmail.com";
		adminPassword = "123456";
	
	}
	
	@Test
	public void insertShouldReturn201WhenOperatorLogged() throws Exception{
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, operatorUsername, operatorPassword);
		System.out.println(accessToken);
		HashSet listaCategory = new HashSet();
		listaCategory.add(new Category(2L,"Eletronics"));
		
		Product product = new Product(null, "Caneca Jaspion", "a melhor caneca do mundo", 600.0, "http://www.google.com", Instant.parse("2022-02-17T19:55:00Z"));
		
		ProductDTO productDTO= new ProductDTO(product, listaCategory);
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result  = 
				mockMvc.perform(post("/producties")
								.header("Authorization", "Bearer " + accessToken)
								.content(jsonBody)
								.contentType(MediaType.APPLICATION_JSON)
								 .accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isCreated());
		
		
				
	}
	
	@Test
	public void insertShouldReturn201WhenAdminLogged() throws Exception{
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUser, adminPassword);
		HashSet listaCategory = new HashSet<>();
		listaCategory.add(new Category(2L,"Eletronics"));
		
		Product product = new Product(null, "Caneca Java", "a melhor caneca de programação", 50.00, "http://www.google.com", Instant.parse("2022-02-21T16:42:00Z"));
		ProductDTO productDTO = new ProductDTO(product, listaCategory);
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = 
				mockMvc.perform(post("/producties")
						.header("Authorization", "Bearer " + accessToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isCreated());
	}
	
	@Test
	public void insertShouldReturn401WhenNotUserLogged () throws Exception{
		HashSet listaCategory = new HashSet<>();
		listaCategory.add(new Category(2L,"Eletronics"));
		
		Product product = new Product(null, "Caneca Java", "a melhor caneca de programação", 50.00, "http://www.google.com", Instant.parse("2022-02-21T16:42:00Z"));
		ProductDTO productDTO = new ProductDTO(product, listaCategory);
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = 
				mockMvc.perform(post("/producties")
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isUnauthorized());
		
		
		
	}

	
	@Test
	public void findAllShouldReturn200WhenNotUserLogged() throws Exception {
		
		ResultActions result = mockMvc.perform(get("/producties")
					           .contentType(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		
		
		
	}
	
	@Test
	public void insertShouldReturn422WhenUserLoggerAndNameNotBlank() throws Exception{
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUser, adminPassword);
		HashSet listaCategory = new HashSet<>();
		listaCategory.add(new Category(2L,"Eletronics"));
		
		Product product = new Product(null, "   ","a melhor caneca de programação", 50.00, "http://www.google.com", Instant.parse("2022-02-21T16:42:00Z"));
		ProductDTO productDTO = new ProductDTO(product, listaCategory);
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = 
				mockMvc.perform(post("/producties")
						.header("Authorization", "Bearer " + accessToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isUnprocessableEntity());
		
		
	}
	
	@Test
	public void insertShouldReturn422WhenUserLoggedAndPriceNotPositive() throws Exception{
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUser, adminPassword);
		HashSet listaCategory = new HashSet<>();
		listaCategory.add(new Category(2L,"Eletronics"));
		
		Product product = new Product(null, "Caneca do Java","a melhor caneca de programação", -50.00, "http://www.google.com", Instant.parse("2022-02-21T16:42:00Z"));
		ProductDTO productDTO = new ProductDTO(product, listaCategory);
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = 
				mockMvc.perform(post("/producties")
						.header("Authorization", "Bearer " + accessToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isUnprocessableEntity());
		
	}
	
	@Test
	public void insertShouldReturn422WhenUserLoggedAndDateFuture() throws Exception{
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUser, adminPassword);
		HashSet listaCategory = new HashSet<>();
		listaCategory.add(new Category(2L,"Eletronics"));
		
		Product product = new Product(null, "Caneca do Java","a melhor caneca de programação", 50.00, "http://www.google.com", Instant.parse("2023-04-21T16:42:00Z"));
		ProductDTO productDTO = new ProductDTO(product, listaCategory);
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = 
				mockMvc.perform(post("/producties")
						.header("Authorization", "Bearer " + accessToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isUnprocessableEntity());
		
	}
	
	
	

}
