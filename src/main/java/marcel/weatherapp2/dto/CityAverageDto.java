package marcel.weatherapp2.dto;

public record CityAverageDto(
        Long cityId,
        String cityName,
        double averageTempDay,
        double averageTempWeek,
        double averageTempTwoWeeks,
        double averageHumidityDay,
        double averageHumidityWeek,
        double averageHumidityTwoWeeks
) {}
