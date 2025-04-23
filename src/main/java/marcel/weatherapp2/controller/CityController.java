package marcel.weatherapp2.controller;

import lombok.RequiredArgsConstructor;
import marcel.weatherapp2.dto.CityCreateDto;
import marcel.weatherapp2.dto.CityDto;
import marcel.weatherapp2.service.CityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/city")
@RequiredArgsConstructor
public class CityController {
    private final CityService service;

    @GetMapping
    public List<CityDto> getCity(@RequestParam(required = false) Long id) {
        if (id != null) {
            return List.of(service.findById(id));
        }
        return service.findAll();
    }

    @PostMapping
    public CityDto create(@RequestBody CityCreateDto dto) {
        return service.save(dto);
    }

    @PutMapping("/{id}")
    public CityDto update(@RequestBody CityDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
