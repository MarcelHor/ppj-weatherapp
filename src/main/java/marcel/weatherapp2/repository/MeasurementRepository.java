package marcel.weatherapp2.repository;
import marcel.weatherapp2.model.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    List<Measurement> findByCityIdAndTimestampBetween(Long cityId, LocalDateTime from, LocalDateTime to);
    List<Measurement> findByCityIdAndTimestampAfter(Long cityId, LocalDateTime timestamp);
    boolean existsByCityId(Long cityId);
}
