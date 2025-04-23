package marcel.weatherapp2.service;

import lombok.RequiredArgsConstructor;
import marcel.weatherapp2.dto.CityDto;
import marcel.weatherapp2.dto.StateDto;
import marcel.weatherapp2.dto.CityCreateDto;
import marcel.weatherapp2.dto.StateCreateDto;
import marcel.weatherapp2.model.City;
import marcel.weatherapp2.model.State;
import marcel.weatherapp2.repository.CityRepository;
import marcel.weatherapp2.repository.StateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository repository;
    private final StateRepository stateRepository;


    public List<CityDto> findAll() {
        return repository.findAll().stream()
                .map(city -> new CityDto(city.getId(), city.getName(), city.getState().getId(), city.getState().getName()))
                .toList();
    }

    public CityDto findById(Long id) {
        City city = repository.findById(id).orElseThrow(() -> new RuntimeException("City not found"));
        return new CityDto(city.getId(), city.getName(), city.getState().getId(), city.getState().getName());
    }

    public CityDto save(CityCreateDto dto) {
        State state = stateRepository.findById(dto.getStateId());
        City city = new City(null, dto.getName(), new State(state.getId(), state.getName()));
        City savedCity = repository.save(city);
        return new CityDto(savedCity.getId(), savedCity.getName(), savedCity.getState().getId(), savedCity.getState().getName());
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public CityDto update(CityDto dto) {
        State state = stateRepository.findById(dto.getStateId());
        City city = new City(dto.getId(), dto.getName(), state);
        City updatedCity = repository.save(city);
        return new CityDto(updatedCity.getId(), updatedCity.getName(), updatedCity.getState().getId(), updatedCity.getState().getName());
    }
}
