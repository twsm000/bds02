package com.devsuperior.bds02.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CityControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	//Link of discussion: https://github.com/spring-projects/spring-boot/issues/5993
	public void deleteShouldReturnBadRequestWhenDependentId() throws Exception {

		Long dependentId = 1L;

		ResultActions result =
				mockMvc.perform(delete("/cities/{id}", dependentId));

		result.andExpect(status().isBadRequest());
	}
	
	@Test
	public void deleteShouldReturnNoContentWhenIndependentId() throws Exception {		
		
		Long independentId = 5L;
		
		ResultActions result =
				mockMvc.perform(delete("/cities/{id}", independentId));
		
		
		result.andExpect(status().isNoContent());
	}

	/**
	 * This test is here only to validate the @Transactional level
	 * witch should delete correctly the independentId
	 * @throws Exception
	 */
	@Test
	public void deleteShouldReturnNoContentWhenIndependentId2() throws Exception {

		Long independentId = 5L;

		ResultActions result =
				mockMvc.perform(delete("/cities/{id}", independentId));


		result.andExpect(status().isNoContent());
	}

	@Test
	public void deleteShouldReturnNotFoundWhenNonExistingId() throws Exception {		

		Long nonExistingId = 50L;
		
		ResultActions result =
				mockMvc.perform(delete("/cities/{id}", nonExistingId));

		result.andExpect(status().isNotFound());
	}

	@Test
	public void findAllShouldReturnAllResourcesSortedByName() throws Exception {
		
		ResultActions result =
				mockMvc.perform(get("/cities")
					.contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$[0].name").value("Belo Horizonte"));
		result.andExpect(jsonPath("$[1].name").value("Belém"));
		result.andExpect(jsonPath("$[2].name").value("Brasília"));
	}
}
