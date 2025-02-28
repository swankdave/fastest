// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.github.swankdave.fastest;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.swankdave.fastest.java.JavaClassScope;
import com.github.swankdave.fastest.javascript.JavascriptClassScope;
import com.github.swankdave.fastest.kotlin.KotlinClassScope;
import com.github.swankdave.fastest.python.PythonClassScope;
import com.github.swankdave.fastest.typescript.TypescriptClassScope;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.codeStyle.CodeStyleManager;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.*;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static com.github.swankdave.fastest.Constants.CLASS_NAME_POSTFIX;

/**
 * Action class to demonstrate how to interact with the IntelliJ Platform.
 * The only action this class performs is to provide the user with a popup dialog as feedback.
 * Typically this class is instantiated by the IntelliJ Platform framework based on declarations
 * in the plugin.xml file. But when added at runtime this class is instantiated by an action group.
 */
public class PopupDialogAction extends AnAction {


  /**
   * This default constructor is used by the IntelliJ Platform framework to instantiate this class based on plugin.xml
   * declarations. Only needed in {@link PopupDialogAction} class because a second constructor is overridden.
   *
   * @see AnAction#AnAction()
   */
  public PopupDialogAction() {
    super();
  }

  /**
   * This constructor is used to support dynamically added menu actions.
   * It sets the text, description to be displayed for the menu item.
   * Otherwise, the default AnAction constructor is used by the IntelliJ Platform.
   *
   * @param text        The text to be displayed as a menu item.
   * @param description The description of the menu item.
   * @param icon        The icon to be used with the menu item.
   */
  public PopupDialogAction(@Nullable String text, @Nullable String description, @Nullable Icon icon) {
    super(text, description, icon);
  }

  /**
   * Gives the user feedback when the dynamic action menu is chosen.
   * Pops a simple message dialog. See the psi_demo plugin for an
   * example of how to use {@link AnActionEvent} to access data.
   *
   * @param event Event received when the associated menu item is chosen.
   */
  @Override
  public void actionPerformed(@NotNull AnActionEvent event) {
    Project project = event.getProject();

    if (project != null) {
      FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
      Editor selectedTextEditor = fileEditorManager.getSelectedTextEditor();
      VirtualFile file = fileEditorManager.getSelectedFiles()[0];
      String testFileName = file.getNameWithoutExtension() + CLASS_NAME_POSTFIX + "." + file.getExtension();

      if (selectedTextEditor != null) {
        var psiFile = PsiManager.getInstance(project).findFile(file);
        ClassScope classScope =
          switch (Objects.requireNonNull(psiFile).getFileType().getName()) {
            case "JAVA" -> new JavaClassScope(psiFile);
            case "Kotlin" -> new KotlinClassScope(psiFile);
            case "JavaScript" -> new JavascriptClassScope(psiFile);
            case "TypeScript" -> new TypescriptClassScope(psiFile);
            case "Python" -> new PythonClassScope(psiFile);
            default -> throw new RuntimeException("I don't know what to do with this file yet");
          };

        AtomicReference<VirtualFile> vTestDir = new AtomicReference<>(file.getParent().findChild(Constants.TEST_NAMESPACE));
        if (vTestDir.get() == null)
          ApplicationManager.getApplication().runWriteAction(() -> {
            try {
              vTestDir.set(file.getParent().createChildDirectory(this, Constants.TEST_NAMESPACE));
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          });
        AtomicReference<VirtualFile> vTestFile = new AtomicReference<>(vTestDir.get().findChild(testFileName));
        
        if (vTestFile.get() == null)
          ApplicationManager.getApplication().runWriteAction(() -> {
            try {
              vTestFile.set(vTestDir.get().createChildData(this, testFileName));
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          });
        PsiFile pTestFile = PsiManager.getInstance(project).findFile(vTestFile.get());
        var testDocument = PsiDocumentManager.getInstance(project)
                .getDocument(Objects.requireNonNull(pTestFile));

        try (InputStream resourceAsStream =
                     getClass().getClassLoader().getResourceAsStream(classScope.languageConfig.getMustacheTemplateFilenameForLanguage())) {
          assert resourceAsStream != null : "failed to load template file";


            try (StringWriter writer = new StringWriter()) {
              if (classScope.languageConfig.getMustacheTemplateFilenameForLanguage().endsWith("mustache"))
                new DefaultMustacheFactory()
                        .compile(new InputStreamReader(resourceAsStream), "root")
                        .execute(writer, classScope.getScopes());
              else if (classScope.languageConfig.getMustacheTemplateFilenameForLanguage().endsWith(".vtl")) {
                VelocityEngine velocityEngine = new VelocityEngine();
                velocityEngine.init();

                VelocityContext context = new VelocityContext();
                classScope.getScopes().forEach(context::put);

                InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream);
                velocityEngine.evaluate(context, writer, "ERROR", inputStreamReader);
              }

                WriteCommandAction.runWriteCommandAction(project, "Generate Test Code", "", () -> {
                  Objects.requireNonNull(testDocument).setReadOnly(false);
                  testDocument.setText(writer.toString());
                  PsiDocumentManager.getInstance(project).commitDocument(testDocument);
                  CodeStyleManager.getInstance(project).reformat(pTestFile, true);
                }, pTestFile);
            } catch (Exception e) {
              System.out.println(e.getMessage());
              e.printStackTrace();
              throw new RuntimeException(e);
            }


            fileEditorManager.openFile(vTestFile.get(), true);
        } catch (IOException e) {
          Project currentProject = event.getProject();
          Messages.showMessageDialog(currentProject, e.getMessage(), "Oops", Messages.getErrorIcon());
        }

      }
    }
  }

  /**
   * Determines whether this menu item is available for the current context.
   * Requires a project to be open.
   *
   * @param e Event received when the associated group-id menu is chosen.
   */
  @Override
  public void update(AnActionEvent e) {
    // Set the availability based on whether a project is open
    Project project = e.getProject();
    if (project!=null) {
      if (FileEditorManager.getInstance(project).getSelectedTextEditor()!=null &&
          !FileEditorManager.getInstance(project).getSelectedFiles()[0].getNameWithoutExtension().endsWith(CLASS_NAME_POSTFIX))
        e.getPresentation().setEnabledAndVisible(true);
      else {
        e.getPresentation().setVisible(true);
        e.getPresentation().setEnabled(false);
      }
    }else
      e.getPresentation().setEnabledAndVisible(false);
  }

}
