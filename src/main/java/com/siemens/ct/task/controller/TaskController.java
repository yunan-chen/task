package com.siemens.ct.task.controller;

import com.siemens.ct.task.service.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

    @Autowired
    TaskServiceImpl taskService;

    /**
     * get and return the temperature of one certain county in China
     * @param province
     * @param city
     * @param county
     */
    @GetMapping("/task/getTemperature")
    public String getTemperature(@RequestParam(defaultValue = "", required = false) String province,
                               @RequestParam(defaultValue = "", required = false) String city,
                               @RequestParam(defaultValue = "", required = false) String county) {
        return taskService.getTemperature(province, city, county);
    }
}
