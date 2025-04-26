package marcel.weatherapp2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        City city = new City("Liberec", new State("Česko"));
        city.setId(1L);

        Measurement measurement = new Measurement(18.5, 65.0, LocalDateTime.now(), city);
        measurement.setId(1L);

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
        City city = new City("Brno", new State("CZ"));
        city.setId(2L);
        Measurement m1 = new Measurement(20.0, 70.0, LocalDateTime.now(), city);
        m1.setId(1L);
        Measurement m2 = new Measurement(22.0, 60.0, LocalDateTime.now(), city);
        m2.setId(2L);

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
        City city = new City("Plzeň", new State("CZ"));
        city.setId(3L);
        Measurement m = new Measurement(15.5, 55.5, LocalDateTime.now(), city);
        m.setId(5L);

        when(measurementService.getAllMeasurements()).thenReturn(List.of(m));

        mockMvc.perform(get("/api/measurement/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].temperature").value(15.5));
    }

    @Test
    void shouldUpdateMeasurement() throws Exception {
        City city = new City("Praha", new State("CZ"));
        city.setId(4L);
        Measurement updated = new Measurement(16.0, 50.0, LocalDateTime.now(), city);
        updated.setId(99L);

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
}
