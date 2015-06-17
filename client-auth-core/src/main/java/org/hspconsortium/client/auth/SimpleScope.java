package org.hspconsortium.client.auth;

public class SimpleScope implements Scope {

    private String value;

    public SimpleScope(String value) {
        this.value = value;
    }

    @Override
    public String asStringValue() {
        return this.value;
    }
}
