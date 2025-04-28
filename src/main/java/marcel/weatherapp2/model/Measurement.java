package marcel.weatherapp2.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double temperature;
    private double humidity;
    private LocalDateTime timestamp;

    @ManyToOne
    private City city;

    public Measurement(double temperature, double humidity, LocalDateTime timestamp, City city) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.timestamp = timestamp;
        this.city = city;
    }

    public Measurement(Long id, double temperature, double humidity, LocalDateTime timestamp, City city) {
        this.id = id;
        this.temperature = temperature;
        this.humidity = humidity;
        this.timestamp = timestamp;
        this.city = city;
    }
}


