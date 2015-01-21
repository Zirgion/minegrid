package com.example.minegrid;

import com.vaadin.data.Property;

public class StateProperty implements Property<String> {

    private String value;

    public StateProperty(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String newValue)
            throws com.vaadin.data.Property.ReadOnlyException {
        value = newValue;
    }

    @Override
    public Class<? extends String> getType() {
        return String.class;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public void setReadOnly(boolean newStatus) {
    }

}
