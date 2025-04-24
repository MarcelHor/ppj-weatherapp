package marcel.weatherapp2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CityCreateDto(@NotBlank String name, @NotNull Long stateId) {
}