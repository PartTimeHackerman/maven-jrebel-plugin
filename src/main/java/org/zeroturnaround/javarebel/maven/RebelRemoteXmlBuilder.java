package org.zeroturnaround.javarebel.maven;

import java.io.IOException;
import java.io.Writer;

public class RebelRemoteXmlBuilder extends RebelXmlBuilder {

    @Override
    public void writeXml(Writer writer) throws IOException {
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        writer.write("\n<!--\n"
                + "  This is the JRebel configuration file. It maps the running application to your IDE workspace, enabling JRebel reloading for this project.\n"
                + "  Refer to https://manuals.zeroturnaround.com/jrebel/standalone/config.html for more information.\n" + "-->\n");
        writer.write("<rebel-remote xmlns=\"http://www.zeroturnaround.com/rebel/remote\">\n");
        writer.write("\t<id>");
        writer.write("sa");
        writer.write("</id>");
        writer.write("</rebel-remote>");
        writer.flush();
    }

}
