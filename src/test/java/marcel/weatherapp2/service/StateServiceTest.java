package marcel.weatherapp2.service;

import marcel.weatherapp2.dto.StateCreateDto;
import marcel.weatherapp2.model.State;
import marcel.weatherapp2.repository.CityRepository;
import marcel.weatherapp2.repository.StateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StateServiceTest {

    @Mock
    private StateRepository repository;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private StateService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFindStateById() {
        State state = new State("Česko", 1L);
        when(repository.findById(1L)).thenReturn(Optional.of(state));

        State result = service.findById(1L);
        assertEquals("Česko", result.getName());
        assertEquals(1L, result.getId());
    }

    @Test
    void shouldThrowExceptionIfStateNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.findById(999L));
    }

    @Test
    void shouldSaveState() {
        StateCreateDto dto = new StateCreateDto("Rakousko");
        State saved = new State("Rakousko", 5L);

        when(repository.save(any(State.class))).thenReturn(saved);
        State result = service.save(dto);
        assertEquals("Rakousko", result.getName());
        assertEquals(5L, result.getId());
    }

    @Test
    void shouldUpdateState() {
        State existing = new State("Původní", 1L);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(State.class))).thenAnswer(inv -> inv.getArgument(0));

        State result = service.update(1L, new StateCreateDto("Nový název"));
        assertEquals("Nový název", result.getName());
    }

    @Test
    void shouldDeleteState() {
        when(repository.existsById(1L)).thenReturn(true);
        when(cityRepository.existsByStateId(1L)).thenReturn(false);

        doNothing().when(repository).deleteById(1L);

        service.delete(1L);
        verify(repository, times(1)).deleteById(1L);
    }
}
