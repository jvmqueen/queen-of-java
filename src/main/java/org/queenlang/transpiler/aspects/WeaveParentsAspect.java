package org.queenlang.transpiler.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
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

/**
 * Due to immutable nature of the Queen AST, created by a visitor pattern over the ANTLR Parser,
 * there is no other way of setting the parents of each node, than through AOP.
 *
 * The Queen AST needs to be traversable downwards as well as upwards, in order to accomplish semantic
 * validation.
 *
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 */
@Aspect
public class WeaveParentsAspect {

    private final Logger LOG = LoggerFactory.getLogger(WeaveParentsAspect.class);

    @AfterReturning(
        value = "execution(@org.queenlang.transpiler.aspects.WeaveParents * org.queenlang.transpiler.QueenToJavaTranspiler.*(..))",
        returning = "project"
    )
    public void weaveTheParents(final JoinPoint joinPoint, QueenProject project) {
        LOG.info("Weaving parents into object: {} at {}", project.getClass().getSimpleName(), joinPoint.getSignature());
        this.traverse(project);
        LOG.info("Finished weaving the QueenProject, containing {} Queen files.", project.children().size());
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