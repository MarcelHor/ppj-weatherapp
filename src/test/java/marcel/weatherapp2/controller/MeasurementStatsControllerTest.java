package marcel.weatherapp2.controller;

import marcel.weatherapp2.dto.CityAverageDto;
import marcel.weatherapp2.dto.StateAverageDto;
import marcel.weatherapp2.service.MeasurementStatsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MeasurementStatsController.class)
class MeasurementStatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MeasurementStatsService service;

    @Test
    void shouldReturnCityAverages() throws Exception {
        CityAverageDto dto = new CityAverageDto(1L, "Liberec", 10, 9, 8, 70, 65, 60);
        when(service.getCityAverages(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/measurement/average/city/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cityId").value(1))
                .andExpect(jsonPath("$.cityName").value("Liberec"))
                .andExpect(jsonPath("$.averageTempDay").value(10.0))
                .andExpect(jsonPath("$.averageHumidityWeek").value(65.0));
    }

    @Test
    void shouldReturnStateAverages() throws Exception {
        StateAverageDto dto = new StateAverageDto(2L, "Česko", 12, 11, 10, 75, 70, 65);
        when(service.getStateAverages(2L)).thenReturn(dto);

        mockMvc.perform(get("/api/measurement/average/state/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stateId").value(2))
                .andExpect(jsonPath("$.stateName").value("Česko"))
                .andExpect(jsonPath("$.averageTempWeek").value(11.0))
                .andExpect(jsonPath("$.averageHumidityTwoWeeks").value(65.0));
    }
}
