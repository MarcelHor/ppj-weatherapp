package marcel.weatherapp2.dto;
import marcel.weatherapp2.model.City;

public record CityDto(Long id, String name, Long stateId, String stateName) {
    public CityDto(City city) {
        this(city.getId(), city.getName(), city.getState().getId(), city.getState().getName());
    }
}
