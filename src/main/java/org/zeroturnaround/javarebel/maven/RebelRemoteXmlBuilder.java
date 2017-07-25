package org.zeroturnaround.javarebel.maven;

import org.apache.maven.project.MavenProject;
import org.zeroturnaround.javarebel.maven.model.RebelClasspathResource;

import java.io.IOException;
import java.io.Writer;

public class RebelRemoteXmlBuilder extends RebelXmlBuilder {

    private MavenProject mavenProject;

    public RebelRemoteXmlBuilder(MavenProject mavenProject) {
        super();
        this.mavenProject = mavenProject;
    }

    @Override
    public void writeXml(Writer writer) throws IOException {
        String moduleName = mavenProject.getName();
        if (moduleName.contains(":")) {
            String[] moduleNameArr = moduleName.split(":");
            moduleName = moduleNameArr[moduleNameArr.length - 1];
        }

        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        writer.write("\n<!--\n"
                + "  This is the JRebel remote configuration file. It maps the running application to your IDE workspace, enabling JRebel reloading for this project.\n"
                + "  Refer to https://manuals.zeroturnaround.com/jrebel/standalone/config.html for more information.\n" + " -->\n");
        writer.write("<rebel-remote xmlns=\"http://www.zeroturnaround.com/rebel/remote\">\n");
        writer.write("\t<id>");
        writer.write(moduleName);
        writer.write("</id>\n");
        writer.write("</rebel-remote>");
        writer.flush();
    }

}
