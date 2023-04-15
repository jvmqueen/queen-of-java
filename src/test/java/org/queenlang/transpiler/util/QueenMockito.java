package org.queenlang.transpiler.util;

import org.mockito.Mockito;
import org.queenlang.transpiler.nodes.QueenNode;

public class QueenMockito {

    public static <T> T mock(Class<T> classToMock) {
        final T mock = Mockito.mock(classToMock);
        try {
            final QueenNode node = (QueenNode) mock;
            Mockito.when(node.withParent(Mockito.any())).thenReturn(node);
        } catch (final ClassCastException ignored) {
        }
        return mock;
    }

}
