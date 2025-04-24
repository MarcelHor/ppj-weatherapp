package marcel.weatherapp2.dto;

import marcel.weatherapp2.model.State;

public record StateDto(Long id, String name) {
    public StateDto(State state) {
        this(state.getId(), state.getName());
    }
}
