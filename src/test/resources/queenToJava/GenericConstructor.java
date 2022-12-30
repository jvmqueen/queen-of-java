public final class GenericConstructor<K extends Comparable, V> implements Entry {

    private K key;

    private V value;

    public <K extends Comparable, V> GenericConstructor(final K key, final V value) {
        this.key = key;
        this.value = value;
    }
}
