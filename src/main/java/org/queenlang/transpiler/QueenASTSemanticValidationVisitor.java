/**
 * Copyright (c) 2022-2023, Extremely Distributed Technologies S.R.L. Romania
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * Neither the name of the copyright holder nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
package org.queenlang.transpiler;

import org.queenlang.transpiler.nodes.NameNode;
import org.queenlang.transpiler.nodes.QueenNode;
import org.queenlang.transpiler.nodes.body.*;
import org.queenlang.transpiler.nodes.expressions.*;
import org.queenlang.transpiler.nodes.project.FileNode;
import org.queenlang.transpiler.nodes.statements.*;
import org.queenlang.transpiler.nodes.types.*;

import java.util.*;
import java.util.List;

/**
 * AST Visitor for semantic/contextual validation of Queen code.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.1
 * @todo #64:60min Continue implementing semantic validation for Queen.
 */
public final class QueenASTSemanticValidationVisitor implements QueenASTVisitor<List<SemanticProblem>> {

    @Override
    public List<SemanticProblem> visitFile(FileNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        final String fileName = node.fileName();
        final TypeDeclarationNode typeDeclaration = node.compilationUnit().typeDeclaration();
        final String typeDeclarationName = typeDeclaration.name();
        if(!typeDeclarationName.equals(fileName) && !(typeDeclarationName + ".queen").equals(fileName)) {
            problems.add(
                new QueenSemanticError(
                    "Declared type (" + typeDeclarationName + ") does not match the file's name (" + fileName + "). ",
                    typeDeclaration.position()
                )
            );
        }
        problems.addAll(this.visitCompilationUnit(node.compilationUnit()));
        return problems;
    }

    @Override
    public List<SemanticProblem> visitCompilationUnit(CompilationUnitNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        final List<ImportDeclarationNode> imports = node.importDeclarations();
        for(int i=imports.size() - 1; i>=0; i--) {
            for(int j=0; j<imports.size(); j++) {
                if(i!= j && imports.get(i).isContainedBy(imports.get(j))) {
                    problems.add(
                        new QueenSemanticError(
                            "Type '" + imports.get(i).importDeclarationName().identifier() + "' already imported. ",
                            imports.get(i).importDeclarationName().position()
                        )
                    );
                }
            }
        }
        for(final ImportDeclarationNode importDeclaration : imports) {
            if(importDeclaration.resolve() == null) {
                problems.add(
                    new QueenSemanticError(
                        "Symbol '" + importDeclaration.importDeclarationName().name() + "' could not be resolved. ",
                        importDeclaration.importDeclarationName().position()
                    )
                );
            }
        }
        problems.addAll(
            this.visitTypeDeclarationNode(node.typeDeclaration())
        );
        return problems;
    }

    @Override
    public List<SemanticProblem> visitAnnotationElementDeclarationNode(AnnotationElementDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        problems.addAll(this.visitNodeWithModifiers(node));
        return problems;
    }

    @Override
    public List<SemanticProblem> visitAnnotationTypeDeclarationNode(AnnotationTypeDeclarationNode node) {
        return this.visitAnnotationTypeBodyNode(node.body());
    }

    @Override
    public List<SemanticProblem> visitAnnotationTypeMemberDeclarationNode(AnnotationTypeMemberDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        if(node instanceof AnnotationElementDeclarationNode) {
            problems.addAll(this.visitAnnotationElementDeclarationNode((AnnotationElementDeclarationNode) node));
        } else if(node instanceof ClassDeclarationNode) {
            problems.addAll(this.visitClassDeclarationNode((ClassDeclarationNode) node));
        } else if(node instanceof InterfaceDeclarationNode) {
            problems.addAll(this.visitInterfaceDeclarationNode((InterfaceDeclarationNode) node));
        }
        return problems;
    }

    @Override
    public List<SemanticProblem> visitClassBodyDeclarationNode(ClassBodyDeclarationNode node) {
        if(node instanceof InstanceInitializerNode) {
            return this.visitInstanceInitializerNode((InstanceInitializerNode) node);
        } else if(node instanceof ConstructorDeclarationNode) {
            return this.visitConstructorDeclarationNode((ConstructorDeclarationNode) node);
        }
        return this.visitClassMemberDeclarationNode((ClassMemberDeclarationNode) node);
    }

    @Override
    public List<SemanticProblem> visitClassDeclarationNode(ClassDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        problems.addAll(this.visitNodeWithTypeParameters(node));

        final List<ClassOrInterfaceTypeNode> interfaces = node.of();
        final Set<String> unique = new HashSet<>();
        for(final ClassOrInterfaceTypeNode implementedInterface : interfaces) {
            final String fullName = implementedInterface.name();
            if(!unique.add(fullName)) {
                problems.add(
                    new QueenSemanticError(
                        "Interface '" + fullName + "' already present in of list.",
                        implementedInterface.position()
                    )
                );
            }
        }
        final ClassBodyNode body = node.body();
        boolean defaultCtorPresent = false;
        for(final ClassBodyDeclarationNode declaration : body) {
            if(declaration instanceof ConstructorDeclarationNode) {
                final ConstructorDeclarationNode ctor = (ConstructorDeclarationNode) declaration;
                if(!node.name().equals(ctor.name())) {
                    problems.add(
                        new QueenSemanticError(
                            "Constructor name '" + ctor.name() + "' does not match the implementation name.",
                            ctor.position()
                        )
                    );
                } else {
                    if(ctor.parameters() == null || ctor.parameters().isEmpty()) {
                        if(defaultCtorPresent) {
                            problems.add(
                                new QueenSemanticError(
                                    "Default Constructor '" + ctor.name() + "' is duplicated.",
                                    ctor.position()
                                )
                            );
                        }
                        defaultCtorPresent = true;
                    }
                }
            }
        }
        problems.addAll(this.visitClassBodyNode(node.body()));
        return problems;
    }

    @Override
    public List<SemanticProblem> visitClassMemberDeclarationNode(ClassMemberDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        if(node instanceof FieldDeclarationNode) {
            problems.addAll(this.visitFieldDeclarationNode((FieldDeclarationNode) node));
        } else if(node instanceof MethodDeclarationNode) {
            problems.addAll(this.visitMethodDeclarationNode((MethodDeclarationNode) node));
        } else if(node instanceof ClassDeclarationNode) {
            problems.addAll(this.visitClassDeclarationNode((ClassDeclarationNode) node));
        } else if(node instanceof InterfaceDeclarationNode) {
            problems.addAll(this.visitInterfaceDeclarationNode((InterfaceDeclarationNode) node));
        }
        return problems;
    }

    @Override
    public List<SemanticProblem> visitConstantDeclarationNode(ConstantDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        problems.addAll(this.visitNodeWithAnnotations(node));
        problems.addAll(this.visitNodeWithModifiers(node));
        final ModifierNode publicModifier = node.modifier("public");
        final ModifierNode staticModifier = node.modifier("static");
        if(publicModifier != null) {
            problems.add(
                new QueenSemanticWarning(
                    "Mofifier 'public' is redundant.",
                    publicModifier.position()
                )
            );
        }
        if(staticModifier != null) {
            problems.add(
                new QueenSemanticWarning(
                    "Mofifier 'static' is redundant.",
                    staticModifier.position()
                )
            );
        }
        problems.addAll(this.visitTypeNode(node.type()));
        return problems;
    }

    @Override
    public List<SemanticProblem> visitConstructorDeclarationNode(ConstructorDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        problems.addAll(this.visitNodeWithAnnotations(node));
        problems.addAll(this.visitNodeWithParameters(node));
        problems.addAll(this.visitNodeWithTypeParameters(node));
        problems.addAll(this.visitNodeWithThrows(node));
        problems.addAll(this.visitExplicitConstructorInvocationNode(node.explicitConstructorInvocationNode()));
        problems.addAll(this.visitBlockStatements(node.blockStatements()));
        return problems;
    }

    @Override
    public List<SemanticProblem> visitFieldDeclarationNode(FieldDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        problems.addAll(this.visitNodeWithAnnotations(node));
        problems.addAll(this.visitNodeWithModifiers(node));
        final List<ModifierNode> accessModifiers = node.accessModifiers();
        if(accessModifiers.size() > 1) {
            for(final ModifierNode modifier : accessModifiers) {
                problems.add(
                    new QueenSemanticError(
                        "A field may have only one access modifier.",
                        modifier.position()
                    )
                );
            }
        }
        if(node.isPublic() || accessModifiers.isEmpty()) {
            if(!node.isStatic() || node.isMutable()) {
                problems.add(
                    new QueenSemanticError(
                        "Public or package-private fields must be static constants.",
                        node.position()
                    )
                );
            }
        }
        problems.addAll(this.visitTypeNode(node.type()));
        problems.addAll(this.visitVariableDeclaratorNode(node.variable()));
        return problems;
    }

    @Override
    public List<SemanticProblem> visitLocalVariableDeclarationNode(final LocalVariableDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        problems.addAll(this.visitNodeWithAnnotations(node));
        problems.addAll(this.visitNodeWithModifiers(node));
        problems.addAll(this.visitTypeNode(node.type()));
        node.variables().forEach(
            v -> problems.addAll(this.visitVariableDeclaratorNode(v))
        );
        return problems;
    }

    @Override
    public List<SemanticProblem> visitInterfaceDeclarationNode(InterfaceDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        final List<ModifierNode> modifiers = node.modifiers();
        modifiers.forEach(
            m -> {
                final String modifierString = m.modifier();
                if("abstract".equalsIgnoreCase(modifierString)) {
                    problems.add(
                        new QueenSemanticWarning(
                            "Modifier '" + modifierString + "' is redundant here.",
                            m.position()
                        )
                    );
                }
            }
        );
        if(node instanceof NormalInterfaceDeclarationNode) {
            problems.addAll(
                this.visitNormalInterfaceDeclarationNode(
                    (NormalInterfaceDeclarationNode) node
                )
            );
        } else if(node instanceof AnnotationTypeDeclarationNode) {
            problems.addAll(
                this.visitAnnotationTypeDeclarationNode(
                    (AnnotationTypeDeclarationNode) node
                )
            );
        }
        return problems;
    }

    @Override
    public List<SemanticProblem> visitInterfaceMemberDeclarationNode(InterfaceMemberDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        if(node instanceof ConstantDeclarationNode) {
            problems.addAll(this.visitConstantDeclarationNode((ConstantDeclarationNode) node));
        } else if(node instanceof InterfaceMethodDeclarationNode) {
            problems.addAll(this.visitInterfaceMethodDeclarationNode((InterfaceMethodDeclarationNode) node));
        } else if(node instanceof ClassDeclarationNode) {
            problems.addAll(this.visitClassDeclarationNode((ClassDeclarationNode) node));
        } else if(node instanceof InterfaceDeclarationNode) {
            problems.addAll(this.visitInterfaceDeclarationNode((InterfaceDeclarationNode) node));
        }
        return problems;
    }

    @Override
    public List<SemanticProblem> visitInterfaceMethodDeclarationNode(InterfaceMethodDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        problems.addAll(this.visitNodeWithModifiers(node));
        problems.addAll(this.visitNodeWithParameters(node));
        problems.addAll(this.visitNodeWithTypeParameters(node));
        problems.addAll(this.visitNodeWithThrows(node));

        final boolean isAbstractMethod = node.isAbstract();
        final ModifierNode publicModifier = node.modifier("public");
        final ModifierNode abstractModifier = node.modifier("abstract");

        if(publicModifier != null) {
            problems.add(
                new QueenSemanticWarning(
                    "Modifier 'public' is redundant.",
                    publicModifier.position()
                )
            );
        }
        if(isAbstractMethod) {
            if(abstractModifier != null) {
                problems.add(
                    new QueenSemanticWarning(
                        "Modifier 'abstract' is redundant.",
                        abstractModifier.position()
                    )
                );
            }
            final ModifierNode strictfpModifier = node.modifier("strictfp");
            if(strictfpModifier != null) {
                problems.add(
                    new QueenSemanticError(
                        "Modifier 'strictfp' cannot be placed on abstract methods.",
                        strictfpModifier.position()
                    )
                );
            }
            if(node.blockStatements() != null) {
                problems.add(
                    new QueenSemanticError(
                        "Method '" + node.name() +  "' is abstract, it cannot have a body.",
                        abstractModifier.position()
                    )
                );
            }
        } else {
            if(abstractModifier != null) {
                problems.add(
                    new QueenSemanticError(
                        "Modifier 'abstract' on non-abstract method.",
                        abstractModifier.position()
                    )
                );
            }
            if(node.blockStatements() == null) {
                problems.add(
                    new QueenSemanticError(
                        "Method '" + node.name() +  "' is not abstract, it needs to have a body.",
                        abstractModifier.position()
                    )
                );
            }
        }
        problems.addAll(this.visitTypeNode(node.returnType()));
        problems.addAll(this.visitBlockStatements(node.blockStatements()));

        return problems;
    }

    @Override
    public List<SemanticProblem> visitMethodDeclarationNode(MethodDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        problems.addAll(this.visitNodeWithModifiers(node));
        problems.addAll(this.visitNodeWithParameters(node));
        problems.addAll(this.visitNodeWithTypeParameters(node));
        problems.addAll(this.visitNodeWithThrows(node));
        problems.addAll(this.visitBlockStatements(node.blockStatements()));
        return problems;
    }

    @Override
    public List<SemanticProblem> visitNormalInterfaceDeclarationNode(NormalInterfaceDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        problems.addAll(this.visitNodeWithTypeParameters(node));
        problems.addAll(this.visitInterfaceBodyNode(node.body()));

        final List<ClassOrInterfaceTypeNode> extendsInterfaces = node.extendsTypes();
        final Set<String> unique = new HashSet<>();
        for(final ClassOrInterfaceTypeNode extendsInterface : extendsInterfaces) {
            final String fullName = extendsInterface.name();
            if(!unique.add(fullName)) {
                problems.add(
                    new QueenSemanticError(
                        "Interface '" + fullName + "' already present in extends list.",
                        extendsInterface.position()
                    )
                );
            }
        }

        return problems;
    }

    @Override
    public List<SemanticProblem> visitParameterNode(ParameterNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        problems.addAll(this.visitNodeWithModifiers(node));
        return problems;
    }

    @Override
    public List<SemanticProblem> visitTypeDeclarationNode(TypeDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        problems.addAll(this.visitNodeWithModifiers(node));
        final List<ModifierNode> modifiers = node.modifiers();
        if(modifiers != null && modifiers.size() > 0) {
            final List<String> allowedModifiers = Arrays.asList("public", "abstract", "strictfp");
            modifiers.forEach(m -> {
                final String modifierString = m.modifier();
                if(!allowedModifiers.contains(modifierString)) {
                    problems.add(
                        new QueenSemanticError(
                            "Modifier '" + modifierString + "' not allowed here.",
                            m.position()
                        )
                    );
                }
            });
        }
        if(node instanceof ClassDeclarationNode) {
            problems.addAll(
                this.visitClassDeclarationNode(
                    (ClassDeclarationNode) node
                )
            );
        } else if(node instanceof InterfaceDeclarationNode) {
            problems.addAll(
                this.visitInterfaceDeclarationNode(
                    (InterfaceDeclarationNode) node
                )
            );
        }
        return problems;
    }

    @Override
    public List<SemanticProblem> visitNodeWithParameters(final NodeWithParameters node) {
        final List<SemanticProblem> problems = new ArrayList<>();

        final List<ParameterNode> parameters = node.parameters();
        final Set<String> unique = new HashSet<>();
        for(final ParameterNode parameter : parameters) {
            final String name = parameter.variableDeclaratorId().name();
            if(!unique.add(name)) {
                problems.add(
                    new QueenSemanticError(
                        "Parameter '" + name + "' is duplicated.",
                        parameter.position()
                    )
                );
            }
            problems.addAll(this.visitParameterNode(parameter));
        }

        return problems;
    }

    @Override
    public List<SemanticProblem> visitNodeWithTypeParameters(final NodeWithTypeParameters node) {
        final List<SemanticProblem> problems = new ArrayList<>();

        final List<TypeParameterNode> typeParameters = node.typeParameters();
        if(typeParameters != null && typeParameters.size() > 0) {
            final Set<String> unique = new HashSet<>();
            typeParameters.forEach(
                tp -> {
                    if(!unique.add(tp.name())) {
                        problems.add(
                            new QueenSemanticError(
                                "Type parameter '" + tp.name() + "' already present.",
                                tp.position()
                            )
                        );
                    }
                    problems.addAll(this.visitTypeParameterNode(tp));
                }
            );
        }
        //@todo #49:60min Validate boundTypes of each type parameter.
        return problems;
    }

    @Override
    public List<SemanticProblem> visitNodeWithThrows(final NodeWithThrows node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        final List<ExceptionTypeNode> exceptions = node.throwsList();

        final Set<String> unique = new HashSet<>();
        for(final ExceptionTypeNode exception : exceptions) {
            final String fullName = exception.exceptionType().name();
            if(!unique.add(fullName)) {
                problems.add(
                    new QueenSemanticError(
                        "Exception '" + fullName + "' already present in throws list.",
                        exception.position()
                    )
                );
            }
            problems.addAll(this.visitExceptionTypeNode(exception));
        }

        return problems;
    }

    @Override
    public List<SemanticProblem> visitNameNode(final NameNode node) {
        if(node.resolve() == null) {
            return Arrays.asList(
                new QueenSemanticError(
                    "Symbol " + node.name() + " could not be resolved.",
                    node.position()
                )
            );
        }
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitNodeWithModifiers(final NodeWithModifiers node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        final Set<String> unique = new HashSet<>();
        node.modifiers().forEach(m -> {
            final String modifierString = m.modifier();
            if(!unique.add(modifierString)) {
                problems.add(
                    new QueenSemanticError(
                        "Modifier '" + modifierString + "' is duplicated.",
                        m.position()
                    )
                );
            }
        });
        return problems;
    }

    @Override
    public List<SemanticProblem> visitNodeWithAnnotations(final NodeWithAnnotations node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem>  defaultResult() {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> aggregateResult(
        final List<SemanticProblem> aggregate,
        final List<SemanticProblem> nextResult
    ) {
        final List<SemanticProblem> all = new ArrayList<>();
        all.addAll(aggregate);
        all.addAll(nextResult);
        return all;
    }
}
