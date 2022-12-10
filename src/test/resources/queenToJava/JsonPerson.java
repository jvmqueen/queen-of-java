package org.queenlang.helloworld;

import javax.json.JsonObject;

@FromJson
public final class JsonPerson implements Person {

    @NotNull
    private final JsonObject json;

    public JsonPerson() {
        this(Json.createObjectBuilder().build());
    }

    public JsonPerson(final JsonObject json) {
        super();
        this.json = json;
    }
}
