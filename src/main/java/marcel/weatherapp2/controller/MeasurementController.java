package marcel.weatherapp2.controller;

import lombok.RequiredArgsConstructor;
import marcel.weatherapp2.dto.MeasurementDto;
import marcel.weatherapp2.model.Measurement;
import marcel.weatherapp2.service.MeasurementService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/measurement")
@RequiredArgsConstructor
public class MeasurementController {

    private final MeasurementService service;

    @GetMapping("/current/{cityId}")
    public MeasurementDto getCurrentMeasurement(@PathVariable Long cityId) {
        Measurement measurement = service.getMeasurement(cityId);
        return new MeasurementDto(measurement);
    }

    @GetMapping("/range")
    public List<MeasurementDto> getInRange(
            @RequestParam Long cityId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return service.getMeasurements(cityId, from, to).stream()
                .map(MeasurementDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping
    public List<MeasurementDto> getAll() {
        return service.getAllMeasurements().stream()
                .map(MeasurementDto::new)
                .collect(Collectors.toList());
    }
}
