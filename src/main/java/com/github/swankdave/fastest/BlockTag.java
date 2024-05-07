package com.github.swankdave.fastest;

public record BlockTag(TestHeaders context, String subject, String content) {
    public BlockTag(TestHeaders context, String subject, String content) {
        this.context = context;

        while (subject.startsWith("\n"))
            subject = subject.substring(1);
        while (content.startsWith("\n"))
            content = content.substring(1);

        switch (context) {
            case test, testDeclaration, testSetup, testTeardown -> {
                this.subject = "";
                this.content = subject + " " + content;
            }
            case testFragment, testData -> {
                this.subject = subject;
                this.content = content;
            }
            default ->
                throw new IllegalStateException("Unexpected value: " + context);
        }
    }
}
