package com.cb.gradle.plugins;

import static com.cb.gradle.plugins.Versions.JVM_VERSION;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.jvm.toolchain.JavaLanguageVersion;
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension;

public class KotlinBasePlugin implements Plugin<Project> {

  @Override
  public void apply(Project project) {
    var plugins = project.getPlugins();

    plugins.apply("org.jetbrains.kotlin.jvm");
    plugins.apply("org.jetbrains.kotlin.kapt");
    plugins.apply("cb-java-base");
    /*1. set base kotlin config*/
    var extensions = project.getExtensions();
//    var kotlin = extensions.getByType(KotlinJvmProjectExtension.class);
//    kotlin.jvmToolchain(JVM_VERSION);

    var kotlin = extensions.getByType(KotlinJvmProjectExtension.class);
    kotlin.jvmToolchain(toolchain -> {
      toolchain.getLanguageVersion().set(JavaLanguageVersion.of(JVM_VERSION));
    });
    var dependencies = project.getDependencies();

    dependencies.add("implementation", "org.jetbrains.kotlin:kotlin-stdlib:" + Versions.KOTLIN);
    dependencies.add("implementation", "org.jetbrains.kotlin:kotlin-reflect:" + Versions.KOTLIN);



  }
}
