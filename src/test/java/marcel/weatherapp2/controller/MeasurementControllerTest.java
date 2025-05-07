package marcel.weatherapp2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import marcel.weatherapp2.dto.CityAverageDto;
import marcel.weatherapp2.dto.MeasurementDto;
import marcel.weatherapp2.model.City;
import marcel.weatherapp2.model.Measurement;
import marcel.weatherapp2.model.State;
import marcel.weatherapp2.service.MeasurementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MeasurementController.class)
class MeasurementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MeasurementService measurementService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetCurrentMeasurement() throws Exception {
        City city = new City(1L,"Liberec", new State("Česko"));

        Measurement measurement = new Measurement(1L,18.5, 65.0, LocalDateTime.now(), city);

        when(measurementService.getMeasurement(1L)).thenReturn(measurement);

        mockMvc.perform(get("/api/measurement/current/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.temperature").value(18.5))
                .andExpect(jsonPath("$.humidity").value(65.0))
                .andExpect(jsonPath("$.cityName").value("Liberec"))
                .andExpect(jsonPath("$.stateName").value("Česko"));
    }

    @Test
    void shouldGetMeasurementsInRange() throws Exception {
        City city = new City(2L,"Brno", new State("CZ"));
        Measurement m1 = new Measurement(1L,20.0, 70.0, LocalDateTime.now(), city);

        Measurement m2 = new Measurement(2L,22.0, 60.0, LocalDateTime.now(), city);

        when(measurementService.getMeasurements(eq(2L), any(), any())).thenReturn(List.of(m1, m2));

        mockMvc.perform(get("/api/measurement/range")
                        .param("cityId", "2")
                        .param("from", "2025-04-20T00:00:00")
                        .param("to", "2025-04-25T23:59:59"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void shouldGetAllMeasurements() throws Exception {
        City city = new City(3L,"Plzeň", new State("CZ"));
        Measurement m = new Measurement(5L,15.5, 55.5, LocalDateTime.now(), city);

        when(measurementService.getAllMeasurements()).thenReturn(List.of(m));

        mockMvc.perform(get("/api/measurement/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].temperature").value(15.5));
    }

    @Test
    void shouldUpdateMeasurement() throws Exception {
        City city = new City(4L,"Praha", new State("CZ"));
        Measurement updated = new Measurement(99L,16.0, 50.0, LocalDateTime.now(), city);

        when(measurementService.updateMeasurement(eq(99L), any(MeasurementDto.class))).thenReturn(updated);

        MeasurementDto input = new MeasurementDto(updated);

        mockMvc.perform(put("/api/measurement/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.temperature").value(16.0));
    }

    @Test
    void shouldDeleteMeasurement() throws Exception {
        doNothing().when(measurementService).deleteMeasurement(55L);

        mockMvc.perform(delete("/api/measurement/55"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnCityAverages() throws Exception {
        CityAverageDto dto = new CityAverageDto(
                1L, "Liberec",
                10.0, 9.0, 8.0,
                70.0, 65.0, 60.0
        );

        when(measurementService.getCityAverages(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/measurement/city/1/avg"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cityId").value(1))
                .andExpect(jsonPath("$.cityName").value("Liberec"))
                .andExpect(jsonPath("$.averageTempDay").value(10.0))
                .andExpect(jsonPath("$.averageTempWeek").value(9.0))
                .andExpect(jsonPath("$.averageTempTwoWeeks").value(8.0))
                .andExpect(jsonPath("$.averageHumidityDay").value(70.0))
                .andExpect(jsonPath("$.averageHumidityWeek").value(65.0))
                .andExpect(jsonPath("$.averageHumidityTwoWeeks").value(60.0));
    }

}
