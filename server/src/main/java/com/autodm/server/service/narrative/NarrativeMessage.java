package com.autodm.server.service.narrative;

/**
 * A structured narrative message produced by the engine.
 */
public class NarrativeMessage {

    private NarrativeCategory category;
    private String text;

    public NarrativeMessage() {
    }

    public NarrativeMessage(NarrativeCategory category, String text) {
        this.category = category;
        this.text = text;
    }

    public NarrativeCategory getCategory() {
        return category;
    }

    public void setCategory(NarrativeCategory category) {
        this.category = category;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
