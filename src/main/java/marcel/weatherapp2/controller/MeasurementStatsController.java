package marcel.weatherapp2.controller;

import lombok.RequiredArgsConstructor;
import marcel.weatherapp2.dto.CityAverageDto;
import marcel.weatherapp2.service.MeasurementStatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/measurement/average")
@RequiredArgsConstructor
public class MeasurementStatsController {

    private final MeasurementStatsService statsService;

    @GetMapping("/city/{id}")
    public CityAverageDto getCityAvg(@PathVariable Long id) {
        return statsService.getCityAverages(id);
    }
}
