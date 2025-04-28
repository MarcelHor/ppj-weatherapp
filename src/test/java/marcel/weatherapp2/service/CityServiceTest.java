package marcel.weatherapp2.service;

import marcel.weatherapp2.dto.CityCreateDto;
import marcel.weatherapp2.model.City;
import marcel.weatherapp2.model.State;
import marcel.weatherapp2.repository.CityRepository;
import marcel.weatherapp2.repository.MeasurementRepository;
import marcel.weatherapp2.repository.StateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private StateRepository stateRepository;

    @Mock
    private MeasurementRepository measurementRepository;

    @InjectMocks
    private CityService cityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFindCityById() {
        State state = new State("Česko", 1L);
        City city = new City(1L,"Liberec", state);

        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

        City result = cityService.findById(1L);
        assertEquals("Liberec", result.getName());
        assertEquals(1L, result.getId());
        assertEquals("Česko", result.getState().getName());
    }

    @Test
    void shouldThrowExceptionIfCityNotFound() {
        when(cityRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> cityService.findById(999L));
    }

    @Test
    void shouldSaveCity() {
        State state = new State("Slovensko", 2L);
        when(stateRepository.findById(2L)).thenReturn(Optional.of(state));

        City city = new City(2L,"Bratislava", state);

        when(cityRepository.save(any(City.class))).thenReturn(city);

        CityCreateDto dto = new CityCreateDto("Bratislava", 2L);
        City result = cityService.save(dto);

        assertEquals("Bratislava", result.getName());
        assertEquals(2L, result.getId());
        assertEquals("Slovensko", result.getState().getName());
    }

    @Test
    void shouldUpdateCity() {
        State oldState = new State("Česko", 1L);
        State newState = new State("Slovensko", 2L);

        City city = new City(5L,"Olomouc", oldState);

        when(cityRepository.findById(5L)).thenReturn(Optional.of(city));
        when(stateRepository.findById(2L)).thenReturn(Optional.of(newState));
        when(cityRepository.save(any(City.class))).thenAnswer(inv -> inv.getArgument(0));

        CityCreateDto dto = new CityCreateDto("Bratislava", 2L);
        City updated = cityService.update(5L, dto);

        assertEquals("Bratislava", updated.getName());
        assertEquals("Slovensko", updated.getState().getName());
    }

    @Test
    void shouldDeleteCity() {
        when(cityRepository.existsById(3L)).thenReturn(true);
        when(measurementRepository.existsByCityId(3L)).thenReturn(false);

        doNothing().when(cityRepository).deleteById(3L);

        cityService.delete(3L);

        verify(cityRepository, times(1)).deleteById(3L);
    }

    @Test
    void shouldThrowIfCityHasMeasurements() {
        when(cityRepository.existsById(4L)).thenReturn(true);
        when(measurementRepository.existsByCityId(4L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> cityService.delete(4L));
    }
}
