package org.zeroturnaround.javarebel.maven.model;

import org.apache.maven.project.MavenProject;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class ImlFacetAppender {

    private final SAXReader reader = new SAXReader();
    private Element JRebelFacet;
    private final String JRebelFacetString = "<facet type=\"JRebel\" name=\"JRebel\">\n" +
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

        File baseDir = mavenProject.getBasedir();

        URL baseDirUrl;
        try {
            baseDirUrl = baseDir.toURI().toURL();
        } catch (MalformedURLException e) {
            System.out.println("Wrong module path :" + mavenProject.getBasedir());
            return;
        }

        String moduleName = mavenProject.getName();
        if (moduleName.contains(":")) {
            String[] moduleNameArr = moduleName.split(":");
            moduleName = moduleNameArr[moduleNameArr.length - 1];
        }

        String imlFile = moduleName + ".iml";
        URL imlDirUrl;
        try {
            imlDirUrl = new URL(baseDirUrl.getPath() + "/" + imlFile);
        } catch (MalformedURLException e) {
            System.out.println(imlFile + "not found, ignoring module.");
            return;
        }

        Document iml = null;
        try {
            iml = parse(imlDirUrl);
        } catch (DocumentException e) {
            System.out.println(e);
        }



        Element facetManagerComponent = getFacetManagerComponent(iml);
        removeOldJrebelFacet(facetManagerComponent);
        addJrebelFacet(facetManagerComponent);

        FileWriter out;
        try {
            out = new FileWriter(imlFile);
            iml.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Document parse(URL url) throws DocumentException {
        JRebelFacet = DocumentHelper.parseText(JRebelFacetString).getRootElement();
        Document document = reader.read(url);
        return document;
    }

    public Element getFacetManagerComponent(Document document) {
        List<Node> list = document.selectNodes("//component/@name");
        for (Iterator<Node> node = list.iterator(); node.hasNext(); ) {
            Attribute attribute = (Attribute) node.next();
            if ("FacetManager".equals(attribute.getValue()))
                return attribute.getParent();
        }
        return null;
    }

    public void addJrebelFacet(Element facetManager){
        facetManager.add(JRebelFacet);
    }

    public void removeOldJrebelFacet(Element facetManager){
        List<Node> facets = facetManager.selectNodes("//facet/@name");

        for (Iterator<Node> node = facets.iterator(); node.hasNext(); ) {
            Attribute attribute = (Attribute) node.next();
            if ("JRebel".equals(attribute.getValue()))
                attribute.getParent().detach();
        }

    }

}
