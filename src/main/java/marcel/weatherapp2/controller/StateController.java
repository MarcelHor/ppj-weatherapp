package marcel.weatherapp2.controller;

import lombok.RequiredArgsConstructor;
import marcel.weatherapp2.dto.StateCreateDto;
import marcel.weatherapp2.dto.StateDto;
import marcel.weatherapp2.service.StateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/states")
@RequiredArgsConstructor
public class StateController {

    private final StateService service;

    @GetMapping
    public List<StateDto> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public StateDto getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public StateDto create(@RequestBody StateCreateDto dto) {
        return service.save(dto);
    }

    @PutMapping("/{id}")
    public StateDto update(@PathVariable Long id, @RequestBody StateDto dto) {
        dto.setId(id);
        service.update(dto);
        return dto;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
