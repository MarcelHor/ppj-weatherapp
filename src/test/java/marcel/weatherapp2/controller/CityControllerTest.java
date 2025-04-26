package marcel.weatherapp2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import marcel.weatherapp2.dto.CityCreateDto;
import marcel.weatherapp2.model.City;
import marcel.weatherapp2.model.State;
import marcel.weatherapp2.service.CityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CityController.class)
class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CityService cityService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnCityById() throws Exception {
        State state = new State("Česko");
        state.setId(1L);
        City city = new City("Liberec", state);
        city.setId(10L);

        when(cityService.findById(10L)).thenReturn(city);

        mockMvc.perform(get("/api/city/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Liberec"))
                .andExpect(jsonPath("$.stateId").value(1))
                .andExpect(jsonPath("$.stateName").value("Česko"));
    }

    @Test
    void shouldReturnAllCities() throws Exception {
        State state = new State("Česko");
        state.setId(1L);
        City c1 = new City("Liberec", state); c1.setId(10L);
        City c2 = new City("Brno", state); c2.setId(11L);

        when(cityService.findAll()).thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/api/city/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Liberec"))
                .andExpect(jsonPath("$[1].name").value("Brno"));
    }

    @Test
    void shouldCreateCity() throws Exception {
        State state = new State("Česko"); state.setId(1L);
        City created = new City("Ostrava", state); created.setId(12L);

        when(cityService.save(any(CityCreateDto.class))).thenReturn(created);

        mockMvc.perform(post("/api/city")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CityCreateDto("Ostrava", 1L))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(12))
                .andExpect(jsonPath("$.name").value("Ostrava"))
                .andExpect(jsonPath("$.stateId").value(1));
    }

    @Test
    void shouldUpdateCity() throws Exception {
        State state = new State("Česko"); state.setId(1L);
        City updated = new City("Plzeň", state); updated.setId(13L);

        when(cityService.update(eq(13L), any(CityCreateDto.class))).thenReturn(updated);

        mockMvc.perform(put("/api/city/13")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CityCreateDto("Plzeň", 1L))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Plzeň"))
                .andExpect(jsonPath("$.stateId").value(1));
    }

    @Test
    void shouldDeleteCity() throws Exception {
        doNothing().when(cityService).delete(15L);

        mockMvc.perform(delete("/api/city/15"))
                .andExpect(status().isOk());
    }
}