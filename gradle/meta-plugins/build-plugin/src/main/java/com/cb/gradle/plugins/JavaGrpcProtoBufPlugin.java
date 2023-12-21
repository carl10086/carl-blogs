package com.cb.gradle.plugins;

import com.google.protobuf.gradle.ProtobufExtension;
import com.google.protobuf.gradle.ProtobufPlugin;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;

public class JavaGrpcProtoBufPlugin implements Plugin<Project> {

  @Override
  public void apply(Project project) {
    var plugins = project.getPlugins();
//    plugins.apply("java-base");
    plugins.apply("java-library");

    var dependencies = project.getDependencies();

    dependencies.add("api", "io.grpc:grpc-services:"+Versions.GRPC_VERSION);
    dependencies.add("api", "io.grpc:grpc-protobuf:"+Versions.GRPC_VERSION);
    dependencies.add("api", "io.grpc:grpc-stub:"+Versions.GRPC_VERSION);
    dependencies.add("api", "com.google.protobuf:protobuf-java-util:" + Versions.PROTOBUF_VERSION);
    dependencies.add("api", "com.linecorp.armeria:armeria-grpc:"+Versions.ARMERIA);
    dependencies.add("compileOnly", "javax.annotation:javax.annotation-api:1.3.2"  );
//    dependencies.add("compileOnly", "javax.annotation:javax.annotation-api:");

    this.configureProtobuf(project);
    this.registerGenerateProtoJavaStubTask(project);
  }


  private void configureProtobuf(Project project) {
    var plugins = project.getPlugins();
    var extensions = project.getExtensions();
    plugins.apply("com.google.protobuf");
    project.getPluginManager().apply(ProtobufPlugin.class);
    var protobufVersion = Versions.PROTOBUF_VERSION;
    var grpcVersion = Versions.GRPC_VERSION;
    var protobufExtension = extensions.getByType(ProtobufExtension.class);
    protobufExtension.protoc(it -> {
      it.setArtifact("com.google.protobuf:protoc:" + protobufVersion);
    });
    protobufExtension.plugins(it -> {
      it.create("grpc", (g) -> {
        g.setArtifact("io.grpc:protoc-gen-grpc-java:" + grpcVersion);
      });
    });

    protobufExtension.generateProtoTasks((generateProtoTaskCollection -> generateProtoTaskCollection.all()
        .forEach(it -> {
          it.plugins(plugin -> {
            plugin.register("grpc");
          });
        }))
    );

    var sourceSetContainer = Objects.requireNonNull((SourceSetContainer) project.property("sourceSets"));
    var mainSourceSet = sourceSetContainer.getByName(SourceSet.MAIN_SOURCE_SET_NAME);
    var proto = (SourceDirectorySet) mainSourceSet.getExtensions().getByName("proto");
    proto.include("**/*.proto");
  }


  private void registerGenerateProtoJavaStubTask(Project project) {
    project.getTasks().register("generateProtoJavaStub", task -> {
      task.dependsOn("generateProto"); // 依赖于 generateProto 任务
      task.doFirst(it -> {
        generateStub(project);
      });
    });
    project.getTasks().named("compileJava").configure(compileJava -> compileJava.dependsOn("generateProtoJavaStub"));
  }

  private void generateStub(Project project) {
    var bd = project.getLayout().getBuildDirectory().getAsFile().get().toPath();

    var ps = bd.resolve("generated/source/proto/main/grpc");
    System.out.println(ps.toAbsolutePath());

    if (!Files.exists(ps)) {
      System.out.println("protobuf source directory not exists");
      return;
    }
    System.out.println("found proto generated source directory");
    try (var fs = Files.walk(ps)) {
      var files = fs
          .filter(path -> Files.isRegularFile(path) && path.getFileName().toString().endsWith(".java"))
          .map(it -> {
            String pkg;
            String cls = null;

            // 确定首行的 package
            try {
              var lines = Files.readAllLines(it);
              if (lines.isEmpty()) {
                return null;
              }

              var fl = lines.get(0);
              if (!fl.startsWith("package")) {
                return null;
              }
              pkg = fl.substring(8, fl.length() - 1);

              List<String> unary = new ArrayList<>();
              int mode = 0; // find class
              for (int i = 1; i < lines.size(); i++) {
                var cur = lines.get(i);

                switch (mode) {
                  case 0:
                    if (cur.startsWith("public final class") && cur.endsWith("Grpc {")) {
                      cls = cur.substring(19, cur.lastIndexOf('{') - 1);
                      mode = 1;
                    }
                    break;
                  case 1: // find blocking stub
                    if (cur.contains("io.grpc.stub.AbstractBlockingStub")) {
//                                        println(cur)
                      mode = 2; // parse blocking stub
                    }
                    break;
                  case 2:
//                                    println(cur)
                    if (cur.trim().startsWith("protect")) {
                      break;
                    }
                    if (cur.trim().startsWith("private")) {
                      break;
                    }
                    if (cur.trim().startsWith("public") && cur.trim().endsWith("{")) {
                      // found
                      var nl = cur.trim().replace("public", "").replace(" {", "");
//                                        println(nl)
                      unary.add(nl);
                      i = i + 3;
                      mode = 3;
                      break;
                    }
                    break;
                  case 3:
                    if (cur.trim().equals("}")) {
                      // end
//                                        println("end .......")
                      mode = 1;
                      break;
                    }
                    mode = 2;
                    break;
                  default:
                    break;
                }
              }
              // build
              if (cls == null) {
                return null;
              }
              if (unary.isEmpty()) {
                return null;
              }

              var sb = new StringBuilder();
              sb.append("package ").append(pkg).append(";\n\n");

              var service = cls.replace("Grpc", "");
              sb.append("public interface ").append(service).append(" {\n\n");

//                    sb.append("  default io.grpc.ServerServiceDefinition bindService() {\n" +
//                            "    throw new IllegalStateException();\n" +
//                            "  }\n")

              // methods
              for (String s : unary) {
                s = s.trim();
//                            println(s)
                var ss = s.split(" ");
                var rt = ss[0];
                var mds = ss[1].split("\\(");
                var name = mds[0];
                var pt = mds[1].split(",")[0];

                sb.append("  ").append(rt).append(" ").append(name).append("(").append(pt).append(" request);\n\n");
              }

              sb.append("  // end unary calls.\n\n");

              // provider
              sb.append("  static ").append("io.grpc.BindableService newProvider(").append(service)
                  .append(" service) {\n")
                  .append("    return new ").append(pkg).append(".").append(service).append("Grpc.").append(service)
                  .append("ImplBase() {\n");

              for (String s : unary) {
                s = s.trim();
                var ss = s.split(" ");
                var rt = ss[0];
                var mds = ss[1].split("\\(");
                var name = mds[0];
                var pt = mds[1].split(",")[0];

                sb.append("        @Override\n");

                sb.append("        public void ").append(name).append("(").append(pt).append(" request, ")
                    .append("io.grpc.stub.StreamObserver<").append(rt).append("> responseObserver").append(" ) {\n");
                sb.append("          responseObserver.onNext(service.").append(name).append("(").append("request)")
                    .append(");\n");
                sb.append("          responseObserver.onCompleted();\n");
                sb.append("        }\n\n");
              }

              sb.append("    };\n")
                  .append("  }\n\n");

              // stub class
              sb.append("  ").append("java.lang.Class<").append(pkg).append(".").append(service).append("Grpc.")
                  .append(service).append("BlockingStub> STUB_CLASS = ")
                  .append(pkg).append(".").append(service).append("Grpc.").append(service).append("BlockingStub.class")
                  .append(";\n");

              sb.append("  static ").append(service).append(" ")
                  .append("newConsumer(com.linecorp.armeria.client.grpc.GrpcClientBuilder factory)").append(" {\n")
                  .append("    ").append("return new Consumer(factory.build(STUB_CLASS))").append(";\n")
                  .append("  }\n");
              // end stub class

              // consumer
              sb.append("\n")
                  .append("  final class Consumer implements ").append(service).append(" {\n")
                  .append("\n")
                  .append("    private final ").append(pkg).append(".").append(service).append("Grpc.").append(service)
                  .append("BlockingStub stub;\n")
                  .append("\n")
                  .append("    public Consumer(").append(pkg).append(".").append(service).append("Grpc.")
                  .append(service).append("BlockingStub stub)").append(" {\n")
                  .append("      this.stub = stub;\n")
                  .append("    }\n")
                  .append("\n");
              // methods
              for (String s : unary) {
                s = s.trim();
                var ss = s.split(" ");
                var rt = ss[0];
                var mds = ss[1].split("\\(");
                var name = mds[0];
                var pt = mds[1].split(",")[0];

                sb.append("    public ").append(rt).append(" ").append(name).append("(").append(pt)
                    .append(" request) {\n");
                sb.append("      return this.stub.").append(name).append("(").append("request").append(");\n");
                sb.append("    }\n\n");
              }

              sb.append("  }\n");

              // end
              sb.append("}\n");

              var stub = sb.toString();
//                    println(stub); // for debug

              var newPath = it.getParent().resolve(service + ".java");
              Files.writeString(newPath, stub);
              return stub;
            } catch (Exception e) {
              System.out.println(e.getMessage());
              return null;
            }
          })
          .toList();
      System.out.println("generateProtoJavaStub done. total: " + files.size());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
