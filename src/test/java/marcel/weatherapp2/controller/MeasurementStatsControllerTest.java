package marcel.weatherapp2.controller;

import marcel.weatherapp2.service.MeasurementStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(MeasurementStatsController.class)
class MeasurementStatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MeasurementStatsService service;


}
