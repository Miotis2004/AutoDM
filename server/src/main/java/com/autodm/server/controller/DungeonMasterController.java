package com.autodm.server.controller;

import com.autodm.server.service.dm.ActionResponse;
import com.autodm.server.service.dm.DungeonMasterEngine;
import com.autodm.server.service.dm.PlayerAction;
import com.autodm.server.service.dm.SceneInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/campaigns/{campaignId}/dm")
public class DungeonMasterController {

    private final DungeonMasterEngine dungeonMasterEngine;

    public DungeonMasterController(DungeonMasterEngine dungeonMasterEngine) {
        this.dungeonMasterEngine = dungeonMasterEngine;
    }

    @GetMapping("/scene")
    public ResponseEntity<SceneInfo> getCurrentScene(@PathVariable Long campaignId) {
        SceneInfo sceneInfo = dungeonMasterEngine.getCurrentScene(campaignId);
        return ResponseEntity.ok(sceneInfo);
    }

    @PostMapping("/actions")
    public ResponseEntity<ActionResponse> handleAction(@PathVariable Long campaignId, @RequestBody PlayerAction action) {
        ActionResponse response = dungeonMasterEngine.handleAction(campaignId, action);
        return ResponseEntity.ok(response);
    }
}
