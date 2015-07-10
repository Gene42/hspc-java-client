package org.hspconsortium.client.auth.impl;

import org.hspconsortium.client.auth.StateProvider;

import java.util.UUID;

public class DefaultStateProvider implements StateProvider {

    @Override
    public String getNewState() {
        return UUID.randomUUID().toString();
    }
}
