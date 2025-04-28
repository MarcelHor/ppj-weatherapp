package marcel.weatherapp2.service;

import marcel.weatherapp2.model.City;
import marcel.weatherapp2.model.Measurement;
import marcel.weatherapp2.model.State;
import marcel.weatherapp2.repository.CityRepository;
import marcel.weatherapp2.repository.MeasurementRepository;
import marcel.weatherapp2.repository.StateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MeasurementStatsServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private StateRepository stateRepository;

    @Mock
    private MeasurementRepository measurementRepository;

    @InjectMocks
    private MeasurementStatsService measurementStatsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnCityAverages() {
        City city = new City(1L,"Liberec", new State("Česko"));

        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        when(measurementRepository.findByCityIdAndTimestampAfter(eq(1L), any(LocalDateTime.class)))
                .thenReturn(List.of(
                        new Measurement(20.0, 60.0, LocalDateTime.now(), city),
                        new Measurement(22.0, 65.0, LocalDateTime.now(), city)
                ));

        var averages = measurementStatsService.getCityAverages(1L);

        assertEquals(1L, averages.cityId());
        assertEquals("Liberec", averages.cityName());
        assertTrue(averages.averageTempDay() > 0);
        assertTrue(averages.averageHumidityWeek() > 0);
    }

    @Test
    void shouldReturnStateAverages() {
        State state = new State("Česko", 2L);
        City city1 = new City(3L,"Liberec", state);
        City city2 = new City(4L,"Brno", state);

        state.setCities(List.of(city1, city2));

        when(stateRepository.findById(2L)).thenReturn(Optional.of(state));
        when(measurementRepository.findByCityIdAndTimestampAfter(eq(3L), any(LocalDateTime.class)))
                .thenReturn(List.of(
                        new Measurement(18.0, 55.0, LocalDateTime.now(), city1)
                ));
        when(measurementRepository.findByCityIdAndTimestampAfter(eq(4L), any(LocalDateTime.class)))
                .thenReturn(List.of(
                        new Measurement(20.0, 60.0, LocalDateTime.now(), city2)
                ));

        var averages = measurementStatsService.getStateAverages(2L);

        assertEquals(2L, averages.stateId());
        assertEquals("Česko", averages.stateName());
        assertTrue(averages.averageTempWeek() > 0);
        assertTrue(averages.averageHumidityTwoWeeks() > 0);
    }
}
