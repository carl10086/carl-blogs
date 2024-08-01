package com.cb.gradle.plugins;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.PluginContainer;

public class KotlinAppPlugin implements Plugin<Project> {

  @Override
  public void apply(Project project) {
    PluginContainer plugins = project.getPlugins();
    plugins.apply("cb-kotlin");
    plugins.apply("org.springframework.boot");
  }
}
