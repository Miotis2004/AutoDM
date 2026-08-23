package com.autodm.server.controller;

import com.autodm.server.dto.LocationDto;
import com.autodm.server.model.LocationType;
import com.autodm.server.service.LocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LocationController.class)
public class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService locationService;

    @Test
    void getLocationsByCampaign() throws Exception {
        LocationDto dto = new LocationDto();
        dto.setId(1L);
        dto.setName("Waterdeep");
        dto.setType(LocationType.SETTLEMENT);

        when(locationService.getLocationsByCampaign(1L)).thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/locations?campaignId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Waterdeep"));
    }
}
