package marcel.weatherapp2.service;

import lombok.RequiredArgsConstructor;
import marcel.weatherapp2.dto.StateCreateDto;
import marcel.weatherapp2.dto.StateDto;
import marcel.weatherapp2.model.State;
import marcel.weatherapp2.repository.StateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StateService {

    private final StateRepository repository;

    public List<StateDto> findAll() {
        return repository.findAll().stream()
                .map(state -> new StateDto(state.getId(), state.getName()))
                .toList();

    }

    public StateDto findById(Long id) {
        State state = repository.findById(id);
        return new StateDto(state.getId(), state.getName());
    }

    public StateDto save(StateCreateDto dto) {
        State state = new State(null, dto.getName());
        State savedState = repository.save(state);
        return new StateDto(savedState.getId(), savedState.getName());
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public void update(StateDto dto) {
        State state = new State(dto.getId(), dto.getName());
        repository.update(state);
    }
}
