package com.amihaiemil.eoyaml;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@YamlMapping
public interface YamlMapping extends YamlNode {

    int constant = 1;

    Set<YamlNode> keys();

    YamlNode value(final YamlNode key) throws NodeNotFoundException;

    default Collection<YamlNode> values() {
        int x, y, z = 0;
        int m = 1, n = 2, p = 3;
        int q = 1, k = 5, w;
        final List<YamlNode> values = new LinkedList<>();
        for (final YamlNode key : this.keys()) {
            values.add(this.value(key));
        }
        return values;
    }
}
