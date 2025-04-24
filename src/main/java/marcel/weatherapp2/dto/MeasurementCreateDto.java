package marcel.weatherapp2.dto;

import jakarta.validation.constraints.NotNull;

public record MeasurementCreateDto(
        @NotNull Long cityId,
        @NotNull Double temperature,
        @NotNull Double humidity
) {}
