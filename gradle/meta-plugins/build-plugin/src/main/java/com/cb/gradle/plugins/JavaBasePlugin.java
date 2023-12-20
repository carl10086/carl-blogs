package com.cb.gradle.plugins;

import static com.cb.gradle.plugins.Versions.JVM_VERSION;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.api.tasks.testing.Test;
import org.gradle.jvm.toolchain.JavaLanguageVersion;

/**
 * 负责所有 java 相关应用的最 base 的依赖
 * 1. 基础库版本，例如 spring boot 等等
 * 2.
 * @author carl
 */
public class JavaBasePlugin implements Plugin<Project> {

  @Override
  public void apply(Project project) {
    System.out.println("加载插件 JavaBasePlugin");

    var plugins = project.getPlugins();
//    var layout = project.getLayout();
//    var extensions = project.getExtensions();
    var tasks = project.getTasks();

    plugins.apply("java-library");
    var dependencies = project.getDependencies();
//    var configurations = project.getConfigurations();

    // 设置 JVM Toolchain
    var javaExt = project.getExtensions().getByType(JavaPluginExtension.class);
    javaExt.getToolchain().getLanguageVersion().set(JavaLanguageVersion.of(JVM_VERSION));

    tasks.named("test", Test.class, t -> {
      t.useJUnitPlatform();
      t.systemProperty("java.awt.headless", "true");
      t.systemProperty("testGroups", project.getProperties().get("testGroups"));
      t.systemProperty("io.netty.leakDetection.level", "paranoid");
      t.systemProperty("io.netty5.leakDetectionLevel", "paranoid");
      t.systemProperty("io.netty5.leakDetection.targetRecords", "32");
      t.systemProperty("io.netty5.buffer.lifecycleTracingEnabled", "true");
      t.systemProperty("io.netty5.buffer.leakDetectionEnabled", "true");
      t.jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED", "--add-opens=java.base/java.util=ALL-UNNAMED");
    });

    tasks.withType(JavaCompile.class).configureEach(t -> {
      t.getOptions().setEncoding("UTF-8");
    });

    /*3. finally, we set dependency*/
    dependencies.add("api",
                     dependencies.platform(
                         "org.springframework.boot:spring-boot-dependencies:" + Versions.SPRING_BOOT_VERSION)
    );

    System.out.println(
        "加载 spring boot:" + "org.springframework.boot:spring-boot-dependencies:" + Versions.SPRING_BOOT_VERSION);
    dependencies.add("api",
                     dependencies.platform(
                         "com.google.protobuf:protobuf-bom:" + Versions.PROTOBUF_VERSION)
    );

    dependencies.add("api",
                     dependencies.platform(
                         "io.grpc:grpc-bom:" + Versions.GRPC_VERSION)
    );

    dependencies.add("api",
                     dependencies.platform(
                         "com.linecorp.armeria:armeria-bom:" + Versions.ARMERIA)
    );

    dependencies.add("api",
                     dependencies.platform(
                         "io.opentelemetry:opentelemetry-bom:" + Versions.OPENTELEMTRY_VERSION)
    );

    dependencies.add("api",
                     dependencies.platform(
                         "org.http4k:http4k-bom:" + Versions.HTTP4K)
    );

    dependencies.add("testImplementation", "org.junit.jupiter:junit-jupiter");
    dependencies.add("testImplementation", "org.assertj:assertj-core");


  }
}
