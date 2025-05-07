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


}
