package marcel.weatherapp2.service;

import lombok.RequiredArgsConstructor;
import marcel.weatherapp2.dto.CityCreateDto;
import marcel.weatherapp2.model.City;
import marcel.weatherapp2.model.State;
import marcel.weatherapp2.repository.CityRepository;
import marcel.weatherapp2.repository.MeasurementRepository;
import marcel.weatherapp2.repository.StateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository repository;
    private final StateRepository stateRepository;
    private final MeasurementRepository measurementRepository;


    public List<City> findAll() {
        return repository.findAll();
    }

    public City findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("City not found"));
    }

    public City save(CityCreateDto dto) {
        State state = stateRepository.findById(dto.stateId()).orElseThrow(() -> new IllegalArgumentException("State not found"));
        return repository.save(new City(dto.name(), state));
    }

    public City update(Long id, CityCreateDto dto) {
        City city = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("City not found"));
        State state = stateRepository.findById(dto.stateId()).orElseThrow(() -> new IllegalArgumentException("State not found"));
        city.setName(dto.name());
        city.setState(state);
        return repository.save(city);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("City not found");
        }

        if(measurementRepository.existsByCityId(id)) {
            throw new IllegalArgumentException("City has measurements and cannot be deleted");
        }

        repository.deleteById(id);
    }
}
