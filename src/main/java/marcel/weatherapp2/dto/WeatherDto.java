package marcel.weatherapp2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDto {
    private double temperature;
    private double humidity;
    private LocalDateTime timestamp;
}
