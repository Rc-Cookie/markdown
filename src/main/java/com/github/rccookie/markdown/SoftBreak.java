package com.github.rccookie.markdown;

import com.github.rccookie.json.JsonObject;

public class SoftBreak extends Word implements TextItem<Element<?>> {

    public SoftBreak() {
        super("  \n");
    }

    @Override
    public Object toJson() {
        return new JsonObject();
    }
}
