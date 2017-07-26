package org.zeroturnaround.javarebel.maven;

import org.apache.maven.project.MavenProject;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class ImlFacetAppender {

    private final SAXReader reader = new SAXReader();
    private final String imlRootString = "" +
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "\n" +
            "<module org.jetbrains.idea.maven.project.MavenProjectsManager.isMavenModule=\"true\" type=\"JAVA_MODULE\" version=\"4\"> \n" +
            "</module>";
    private final String JRebelFacetString = "" +
            "<facet type=\"JRebel\" name=\"JRebel\">\n" +
            "      <configuration>\n" +
            "        <option name=\"deleteUnindexed\" value=\"true\" />\n" +
            "        <option name=\"httpAuthEnabled\" value=\"false\" />\n" +
            "        <option name=\"httpPassword\" />\n" +
            "        <option name=\"httpUsername\" />\n" +
            "        <option name=\"ideModuleStorage\">\n" +
            "          <map>\n" +
            "            <entry key=\"com.zeroturnaround.jrebel.FormatVersion\" value=\"7.0.0\" />\n" +
            "            <entry key=\"com.zeroturnaround.jrebel.remoting.DeleteUnindexedFiles\" value=\"false\" />\n" +
            "            <entry key=\"com.zeroturnaround.jrebel.remoting.ModuleRemoteServerPreviousEnabledSelection\" value=\"instance\" />\n" +
            "            <entry key=\"com.zeroturnaround.jrebel.remoting.ModuleRemoteServerSelection\" value=\"instance\" />\n" +
            "          </map>\n" +
            "        </option>\n" +
            "        <option name=\"manualXML\" value=\"false\" />\n" +
            "        <option name=\"masterModuleName\" />\n" +
            "        <option name=\"remotingEnabled\" value=\"true\" />\n" +
            "        <option name=\"secure\" value=\"true\" />\n" +
            "        <option name=\"url\" />\n" +
            "        <option name=\"xmlDir\" />\n" +
            "      </configuration>\n" +
            "    </facet>";


    public void writeIml(MavenProject mavenProject) {
        writeIml(mavenProject, true);
    }

    public void writeIml(MavenProject mavenProject, Boolean createIml) {

        URI baseDirUri = mavenProject.getBasedir().toURI();
        String imlFileName = mavenProject.getName() + ".iml";

        URL imlDirUrl = getImlDirUrl(baseDirUri, imlFileName);
        if (imlDirUrl == null)
            return;

        Document iml = getIml(imlDirUrl, createIml);
        if (iml == null)
            return;

        addFacetManagerComponent(iml);
        removeOldJrebelFacet(iml);
        addJrebelFacet(iml);
        save(iml, imlDirUrl.getPath());
    }

    private URL getImlDirUrl(URI baseDirUri, String imlFile) {
        try {
         return new URL(baseDirUri.toString() + imlFile);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    private Document getIml(URL imlDirUrl, Boolean createIml) {
        try {
            return parse(imlDirUrl);
        } catch (DocumentException e) {
            if (!createIml) {
                System.out.println(imlDirUrl + "not found, ignoring module.");
                return null;
            } else {
                return createImlRoot();
            }
        }
    }

    private Document createImlRoot() {
        try {
            return DocumentHelper.parseText(imlRootString);
        } catch (DocumentException e1) {
            e1.printStackTrace();
            return DocumentHelper.createDocument();
        }
    }

    private Document parse(URL url) throws DocumentException {
        Document document = reader.read(url);
        return document;
    }


    private void addFacetManagerComponent(Document document) {
        List<Node> list = document.selectNodes("//component[@name='FacetManager']");
        if (!list.isEmpty())
            return;
        document.getRootElement().addElement("component").addAttribute("name", "FacetManager");
    }


    private void removeOldJrebelFacet(Document iml) {
        List<Node> facets = iml.selectNodes("//facet[@name='JRebel']");
        for (Node facet : facets) {
            facet.detach();
        }
    }

    private void addJrebelFacet(Document iml) {
        Element JRebelFacet;
        try {
            JRebelFacet = DocumentHelper.parseText(JRebelFacetString).getRootElement();
        } catch (DocumentException e) {
            e.printStackTrace();
            return;
        }
        iml.selectSingleNode("//component[@name='FacetManager']").getParent().add(JRebelFacet);
    }

    private void save(Document iml, String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(fos, format);
            writer.write(iml);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
