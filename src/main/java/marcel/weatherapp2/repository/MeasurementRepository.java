package marcel.weatherapp2.repository;

import marcel.weatherapp2.model.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    List<Measurement> findByCityIdAndTimestampBetween(Long cityId, LocalDateTime from, LocalDateTime to);

    boolean existsByCityId(Long cityId);

    @Query("SELECT AVG(m.temperature) FROM Measurement m WHERE m.city.id = :cityId AND m.timestamp > :since")
    Double getAverageTemperatureByCitySince(Long cityId, LocalDateTime since);

    @Query("SELECT AVG(m.humidity) FROM Measurement m WHERE m.city.id = :cityId AND m.timestamp > :since")
    Double getAverageHumidityByCitySince(Long cityId, LocalDateTime since);
}
