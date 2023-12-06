package com.cb.gradle.plugins;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class JavaBaseLibraryPlugin implements Plugin<Project> {

  @Override
  public void apply(Project project) {
    var plugins = project.getPlugins();
    plugins.apply("java-library");
    plugins.apply("cb-java-base");
  }
}
