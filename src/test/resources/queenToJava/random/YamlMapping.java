package com.amihaiemil.eoyaml;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@YamlMapping
public interface YamlMapping extends YamlNode {

    int constant = 1;

    int[][] doubleArray = { { 1 } };

    int[] simpleArray = { 1 };

    int[] @Annotation [] arr = { { 1 } };

    int @Annotation2 [][] arr2 = { { 2 } };

    Set<YamlNode> keys();

    YamlNode value(final YamlNode key) throws NodeNotFoundException;

    default Collection<YamlNode> values() {
        int x;
        int y;
        int z = 0;
        int m = 1;
        int n = 2;
        int p = 3;
        int q = 1;
        int k = 5;
        int w;
        final List<YamlNode> values = new LinkedList<>();
        for (final YamlNode key : this.keys()) {
            values.add(this.value(key));
        }
        return values;
    }
}
