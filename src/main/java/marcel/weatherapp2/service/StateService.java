package marcel.weatherapp2.service;

import lombok.RequiredArgsConstructor;
import marcel.weatherapp2.dto.StateCreateDto;
import marcel.weatherapp2.model.State;
import marcel.weatherapp2.repository.CityRepository;
import marcel.weatherapp2.repository.StateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StateService {

    private final StateRepository repository;
    private final CityRepository cityRepository;

    public List<State> findAll() {
        return repository.findAll();
    }

    public State findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("State not found"));
    }

    public State save(StateCreateDto dto) {
        return repository.save(new State(dto.name()));
    }

    public State update(Long id, StateCreateDto dto) {
        State state = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("State not found"));
        state.setName(dto.name());
        return repository.save(state);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("State not found");
        }

        if(cityRepository.existsByStateId(id)) {
            throw new IllegalArgumentException("State cannot be deleted because it has associated cities");
        }

        repository.deleteById(id);
    }
}
