// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.github.swankdave.fastest;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.swankdave.fastest.java.JavaClassScope;
import com.github.swankdave.fastest.javascript.JavascriptClassScope;
import com.github.swankdave.fastest.kotlin.KotlinClassScope;
import com.github.swankdave.fastest.typescript.TypescriptClassScope;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Objects;

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

      var testDir = Paths.get(file.getPath()).getParent().resolve(Constants.TEST_NAMESPACE);
      var targetFilePath = testDir.resolve(testDir + "/" + file.getNameWithoutExtension() + "test.shortest." + file.getExtension());

      if (selectedTextEditor != null) {
        var psiFile = PsiManager.getInstance(project).findFile(file);
        ClassScope classScope =
          switch (Objects.requireNonNull(psiFile).getFileType().getName()) {
            case "JAVA" -> new JavaClassScope(psiFile);
            case "Kotlin" -> new KotlinClassScope(psiFile);
            case "JavaScript" -> new JavascriptClassScope(psiFile);
            case "TypeScript" -> new TypescriptClassScope(psiFile);
            default -> throw new RuntimeException("I don't know what to do with this file yet");
          };

        try (InputStream resourceAsStream =
                     getClass().getClassLoader().getResourceAsStream(classScope.getMustacheTemplateFilename())) {
          assert resourceAsStream != null : "failed to load template file";
          var mustacheInstance = new DefaultMustacheFactory().compile(new InputStreamReader(resourceAsStream), "root");

          assert testDir.toFile().exists() || testDir.toFile().mkdir() : "Failed to create directory, probably something to do with file permissions surrounding a test directory";
          assert !targetFilePath.toFile().exists() || targetFilePath.toFile().delete() : "failed to delete existing output file";
          assert targetFilePath.toFile().createNewFile() : "failed to create output file";

          try (FileOutputStream out = new FileOutputStream(targetFilePath.toString())) {
            try (PrintWriter testFile = new PrintWriter(out)) {
              mustacheInstance.execute(testFile, classScope.getScopes());
              testFile.flush();
            }
            out.flush();
          }

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
          !FileEditorManager.getInstance(project).getSelectedFiles()[0].getNameWithoutExtension().endsWith("test.shortest"))
        e.getPresentation().setEnabledAndVisible(true);
      else {
        e.getPresentation().setVisible(true);
        e.getPresentation().setEnabled(false);
      }
    }else
      e.getPresentation().setEnabledAndVisible(false);
  }

}
