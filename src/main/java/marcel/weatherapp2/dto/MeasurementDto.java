package marcel.weatherapp2.dto;

import marcel.weatherapp2.model.Measurement;
import java.time.LocalDateTime;

public record MeasurementDto(
        Long id,
        double temperature,
        double humidity,
        LocalDateTime timestamp,
        String cityName,
        String stateName
) {
    public MeasurementDto(Measurement m) {
        this(
                m.getId(),
                m.getTemperature(),
                m.getHumidity(),
                m.getTimestamp(),
                m.getCity().getName(),
                m.getCity().getState().getName()
        );
    }
}
