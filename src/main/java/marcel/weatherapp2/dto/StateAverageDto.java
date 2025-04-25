package marcel.weatherapp2.dto;

public record StateAverageDto(Long stateId, String stateName, double averageTempDay, double averageTempWeek,
                              double averageTempTwoWeeks, double averageHumidityDay, double averageHumidityWeek,
                              double averageHumidityTwoWeeks) {

}
