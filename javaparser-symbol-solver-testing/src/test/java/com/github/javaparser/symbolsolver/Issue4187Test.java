package com.github.javaparser.symbolsolver;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.resolution.AbstractResolutionTest;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author yangruilin.yrl
 * @version $Id: Issue4187.java, v0.1 2023年11月02日 09:40 yangruilin.yrl Exp $
 */
public class Issue4187Test extends AbstractResolutionTest {
    @Test
    void issue4187() throws IOException {
        // Setup symbol solver
        JavaParserTypeSolver javaParserTypeSolver = new JavaParserTypeSolver(adaptPath("src/test/resources/issue4187/"));
        StaticJavaParser.getConfiguration()
                .setSymbolResolver(new JavaSymbolSolver(
                        new CombinedTypeSolver(new ReflectionTypeSolver(), javaParserTypeSolver))
                );

        // Parse the File
        Path filePath = adaptPath("src/test/resources/issue4187/com/example/service/impl/RecordServiceImpl.java");

        StaticJavaParser.parse(filePath).accept(new VoidVisitorAdapter<String>() {
            @Override
            public void visit(MethodDeclaration methodDeclaration, String arg) {
                try {
                    System.out.println(methodDeclaration.resolve().getQualifiedName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }
}
