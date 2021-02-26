package com.robertosouza.descatalog.tests.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robertosouza.descatalog.dto.ProductDTO;
import com.robertosouza.descatalog.services.ProductService;
import com.robertosouza.descatalog.services.exceptions.DatabaseException;
import com.robertosouza.descatalog.services.exceptions.ResourceNotFoundException;
import com.robertosouza.descatalog.tests.factory.ProductFactory;



@SpringBootTest
@AutoConfigureMockMvc
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService service;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Value("${security.oauth2.client.client-id}")
	private String clientId;
	
	@Value("${security.oauth2.client.client-secret}")
	private String clientSecret;
	
	private Long existingId;
	private Long nonExistingId;
	private ProductDTO newProductDTO;
	private ProductDTO existingProductDTO;
	private Long dependentId;
	private PageImpl<ProductDTO> page;
	
	private String operatorUsername;
	private String operatorPassword;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		operatorUsername = "alex@gmail.com";
		operatorPassword = "123456";
		
		
		newProductDTO = ProductFactory.createProductDTO(null);
		existingProductDTO = ProductFactory.createProductDTO(existingId);
		
		page = new PageImpl<>(List.of(existingProductDTO));
		
		when(service.findById(existingId)).thenReturn(existingProductDTO);
		when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		when(service.findAllPaged(any(), anyString(), any())).thenReturn(page);
		
		when(service.insert(any())).thenReturn(existingProductDTO);
		
		when(service.update(eq(existingId), any())).thenReturn(existingProductDTO);
		when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
		
		doNothing().when(service).delete(existingId);
		doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
		doThrow(DatabaseException.class).when(service).delete(dependentId);
	}
	
	@Test
	public void insertShouldThrowsExceptionWhenProductPriceIsegative() throws Exception {
		
		newProductDTO.setPrice(-200.0);
		
		String accessToken = obtainAccessToken(operatorUsername, operatorPassword);
		String jsonBody = objectMapper.writeValueAsString(newProductDTO);
		ResultActions result = mockMvc.perform(post("/products/")
				.header("Authorization", "Bearer " + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)				
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnprocessableEntity());
		result.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void insertShouldReturnProductDTOWhenProductIsVAlid() throws Exception {
		String accessToken = obtainAccessToken(operatorUsername, operatorPassword);
		String jsonBody = objectMapper.writeValueAsString(newProductDTO);
		ResultActions result = mockMvc.perform(post("/products/")
				.header("Authorization", "Bearer " + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)				
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
	}
	@Test
	public void deleteShouldThrowExceptionWhenIdDoesntExist() throws Exception {
		String accessToken = obtainAccessToken(operatorUsername, operatorPassword);
		ResultActions result = mockMvc.perform(delete("/products/{id}", nonExistingId)
				.header("Authorization", "Bearer " + accessToken));
		result.andExpect(status().isNotFound());
	}
	@Test
	public void deleteShouldDeleteProductWhenIdExists() throws Exception {
		String accessToken = obtainAccessToken(operatorUsername, operatorPassword);
		ResultActions result = mockMvc.perform(delete("/products/{id}", existingId)
				.header("Authorization", "Bearer " + accessToken));
		result.andExpect(status().isNoContent());
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdtExists() throws Exception{
		
		String accessToken = obtainAccessToken(operatorUsername, operatorPassword);
		
		String jsonBody = objectMapper.writeValueAsString(newProductDTO);
		
		String expectedName = newProductDTO.getName();
		Double expectedPrice = newProductDTO.getPrice();
		
		ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
				.header("Authorization", "Bearer " + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.name").value(expectedName));
		result.andExpect(jsonPath("$.price").value(expectedPrice));
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesntExist() throws Exception{
		
		String accessToken = obtainAccessToken(operatorUsername, operatorPassword);
		
		String jsonBody = objectMapper.writeValueAsString(newProductDTO);
		
		ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
				.header("Authorization", "Bearer " + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	
	@Test
	public void findAllShouldReturnPage() throws Exception {
		ResultActions result = mockMvc.perform(get("/products", existingId).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content").exists());
	}
	
	@Test
	public void findByIdShouldReturnProductWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(get("/products/{id}", existingId).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.id").value(existingId));
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		ResultActions result = mockMvc.perform(get("/products/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private String obtainAccessToken(String username, String password) throws Exception {
		 
	    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	    params.add("grant_type", "password");
	    params.add("client_id", clientId);
	    params.add("username", username);
	    params.add("password", password);
	 
	    ResultActions result 
	    	= mockMvc.perform(post("/oauth/token")
	    		.params(params)
	    		.with(httpBasic(clientId, clientSecret))
	    		.accept("application/json;charset=UTF-8"))
	        	.andExpect(status().isOk())
	        	.andExpect(content().contentType("application/json;charset=UTF-8"));
	 
	    String resultString = result.andReturn().getResponse().getContentAsString();
	 
	    JacksonJsonParser jsonParser = new JacksonJsonParser();
	    return jsonParser.parseMap(resultString).get("access_token").toString();
	}	
}