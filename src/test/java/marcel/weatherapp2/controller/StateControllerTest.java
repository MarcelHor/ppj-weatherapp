package marcel.weatherapp2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import marcel.weatherapp2.dto.StateCreateDto;
import marcel.weatherapp2.model.State;
import marcel.weatherapp2.service.StateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StateController.class)
class StateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StateService stateService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnStateById() throws Exception {
        State state = new State("Česko", 1L);
        when(stateService.findById(1L)).thenReturn(state);

        mockMvc.perform(get("/api/state/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.name").value("Česko"));
    }

    @Test
    void shouldReturnAllStates() throws Exception {
        State s1 = new State("Česko", 1L);
        State s2 = new State("Slovensko", 2L);

        when(stateService.findAll()).thenReturn(List.of(s1, s2));

        mockMvc.perform(get("/api/state/all")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(2)).andExpect(jsonPath("$[0].name").value("Česko")).andExpect(jsonPath("$[1].name").value("Slovensko"));
    }

    @Test
    void shouldCreateState() throws Exception {
        State created = new State("Slovensko", 2L);
        when(stateService.save(any(StateCreateDto.class))).thenReturn(created);

        mockMvc.perform(post("/api/state").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new StateCreateDto("Slovensko")))).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(2)).andExpect(jsonPath("$.name").value("Slovensko"));
    }

    @Test
    void shouldUpdateState() throws Exception {
        State updated = new State("Německo", 1L);

        when(stateService.update(eq(1L), any(StateCreateDto.class))).thenReturn(updated);

        mockMvc.perform(put("/api/state/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new StateCreateDto("Německo")))).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Německo"));
    }

    @Test
    void shouldDeleteState() throws Exception {
        doNothing().when(stateService).delete(1L);

        mockMvc.perform(delete("/api/state/1")).andExpect(status().isOk());
    }
}
