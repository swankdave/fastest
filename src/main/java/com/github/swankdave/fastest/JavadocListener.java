package com.github.swankdave.fastest;

import com.github.swankdave.fastest.javadocparser.JavadocParser;
import com.github.swankdave.fastest.javadocparser.JavadocParserBaseListener;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavadocListener extends JavadocParserBaseListener {

    private TestHeaders context;
    private String subject;
    public List<BlockTag> blockTags;

    public JavadocListener() {
        context = TestHeaders.test;
        subject="";
        blockTags=new ArrayList<>();
    }

    /**
     * Enter a parse tree produced by {@link JavadocParser#blockTagName}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterBlockTagName(JavadocParser.BlockTagNameContext ctx) {
        if (Arrays.stream(TestHeaders.values()).anyMatch(header->header.toString().equals(ctx.getText()))) {
            context = TestHeaders.valueOf(ctx.getText());
            subject = "";
        }
    }

    /**
     * Exit a parse tree produced by {@link JavadocParser#blockTagContents}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitBlockTagContents(JavadocParser.BlockTagContentsContext ctx) {
        if (ctx.children.size() > 2)
            blockTags.add(
                    new BlockTag(
                            context,
                            subject.replaceAll("^([\\n\\s]*)\\*+","$1"),
                            ctx.getText().split("[\\n\\s]",2)[1]
                                    .replaceAll("(\\n\\s*)\\*+","$1")
                                    .replaceAll("^(\\s*)\\*+","$1")
                    )
            );
    }

    /**
     * Enter a parse tree produced by {@link JavadocParser#blockTagContent}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterBlockTagContent(JavadocParser.BlockTagContentContext ctx) {
        if (subject.isEmpty())
            subject = ctx.getText();
    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {
        System.out.println("JavaDoc Parse Error: " + errorNode.getText());
    }
}
