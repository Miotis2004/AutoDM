package com.autodm.server.controller;

import com.autodm.server.service.dm.ActionResponse;
import com.autodm.server.service.dm.DungeonMasterEngine;
import com.autodm.server.service.dm.PlayerAction;
import com.autodm.server.service.dm.SceneInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DungeonMasterController.class)
public class DungeonMasterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DungeonMasterEngine dungeonMasterEngine;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetCurrentScene() throws Exception {
        SceneInfo mockScene = new SceneInfo();
        mockScene.setTitle("Test Scene");
        Mockito.when(dungeonMasterEngine.getCurrentScene(1L)).thenReturn(mockScene);

        mockMvc.perform(get("/api/campaigns/1/dm/scene"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Scene"));
    }

    @Test
    public void testHandleAction() throws Exception {
        PlayerAction mockAction = new PlayerAction();
        mockAction.setCharacterId(1L);
        mockAction.setActionType(com.autodm.server.service.dm.PlayerActionType.ATTACK);
        ActionResponse mockResponse = new ActionResponse();
        mockResponse.setSuccess(true);
        mockResponse.setNarrative("Test narrative");

        Mockito.when(dungeonMasterEngine.handleAction(Mockito.eq(1L), Mockito.any(PlayerAction.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(post("/api/campaigns/1/dm/actions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockAction)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.narrative").value("Test narrative"));
    }
}
