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
import org.queenlang.transpiler.nodes.expressions.AnnotationNode;
import org.queenlang.transpiler.nodes.expressions.MarkerAnnotationNode;
import org.queenlang.transpiler.nodes.expressions.NormalAnnotationNode;
import org.queenlang.transpiler.nodes.expressions.SingleMemberAnnotationNode;
import org.queenlang.transpiler.nodes.project.FileNode;
import org.queenlang.transpiler.nodes.statements.BlockStatements;
import org.queenlang.transpiler.nodes.statements.StatementNode;
import org.queenlang.transpiler.nodes.types.*;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

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
            final ImportDeclarationNode importDeclaration = imports.get(i);
            for(int j=0; j<imports.size(); j++) {
                if(i!= j && importDeclaration.isContainedBy(imports.get(j))) {
                    problems.add(
                        new QueenSemanticError(
                            "Type '" + importDeclaration.importDeclarationName().identifier() + "' already imported. ",
                            importDeclaration.importDeclarationName().position()
                        )
                    );
                }
            }
            problems.addAll(this.visitImportDeclarationNode(importDeclaration));
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
        problems.addAll(this.visitNodeWithAnnotations(node));
        problems.addAll(this.visitTypeNode(node.type()));
        if(node.defaultValue() != null) {
            problems.addAll(this.visitExpressionNode(node.defaultValue()));
        }
        return problems;
    }

    @Override
    public List<SemanticProblem> visitAnnotationTypeDeclarationNode(AnnotationTypeDeclarationNode node) {
        return this.visitAnnotationTypeBodyNode(node.body());
    }

    @Override
    public List<SemanticProblem> visitAnnotationTypeBodyNode(final AnnotationTypeBodyNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        problems.addAll(this.visitNodeWithTypeDeclarations(node));

        final List<AnnotationElementDeclarationNode> elements = node.annotationMemberDeclarations().stream().filter(
            amd -> amd instanceof AnnotationElementDeclarationNode
        ).map(
            amd -> (AnnotationElementDeclarationNode) amd
        ).collect(Collectors.toList());
        final Set<String> unique = new HashSet<>();
        for(final AnnotationElementDeclarationNode element : elements) {
            final String name = element.name();
            if(!unique.add(name)) {
                problems.add(
                    new QueenSemanticError(
                        "Annotation element '" + name + "' already declared.",
                        element.position()
                    )
                );
            }
        }

        node.annotationMemberDeclarations().forEach(
            amd -> problems.addAll(this.visitAnnotationTypeMemberDeclarationNode(amd))
        );
        return problems;
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
        problems.addAll(this.visitNodeWithAnnotations(node));
        problems.addAll(this.visitNodeWithTypeParameters(node));
        if(node.of() != null) {
            problems.addAll(this.visitInterfaceTypeList(node.of()));
        }
        if(node.extendsType() != null) {
            problems.addAll(this.visitClassOrInterfaceTypeNode(node.extendsType()));
            final QueenNode resolved = node.extendsType().resolve();
            if(resolved != null) {
                final ClassDeclarationNode asClass = resolved.asClassDeclarationNode();
                if(asClass == null) {
                    problems.add(
                        new QueenSemanticError(
                            "Symbol " + node.extendsType().name() + " should resolve to an implementation. Instead, it resolves to an interface.",
                            node.position()
                        )
                    );
                } else {
                    if(!asClass.isAbstract()) {
                        problems.add(
                            new QueenSemanticError(
                                "Implementation '" + node.extendsType().name() + "' is final, it cannot be extended.",
                                node.position()
                            )
                        );
                    }
                }
            }

        }
        problems.addAll(this.visitClassBodyNode(node.body()));
        return problems;
    }

    @Override
    public List<SemanticProblem> visitClassBodyNode(final ClassBodyNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        problems.addAll(this.visitNodeWithFieldDeclarations(node));
        problems.addAll(this.visitNodeWithConstructors(node));
        problems.addAll(this.visitNodeWithMethodDeclarations(node));
        problems.addAll(this.visitNodeWithTypeDeclarations(node));

        node.classBodyDeclarations().forEach(
            cbd -> problems.addAll(this.visitClassBodyDeclarationNode(cbd))
        );
        return problems;
    }

    @Override
    public List<SemanticProblem> visitInterfaceTypeList(final InterfaceTypeList node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        final List<ClassOrInterfaceTypeNode> interfaces = node.interfaceTypes();
        final Set<String> unique = new HashSet<>();
        for(final ClassOrInterfaceTypeNode implementedInterface : interfaces) {
            problems.addAll(this.visitClassOrInterfaceTypeNode(implementedInterface));
            final QueenNode resolved = implementedInterface.resolve();
            if(resolved != null) {
                final NormalInterfaceDeclarationNode interfaceDeclarationNode = resolved.asNormalInterfaceDeclaration();
                if(interfaceDeclarationNode != null) {
                    final String fullName = interfaceDeclarationNode.fullTypeName();
                    if(!unique.add(fullName)) {
                        problems.add(
                            new QueenSemanticError(
                                "Type '" + fullName + "' already present in the list of interfaces.",
                                implementedInterface.position()
                            )
                        );
                    }
                }
            }
        }
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
                    "Modifier 'public' is redundant.",
                    publicModifier.position()
                )
            );
        }
        if(staticModifier != null) {
            problems.add(
                new QueenSemanticWarning(
                    "Modifier 'static' is redundant.",
                    staticModifier.position()
                )
            );
        }
        problems.addAll(this.visitTypeNode(node.type()));
        if(node.variable().initializer() == null) {
            problems.add(
                new QueenSemanticError(
                    "Constant '" + node.variable().variableDeclaratorId().name() + "' may not have been initialized.",
                    node.position()
                )
            );
        }
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
    public List<SemanticProblem> visitTypeNode(final TypeNode node) {
        if(node instanceof PrimitiveTypeNode) {
            return this.visitPrimitiveTypeNode((PrimitiveTypeNode) node);
        } if(node instanceof ReferenceTypeNode) {
            return this.visitReferenceTypeNode((ReferenceTypeNode) node);
        } if(node instanceof VoidTypeNode) {
            return this.visitVoidTypeNode((VoidTypeNode) node);
        } if(node instanceof WildcardTypeNode) {
            return this.visitWildcardTypeNode((WildcardTypeNode) node);
        }
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitReferenceTypeNode(final ReferenceTypeNode node) {
        if(node instanceof ArrayTypeNode) {
            return this.visitArrayTypeNode((ArrayTypeNode) node);
        } else if(node instanceof ClassOrInterfaceTypeNode) {
            return this.visitClassOrInterfaceTypeNode((ClassOrInterfaceTypeNode) node);
        } else if(node instanceof ExceptionTypeNode) {
            return this.visitExceptionTypeNode((ExceptionTypeNode) node);
        }
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem>  visitArrayTypeNode(final ArrayTypeNode node) {
        return this.visitTypeNode(node.type());
    }

    @Override
    public List<SemanticProblem> visitClassOrInterfaceTypeNode(final ClassOrInterfaceTypeNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        problems.addAll(this.visitNodeWithAnnotations(node));
        problems.addAll(this.visitNodeWithTypeArguments(node));
        final QueenNode resolved = node.resolve();
        if(resolved == null) {
            problems.add(
                new QueenSemanticError(
                    "Symbol '" + node.name() + "' could not be resolved.",
                    node.position()
                )
            );
        } else {
            if(node.interfaceType()) {
                final NormalInterfaceDeclarationNode asInterface = resolved.asNormalInterfaceDeclaration();
                if(asInterface == null) {
                    problems.add(
                        new QueenSemanticError(
                            "Symbol " + node.name() + " should resolve to an interface. Instead, it resolves to an implementation.",
                            node.position()
                        )
                    );
                }
            }

        }
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
    public List<SemanticProblem> visitBlockStatements(final BlockStatements node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        if(node == null) {
            return problems;
        }
        for(final StatementNode stmt : node) {
            problems.addAll(this.visitStatementNode(stmt));
        }
        return problems;
    }

    @Override
    public List<SemanticProblem> visitMethodDeclarationNode(final MethodDeclarationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        if(node.isStatic() && "void".equals(node.returnType().name()) && !node.isMainMethod()) {
            problems.add(
                new QueenSemanticError(
                    "Static method '" + node.name() + "' is void.",
                    node.position()
                )
            );
        }
        if(node.interfaceDeclaration()) {
            final boolean isDefaultMethod = node.isDefaultMethod();
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
            if(!isDefaultMethod) {
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
                            node.position()
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
                            node.position()
                        )
                    );
                }
            }
        }
        problems.addAll(this.visitNodeWithAnnotations(node));
        problems.addAll(this.visitNodeWithModifiers(node));
        problems.addAll(this.visitTypeNode(node.returnType()));
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

        if(node.extendsTypes() != null) {
            problems.addAll(this.visitInterfaceTypeList(node.extendsTypes()));
        }

        return problems;
    }

    @Override
    public List<SemanticProblem> visitInterfaceBodyNode(final InterfaceBodyNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        problems.addAll(this.visitNodeWithConstantDeclarations(node));
        problems.addAll(this.visitNodeWithMethodDeclarations(node));
        problems.addAll(this.visitNodeWithTypeDeclarations(node));

        node.interfaceMemberDeclarations().forEach(
            imd -> problems.addAll(this.visitInterfaceMemberDeclarationNode(imd))
        );
        return problems;
    }

    @Override
    public List<SemanticProblem> visitParameterNode(ParameterNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        problems.addAll(this.visitNodeWithModifiers(node));
        problems.addAll(this.visitNodeWithAnnotations(node));
        problems.addAll(this.visitTypeNode(node.type()));
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
    public List<SemanticProblem> visitNodeWithTypeArguments(final NodeWithTypeArguments node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        node.typeArguments().forEach( ta ->
            problems.addAll(this.visitTypeNode(ta))
        );
        return problems;
    }

    @Override
    public List<SemanticProblem> visitNodeWithFieldDeclarations(final NodeWithFieldDeclarations node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        final List<FieldDeclarationNode> fields = node.fieldDeclarations();
        final Set<String> unique = new HashSet<>();
        for(final FieldDeclarationNode field : fields) {
            final String name = field.name();
            if(!unique.add(name)) {
                problems.add(
                    new QueenSemanticError(
                        "Field '" + name + "' already declared.",
                        field.position()
                    )
                );
            }
        }
        return problems;
    }

    @Override
    public List<SemanticProblem> visitNodeWithTypeDeclarations(final NodeWithTypeDeclarations node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        final List<TypeDeclarationNode> typeDeclarations = node.typeDeclarations();
        final Set<String> unique = new HashSet<>();
        for(final TypeDeclarationNode typeDeclarationNode : typeDeclarations) {
            final String name = typeDeclarationNode.name();
            if(!unique.add(name)) {
                problems.add(
                    new QueenSemanticError(
                        "Type '" + name + "' already declared.",
                        typeDeclarationNode.position()
                    )
                );
            }
        }
        return problems;
    }

    @Override
    public List<SemanticProblem> visitNodeWithConstructors(final NodeWithConstructors node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        final List<ConstructorDeclarationNode> constructors = node.constructors();
        for(final ConstructorDeclarationNode ctor : constructors) {
            if(!ctor.name().equals(node.className())) {
                problems.add(
                    new QueenSemanticError(
                        "Constructor name '" + ctor.name() + "' does not match the implementation's name ('" + node.className() + "').",
                        ctor.position()
                    )
                );
            }
        }
        for(int i=0; i<constructors.size();i++) {
            for(int j=0; j<constructors.size(); j++) {
                if(i!=j) {
                    final ConstructorDeclarationNode ctorI = constructors.get(i);
                    final ConstructorDeclarationNode ctorJ = constructors.get(j);
                    if(ctorI.name().equals(ctorJ.name())) {
                        if(this.sameParameters(ctorI, ctorJ)) {
                            problems.add(
                                new QueenSemanticError(
                                    "Constructor '" + ctorJ.name() + "' already declared.",
                                    ctorJ.position()
                                )
                            );
                        }
                    }
                }
            }
        }
        return problems;
    }

    @Override
    public List<SemanticProblem> visitNodeWithConstantDeclarations(final NodeWithConstantDeclarations node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        final List<ConstantDeclarationNode> constants = node.constantDeclarations();
        final Set<String> unique = new HashSet<>();
        for(final ConstantDeclarationNode constant : constants) {
            final String name = constant.name();
            if(!unique.add(name)) {
                problems.add(
                    new QueenSemanticError(
                        "Constant '" + name + "' already declared.",
                        constant.position()
                    )
                );
            }
        }
        return problems;
    }

    @Override
    public List<SemanticProblem> visitNodeWithMethodDeclarations(final NodeWithMethodDeclarations node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        final List<MethodDeclarationNode> methods = node.methods();
        for(int i=0; i<methods.size();i++) {
            for(int j=0; j<methods.size(); j++) {
                if(i!=j) {
                    final MethodDeclarationNode methodI = methods.get(i);
                    final MethodDeclarationNode methodJ = methods.get(j);
                    if(methodI.name().equals(methodJ.name())) {
                        if(this.sameParameters(methodI, methodJ)) {
                            problems.add(
                                new QueenSemanticError(
                                    "Method '" + methodJ.name() + "' already declared.",
                                    methodJ.position()
                                )
                            );
                        }
                    }
                }
            }
        }
        return problems;
    }

    @Override
    public List<SemanticProblem> visitTypeParameterNode(final TypeParameterNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        problems.addAll(this.visitNodeWithAnnotations(node));
        if(node.typeBound() != null) {
            for(final ClassOrInterfaceTypeNode tb : node.typeBound()) {
                problems.addAll(this.visitClassOrInterfaceTypeNode(tb));
            }
        }
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
        final List<SemanticProblem> problems = new ArrayList<>();
        node.annotations().forEach( a ->
            problems.addAll(this.visitAnnotationNode(a))
        );
        return problems;
    }

    @Override
    public List<SemanticProblem> visitAnnotationNode(final AnnotationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        final QueenNode resolved = node.nameNode().resolve();
        if(resolved == null || resolved.asAnnotationTypeDeclarationNode() == null) {
            problems.add(
                new QueenSemanticError(
                    "Symbol '@" + node.name() + "' could not be resolved or it is not an annotation.",
                    node.position()
                )
            );
        }
        if(node instanceof SingleMemberAnnotationNode) {
            problems.addAll(this.visitSingleMemberAnnotationNode((SingleMemberAnnotationNode) node));
        } else if (node instanceof NormalAnnotationNode) {
            problems.addAll(this.visitNormalAnnotationNode((NormalAnnotationNode) node));
        } else {
            problems.addAll(this.visitMarkerAnnotationNode((MarkerAnnotationNode) node));
        }
        return problems;
    }

    @Override
    public List<SemanticProblem> visitMarkerAnnotationNode(final MarkerAnnotationNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<SemanticProblem> visitNormalAnnotationNode(final NormalAnnotationNode node) {
        final List<SemanticProblem> problems = new ArrayList<>();
        node.elementValuePairs().forEach( evp ->
            problems.addAll(this.visitElementValuePairNode(evp))
        );
        return problems;
    }

    @Override
    public List<SemanticProblem> visitSingleMemberAnnotationNode(final SingleMemberAnnotationNode node) {
        return this.visitExpressionNode(node.elementValue());
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

    /**
     * Checks if the lists of ParameterNodes are identical, looking at type and order. Names of the parameters are not
     * relevant.
     * <pre>
     *     (String s, int i)
     * </pre>
     * is identical to
     * <pre>
     *     (java.util.String a, int x)
     * </pre>
     * but
     * <pre>
     *     (int i, String s)
     * </pre>
     * is not identical to
     * <pre>
     * (String s, int i)
     * </pre>
     * Symbol resolution is also performed. In the example above, String and java.util.String are referencing the same class,
     * one with simple name (and import statement), the other with fully qualified name.
     *
     * @param nodeOne Node with parameters. Can be a method or a constructor declaration etc.
     * @param nodeTwo Node with parameters. Can be a method or a constructor declaration etc.
     * @return True if lists are identical, false otherwise.
     */
    private boolean sameParameters(final NodeWithParameters nodeOne, final NodeWithParameters nodeTwo) {
        final List<ParameterNode> first = nodeOne.parameters();
        final List<ParameterNode> second = nodeTwo.parameters();
        if(first == null || second == null || first.size() != second.size()) {
            return false;
        } else {
            for(int i=0; i<first.size(); i++) {
                final ParameterNode firstParam = first.get(i);
                final ParameterNode secondParam = second.get(i);
                if(firstParam.varArgs() != secondParam.varArgs()) {
                    return false;
                }
                final TypeNode paramTypeFirst = firstParam.type();
                final TypeNode paramTypeSecond = secondParam.type();
                if(paramTypeFirst instanceof PrimitiveTypeNode && paramTypeSecond instanceof PrimitiveTypeNode) {
                    final String nameFirst = paramTypeFirst.name();
                    final String nameSecond = paramTypeSecond.name();
                    if(!nameFirst.equals(nameSecond)) {
                        return false;
                    }
                } else if(paramTypeFirst instanceof ReferenceTypeNode && paramTypeSecond instanceof ReferenceTypeNode) {
                    final QueenNode firstTypeResolved = ((ReferenceTypeNode) paramTypeFirst).resolve();
                    final QueenNode secondTypeResolved = ((ReferenceTypeNode) paramTypeSecond).resolve();
                    if(firstTypeResolved != null && secondTypeResolved != null) {
                        final String nameFirst = firstTypeResolved.asTypeDeclarationNode().fullTypeName();
                        final String nameSecond = secondTypeResolved.asTypeDeclarationNode().fullTypeName();
                        if(!nameFirst.equals(nameSecond)) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }
}
