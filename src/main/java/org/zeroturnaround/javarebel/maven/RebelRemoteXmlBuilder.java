package org.zeroturnaround.javarebel.maven;

import org.zeroturnaround.javarebel.maven.model.RebelClasspathResource;

import java.io.IOException;
import java.io.Writer;

public class RebelRemoteXmlBuilder extends RebelXmlBuilder {

    @Override
    public void writeXml(Writer writer) throws IOException {
        String dir = ((RebelClasspathResource)classpathDir.get(0)).getDirectory();
        String[] dirArr = dir.split("/");
        String moduleName =  dirArr[dirArr.length-1];

        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        writer.write("\n<!--\n"
                + "  This is the JRebel configuration file. It maps the running application to your IDE workspace, enabling JRebel reloading for this project.\n"
                + "  Refer to https://manuals.zeroturnaround.com/jrebel/standalone/config.html for more information.\n" + "-->\n");
        writer.write("<rebel-remote xmlns=\"http://www.zeroturnaround.com/rebel/remote\">\n");
        writer.write("\t<id>");
        writer.write(moduleName);
        writer.write("</id>");
        writer.write("</rebel-remote>");
        writer.flush();
    }

}
