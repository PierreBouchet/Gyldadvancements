package com.gylderia.gyldadvancements;

import java.lang.reflect.Type;
import java.util.Objects;

public enum configOptions {
    KEY("key", "BOTH"),
    PARENT("parent", "ADVANCEMENT"),
    NAME("name", "BOTH"),
    ICON("icon", "BOTH"),
    TEXTURE("texture", "ROOT"),
    MODEL_DATA("model_data", "BOTH"),
    DESCRIPTION("description", "BOTH"),
    FRAME("frame", "BOTH"),
    VISIBILITY("visibility", "BOTH"),
    FLAGS("flags", "BOTH"),
    X("x", "BOTH"),
    Y("y", "BOTH"),
    TRIGGER("trigger", "BOTH"),
    MOB("mob", "BOTH"),
    NUMBER("number", "BOTH");

    String name;
    String type;

    configOptions(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public boolean rootOnly() {
        return this.type.equals("ROOT");
    }

    public boolean advancementOnly() {
        return this.type.equals("ADVANCEMENT");
    }

    public String getName() {
        return this.name;
    }
}
