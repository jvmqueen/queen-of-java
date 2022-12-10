package org.queenlang.helloworld;

import javax.json.JsonObject;

@FromJson
public final class JsonPerson implements Person {

    @NotNull
    private final JsonObject json;

    static {
        System.out.println("STATIC INITIALIZER CALLED");
    }

    {
        System.out.println("INSTANCE INITIALIZER CALLED");
    }

    public JsonPerson(final InputStream jsonStream) {
        super();
        try {
            this.json = Json.readStream(jsonStream).toObject();
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public JsonPerson(final JsonObject json) {
        super();
        this.json = json;
        if (this.json == null) {
            throw new Exception("NOT OK");
        }
    }
}
