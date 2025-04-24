package marcel.weatherapp2.controller;

import jakarta.validation.Valid;
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
    public CityDto getCity(@RequestParam Long id) {
        return new CityDto(service.findById(id));
    }

    @GetMapping("/all")
    public List<CityDto> getCities() {
        return service.findAll().stream()
                .map(CityDto::new)
                .toList();
    }

    @PostMapping
    public CityDto createCity(@Valid @RequestBody CityCreateDto dto) {
        return new CityDto(service.save(dto));
    }

    @PutMapping("/{id}")
    public CityDto updateCity(@PathVariable Long id, @Valid @RequestBody CityCreateDto dto) {
        return new CityDto(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public void deleteCity(@PathVariable Long id) {
        service.delete(id);
    }
}
