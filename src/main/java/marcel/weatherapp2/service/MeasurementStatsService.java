package marcel.weatherapp2.service;

import lombok.RequiredArgsConstructor;
import marcel.weatherapp2.dto.CityAverageDto;
import marcel.weatherapp2.model.City;
import marcel.weatherapp2.repository.CityRepository;
import marcel.weatherapp2.repository.MeasurementRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MeasurementStatsService {

    private final MeasurementRepository measurementRepository;
    private final CityRepository cityRepository;

    public CityAverageDto getCityAverages(Long cityId) {
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new IllegalArgumentException("City not found"));

        return new CityAverageDto(
                city.getId(),
                city.getName(),
                measurementRepository.getAverageTemperatureByCitySince(cityId, LocalDateTime.now().minusDays(1)),
                measurementRepository.getAverageTemperatureByCitySince(cityId, LocalDateTime.now().minusDays(7)),
                measurementRepository.getAverageTemperatureByCitySince(cityId, LocalDateTime.now().minusDays(14)),
                measurementRepository.getAverageHumidityByCitySince(cityId, LocalDateTime.now().minusDays(1)),
                measurementRepository.getAverageHumidityByCitySince(cityId, LocalDateTime.now().minusDays(7)),
                measurementRepository.getAverageHumidityByCitySince(cityId, LocalDateTime.now().minusDays(14))
        );
    }

}
