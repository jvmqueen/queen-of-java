package com.amihaiemil.example;

import java.util.Collection;

@MyCollection
public interface Collection<E> {

    public <T> boolean containsAll(final Collection<T> c);

    public <T extends E> boolean addAll(final Collection<T> c);
}
