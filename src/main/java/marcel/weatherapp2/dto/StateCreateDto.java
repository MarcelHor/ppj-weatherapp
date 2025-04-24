package marcel.weatherapp2.dto;

import jakarta.validation.constraints.NotBlank;

public record StateCreateDto(@NotBlank String name) {
}
