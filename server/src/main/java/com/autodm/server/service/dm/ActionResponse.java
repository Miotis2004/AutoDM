package com.autodm.server.service.dm;

import com.autodm.server.service.narrative.NarrativeMessage;
import java.util.List;

/**
 * Represents the engine's response to a player action.
 */
public class ActionResponse {

    private boolean success;
    private String narrative;
    private List<NarrativeMessage> narrativeLog;
    private List<String> stateChanges;
    private SceneInfo updatedScene;

    public ActionResponse() {
    }

    public ActionResponse(boolean success, String narrative, List<NarrativeMessage> narrativeLog, List<String> stateChanges, SceneInfo updatedScene) {
        this.success = success;
        this.narrative = narrative;
        this.narrativeLog = narrativeLog;
        this.stateChanges = stateChanges;
        this.updatedScene = updatedScene;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getNarrative() {
        return narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    public List<NarrativeMessage> getNarrativeLog() {
        return narrativeLog;
    }

    public void setNarrativeLog(List<NarrativeMessage> narrativeLog) {
        this.narrativeLog = narrativeLog;
    }

    public List<String> getStateChanges() {
        return stateChanges;
    }

    public void setStateChanges(List<String> stateChanges) {
        this.stateChanges = stateChanges;
    }

    public SceneInfo getUpdatedScene() {
        return updatedScene;
    }

    public void setUpdatedScene(SceneInfo updatedScene) {
        this.updatedScene = updatedScene;
    }
}
