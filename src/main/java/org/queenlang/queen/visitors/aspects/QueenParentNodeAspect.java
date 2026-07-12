package org.queenlang.queen.visitors.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.queenlang.queen.nodes.QueenNode;

import java.lang.reflect.Field;

@Aspect
public class QueenParentNodeAspect {

    @AfterReturning(
        value = "execution(@org.queenlang.queen.visitors.aspects.QueenParentNode * org.queenlang.queen.visitors.QueenParseTreeVisitor.*(..))",
        returning = "result"
    )
    public void visitAspect(final JoinPoint joinPoint, QueenNode result) {
        if(result != null && !result.children().isEmpty()) {
            result.children().forEach(child -> {
                if(child != null) {
                    try {
                        Field field = child.getClass().getDeclaredField("parent");
                        field.setAccessible(true);
                        field.set(child, result);
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }
}