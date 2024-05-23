package com.github.swankdave.fastest;

import com.github.swankdave.fastest.javadocparser.JavadocParser;
import com.github.swankdave.fastest.javadocparser.JavadocParserBaseListener;
import org.antlr.v4.runtime.tree.ErrorNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavadocListener extends JavadocParserBaseListener {

    private TestHeaders context;
    private String subject;
    private String body;
    public List<BlockTag> blockTags;

    public JavadocListener() {
        context = TestHeaders.test;
        subject="";
        body="";
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
        }else{
            context = null;
        }
        body = "";
    }

    /**
     * Exit a parse tree produced by {@link JavadocParser#blockTagContent()}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitBlockTagContent(JavadocParser.BlockTagContentContext ctx) {
        if (!(context==null))
            body += ctx.getText();

    }

    /**
     * Exit a parse tree produced by {@link JavadocParser#blockTagContent()}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitBlockTag(JavadocParser.BlockTagContext ctx) {
        if (!(context==null))
            if (ctx.children.size() > 2)
                blockTags.add(
                        new BlockTag(
                                context,
                                subject.replaceAll("^([\\n\\s]*)\\*+","$1"),
                                body.split("[\\n\\s]",2)[1]
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
