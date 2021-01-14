package com.ices.discrete_event_simulation.service.parseXML.pojo;

public class InteractionPOJO {
    private String InteractionName;
    private String Subscribe;
    private String Publish;

    public String getInteractionName() {
        return InteractionName;
    }

    public void setInteractionName(String interactionName) {
        InteractionName = interactionName;
    }

    public String getSubscribe() {
        return Subscribe;
    }

    public void setSubscribe(String subscribe) {
        Subscribe = subscribe;
    }

    public String getPublish() {
        return Publish;
    }

    public void setPublish(String publish) {
        Publish = publish;
    }
}
