package marcel.weatherapp2.repository;

import marcel.weatherapp2.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    boolean existsByStateId(Long stateId);
}
