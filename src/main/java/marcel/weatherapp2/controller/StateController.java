package marcel.weatherapp2.controller;

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

    @GetMapping
    public List<StateDto> getState(@RequestParam(required = false) Long id) {
        if (id != null) {
            return List.of(service.findById(id));
        }
        return service.findAll();
    }

    @PostMapping
    public StateDto create(@RequestBody StateCreateDto dto) {
        return service.save(dto);
    }

    @PutMapping("/{id}")
    public StateDto update(@RequestBody StateDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
