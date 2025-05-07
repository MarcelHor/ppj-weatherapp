package marcel.weatherapp2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marcel.weatherapp2.dto.MeasurementDto;
import marcel.weatherapp2.model.City;
import marcel.weatherapp2.model.Measurement;
import marcel.weatherapp2.repository.CityRepository;
import marcel.weatherapp2.repository.MeasurementRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeasurementService {
    private final MeasurementRepository repository;
    private final CityRepository cityRepository;
    private final RestTemplate restTemplate;

    @Value("${weather.api.key}")
    private String apiKey;

    public Measurement getMeasurement(Long cityId) {
        City city = cityRepository.findById(cityId).orElseThrow(() -> new RuntimeException("City not found"));
        String cityName = city.getName();

        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + apiKey + "&units=metric";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        JSONObject json = new JSONObject(response.getBody());
        log.info("Response from OpenWeather API: {}", json);
        double temp = json.getJSONObject("main").getDouble("temp");
        double humidity = json.getJSONObject("main").getDouble("humidity");
        LocalDateTime timestamp = LocalDateTime.now();

        log.info("Fetched weather for {}: temp={}°C, humidity={}%", cityName, temp, humidity);
        Measurement measurement = new Measurement(temp, humidity, timestamp, city);
        repository.save(measurement);
        return measurement;
    }

    public List<Measurement> getMeasurements(Long cityId, LocalDateTime from, LocalDateTime to) {
        return repository.findByCityIdAndTimestampBetween(cityId, from, to);
    }

    public List<Measurement> getAllMeasurements() {
        return repository.findAll();
    }

    public void deleteMeasurement(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Measurement not found");
        }
        repository.deleteById(id);
    }

    public Measurement updateMeasurement(Long id, MeasurementDto measurement) {
        Measurement existingMeasurement = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Measurement not found"));
        existingMeasurement.setTemperature(measurement.temperature());
        existingMeasurement.setHumidity(measurement.humidity());
        existingMeasurement.setTimestamp(measurement.timestamp());
        return repository.save(existingMeasurement);
    }

    @Transactional
    public void generateTestData(Long cityId, int count) {
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new IllegalArgumentException("City not found"));

        List<Measurement> list = new ArrayList<>(count);
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < count; i++) {
            double temp = ThreadLocalRandom.current().nextDouble(-10, 40);
            double humidity = ThreadLocalRandom.current().nextDouble(30, 90);
            LocalDateTime timestamp = now.minusHours(i);

            list.add(new Measurement(temp, humidity, timestamp, city));
        }

        repository.saveAll(list);
    }
}
