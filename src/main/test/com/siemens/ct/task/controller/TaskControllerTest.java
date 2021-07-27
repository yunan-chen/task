package com.siemens.ct.task.controller;

import com.siemens.ct.task.service.TaskServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TaskControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    TaskServiceImpl taskService;

    @Test
    public void testGetTemperature() throws Exception {
        String province = "江苏";
        String city = "苏州";
        String county = "吴中";
        String temperature = "22";
        when(taskService.getTemperature(province, city, county)).thenReturn(temperature);
        this.mockMvc.perform(get("/task/getTemperature")
                .param("province", province)
                .param("city", province)
                .param("county", province))
                .andExpect(status().isOk());
    }
}
