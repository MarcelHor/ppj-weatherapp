package marcel.weatherapp2.service;

import marcel.weatherapp2.dto.CityAverageDto;
import marcel.weatherapp2.dto.MeasurementDto;
import marcel.weatherapp2.model.City;
import marcel.weatherapp2.model.Measurement;
import marcel.weatherapp2.model.State;
import marcel.weatherapp2.repository.CityRepository;
import marcel.weatherapp2.repository.MeasurementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MeasurementServiceTest {

    @Mock
    private MeasurementRepository measurementRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MeasurementService measurementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFetchAndSaveMeasurement() {
        City city = new City(1L,"Liberec", new State("Česko"));

        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

        String mockApiResponse = """
        {
            "main": {
                "temp": 18.5,
                "humidity": 60
            }
        }
        """;

        when(restTemplate.getForEntity(anyString(), eq(String.class)))
                .thenReturn(ResponseEntity.ok(mockApiResponse));

        when(measurementRepository.save(any(Measurement.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Measurement result = measurementService.getMeasurement(1L);

        assertEquals(18.5, result.getTemperature());
        assertEquals(60, result.getHumidity());
        assertEquals("Liberec", result.getCity().getName());
    }

    @Test
    void shouldGetMeasurementsBetweenDates() {
        Measurement m1 = new Measurement(20.0, 50.0, LocalDateTime.now(), null);
        Measurement m2 = new Measurement(22.0, 55.0, LocalDateTime.now(), null);

        when(measurementRepository.findByCityIdAndTimestampBetween(eq(1L), any(), any()))
                .thenReturn(List.of(m1, m2));

        List<Measurement> measurements = measurementService.getMeasurements(1L,
                LocalDateTime.now().minusDays(1), LocalDateTime.now());

        assertEquals(2, measurements.size());
    }

    @Test
    void shouldGetAllMeasurements() {
        Measurement m = new Measurement(18.0, 60.0, LocalDateTime.now(), null);
        when(measurementRepository.findAll()).thenReturn(List.of(m));

        List<Measurement> measurements = measurementService.getAllMeasurements();

        assertEquals(1, measurements.size());
    }

    @Test
    void shouldDeleteMeasurement() {
        when(measurementRepository.existsById(1L)).thenReturn(true);
        doNothing().when(measurementRepository).deleteById(1L);

        measurementService.deleteMeasurement(1L);

        verify(measurementRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldUpdateMeasurement() {
        Measurement existing = new Measurement(5L,15.0, 50.0, LocalDateTime.now(), null);

        when(measurementRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(measurementRepository.save(any(Measurement.class))).thenAnswer(inv -> inv.getArgument(0));

        MeasurementDto updateDto = new MeasurementDto(5L, 22.5, 55.0, LocalDateTime.now(), "Liberec", "Česko");
        Measurement updated = measurementService.updateMeasurement(5L, updateDto);

        assertEquals(22.5, updated.getTemperature());
        assertEquals(55.0, updated.getHumidity());
    }

    @Test
    void shouldReturnCityAverages() {
        City city = new City(1L, "Liberec", new State("Česko"));
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

        when(measurementRepository.getAverageTemperatureByCitySince(eq(1L), any()))
                .thenReturn(10.0);
        when(measurementRepository.getAverageHumidityByCitySince(eq(1L), any()))
                .thenReturn(70.0);

        CityAverageDto result = measurementService.getCityAverages(1L);

        assertEquals(1L, result.cityId());
        assertEquals("Liberec", result.cityName());
        assertEquals(10.0, result.averageTempDay());
        assertEquals(10.0, result.averageTempWeek());
        assertEquals(10.0, result.averageTempTwoWeeks());
        assertEquals(70.0, result.averageHumidityDay());
        assertEquals(70.0, result.averageHumidityWeek());
        assertEquals(70.0, result.averageHumidityTwoWeeks());
    }

}
