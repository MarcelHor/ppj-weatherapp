package marcel.weatherapp2.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import marcel.weatherapp2.dto.StateCreateDto;
import marcel.weatherapp2.dto.StateDto;
import marcel.weatherapp2.service.StateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/state")
@RequiredArgsConstructor
public class StateController {

    private final StateService service;

    @GetMapping("/{id}")
    public StateDto getState(@PathVariable Long id) {
        return new StateDto(service.findById(id));
    }

    @GetMapping("/all")
    public List<StateDto> getStates() {
        return service.findAll().stream().map(StateDto::new).toList();
    }

    @PostMapping
    public StateDto createState(@Valid @RequestBody StateCreateDto dto) {
        return new StateDto(service.save(dto));
    }

    @PutMapping("/{id}")
    public StateDto updateState(@PathVariable Long id, @Valid @RequestBody StateCreateDto dto) {
        return new StateDto(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
