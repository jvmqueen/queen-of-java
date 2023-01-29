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
        try {
            this.json = Json.readStream(jsonStream).toObject();
        } catch (final IOException | Exception | com.ex.AnotherEx ex) {
            throw new IllegalStateException(ex);
        }
        try {
            this.json = Json.readStream(jsonStream).toObject();
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        } catch (final Exception ex) {
            throw new IllegalStateException(ex);
        } catch (final com.ex.AnotherEx ex) {
            throw new IllegalStateException(ex);
        } finally {
            System.out.println("FINALLY");
        }
        try {
            this.json = Json.readStream(jsonStream).toObject();
        } finally {
            System.out.println("Try without catch, with finally.");
        }
        try (InputStream inst = Json.readStream(jsonStream);
            InputStream inst2 = Json.readStream(jsonStream)) {
            this.json = inst.toObject();
        } catch (final ex.ex.io.Exception exception) {
            System.out.println("Caught in try-with-resources");
        } finally {
            System.out.println("Finally in try-with-resources");
        }
    }

    public JsonPerson(final JsonObject json) {
        super();
        this.json = json;
        if (this.json == null) {
            throw new Exception("NOT OK");
        }
        if (this.json == null) {
            for (int i = 0; i < 10; i++) {
                System.out.println("No short if example for");
            }
        } else {
            System.out.println("All cool");
        }
    }

    @Override
    public final String name() {
        return this.name(false);
    }

    @Override
    public final String name(final boolean supportMissing) {
        java.test.TestClass.xx++;
        java.x--;
        u++;
        int o = m++ + n + ++m;
        casted = (int) i++;
        java.test.TestClass.xx = y + 2;
        return this.getString("name", supportMissing);
    }

    private String getString(final String key, @NotNull final boolean supportMissing) {
        if (supportMissing) {
            this.json.getString(key, "");
        }
        return this.json.getString(key);
    }
}
