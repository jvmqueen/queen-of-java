package org.queenlang.transpiler.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.queenlang.queen.nodes.QueenNode;
import org.queenlang.queen.nodes.project.QueenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Aspect
public class WeaveParentsAspect {

    private final Logger LOG = LoggerFactory.getLogger(WeaveParentsAspect.class);

    @AfterReturning(
        value = "execution(@org.queenlang.transpiler.aspects.WeaveParents * org.queenlang.transpiler.QueenToJavaTranspiler.*(..))",
        returning = "result"
    )
    public void weaveTheParents(final JoinPoint joinPoint, QueenNode result) {
        LOG.debug("Weaving object: {} at {}", result.getClass().getSimpleName(), joinPoint.getSignature());
        this.traverse(result);
        LOG.debug("Finished weaving.");
    }

    public void traverse(final QueenNode project) {
        Deque<QueenNode> stack = new ArrayDeque<>();
        stack.push(project);

        while (!stack.isEmpty()) {
            QueenNode parent = stack.pop();
            final List<QueenNode> children = parent.children().stream().filter(Objects::nonNull).collect(Collectors.toList());
            if(!children.isEmpty()) {
                LOG.debug("Parent to weave inside its children: {}.", parent.getClass().getSimpleName());
            }
            children.forEach(
                child -> {
                    try {
                        Field field = child.getClass().getDeclaredField("parent");
                        field.setAccessible(true);
                        field.set(child, parent);
                        LOG.debug("|----Parent woven into child: {}", child.getClass().getSimpleName());
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        throw new RuntimeException(e);
                    }
                    stack.push(child);
                }
            );
        }
    }
}