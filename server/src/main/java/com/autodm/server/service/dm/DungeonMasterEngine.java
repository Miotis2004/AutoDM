package com.autodm.server.service.dm;

/**
 * Interface defining the DM engine abstraction.
 * Implementing classes could be a deterministic local engine, an LLM-based engine, etc.
 */
public interface DungeonMasterEngine {

    /**
     * Handles a player action and returns the engine's response.
     * @param campaignId The ID of the campaign.
     * @param action The action taken by the player.
     * @return The response from the engine.
     */
    ActionResponse handleAction(Long campaignId, PlayerAction action);

    /**
     * Retrieves the current scene info for a given campaign.
     * @param campaignId The ID of the campaign.
     * @return The scene information.
     */
    SceneInfo getCurrentScene(Long campaignId);
}
