package marcel.weatherapp2.service;

import lombok.RequiredArgsConstructor;
import marcel.weatherapp2.dto.CityAverageDto;
import marcel.weatherapp2.dto.StateAverageDto;
import marcel.weatherapp2.model.City;
import marcel.weatherapp2.model.Measurement;
import marcel.weatherapp2.model.State;
import marcel.weatherapp2.repository.CityRepository;
import marcel.weatherapp2.repository.MeasurementRepository;
import marcel.weatherapp2.repository.StateRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeasurementStatsService {

    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final MeasurementRepository measurementRepository;

    public CityAverageDto getCityAverages(Long cityId) {
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new IllegalArgumentException("City not found"));

        return new CityAverageDto(
                city.getId(),
                city.getName(),
                avgTemp(cityId, 1),
                avgTemp(cityId, 7),
                avgTemp(cityId, 14),
                avgHumidity(cityId, 1),
                avgHumidity(cityId, 7),
                avgHumidity(cityId, 14)
        );
    }

    public StateAverageDto getStateAverages(Long stateId) {
        State state = stateRepository.findById(stateId)
                .orElseThrow(() -> new IllegalArgumentException("State not found"));

        List<City> cities = state.getCities();

        return new StateAverageDto(
                state.getId(),
                state.getName(),
                avgTemp(cities, 1),
                avgTemp(cities, 7),
                avgTemp(cities, 14),
                avgHumidity(cities, 1),
                avgHumidity(cities, 7),
                avgHumidity(cities, 14)
        );
    }

    private double avgTemp(Long cityId, int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return measurementRepository.findByCityIdAndTimestampAfter(cityId, since).stream()
                .mapToDouble(Measurement::getTemperature)
                .average().orElse(0.0);
    }

    private double avgHumidity(Long cityId, int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return measurementRepository.findByCityIdAndTimestampAfter(cityId, since).stream()
                .mapToDouble(Measurement::getHumidity)
                .average().orElse(0.0);
    }

    private double avgTemp(List<City> cities, int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return cities.stream()
                .flatMap(c -> measurementRepository.findByCityIdAndTimestampAfter(c.getId(), since).stream())
                .mapToDouble(Measurement::getTemperature)
                .average().orElse(0.0);
    }

    private double avgHumidity(List<City> cities, int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return cities.stream()
                .flatMap(c -> measurementRepository.findByCityIdAndTimestampAfter(c.getId(), since).stream())
                .mapToDouble(Measurement::getHumidity)
                .average().orElse(0.0);
    }
}
