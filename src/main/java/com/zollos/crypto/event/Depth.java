package com.zollos.crypto.event;

import com.binance.api.client.domain.event.DepthEvent;

public class Depth implements Event<DepthEvent> {

    public Depth(DepthEvent event) {

    }

    @Override
    public String getKey() {
        return null;
    }
}
