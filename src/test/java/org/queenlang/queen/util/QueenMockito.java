package org.queenlang.queen.util;

import org.mockito.Mockito;
import org.queenlang.queen.nodes.QueenNode;

public class QueenMockito {

    public static <T> T mock(Class<T> classToMock) {
        final T mock = Mockito.mock(classToMock);
        try {
            final QueenNode node = (QueenNode) mock;
        } catch (final ClassCastException ignored) {
        }
        return mock;
    }

}
