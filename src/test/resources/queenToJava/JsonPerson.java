package org.queenlang.helloworld;

import javax.json.JsonObject;

@FromJson
public final class JsonPerson implements Person {

    @NotNull
    private JsonObject json;

    private javax.json.JsonObject cached;

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

    @Override
    public final String name() {
        return this.name(false);
    }

    @Override
    public final String name(final boolean supportMissing) {
        return this.getString("name", supportMissing);
    }

    private String getString(final String key, @NotNull final boolean supportMissing) {
        if (supportMissing) {
            this.json.getString(key, "");
        }
        return this.json.getString(key);
    }
}
