package com.simple.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class Main {

	private static final String ATTRIBUTE_STANDALONE_JAR = "Standalone-Jar";
	private static final String ATTRIBUTE_PACKAGE_NAME = "Package-Name";

	public static void main(String[] args) throws Exception {
		Server server = new Server();

		ServerConnector connector = new ServerConnector(server);
		connector.setPort(8080);
		server.addConnector(connector);

		boolean standaloneJarMode = containsAttributeInManifest(ATTRIBUTE_STANDALONE_JAR);

		WebAppContext context = new WebAppContext();
		context.setDescriptor(standaloneJarMode ? "/WEB-INF/web.xml" : "src/main/webapp/WEB-INF/web.xml");

		if (standaloneJarMode) {
			context.setResourceBase(createResourceBaseForStandlalone());
		} else {
			context.setResourceBase("src/main/webapp");
		}
		context.setContextPath("/");

		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			// fix for Windows, so Jetty doesn't lock files
			context.getInitParams().put("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
		}

		server.setHandler(context);
		server.start();
		server.join();
	}

	private static String createResourceBaseForStandlalone() {
		String result;

		URL baseUrl = Main.class.getResource("");
		String fullBasePath = baseUrl.toString();
		String packageName = getAttributeFromManifest(ATTRIBUTE_PACKAGE_NAME);
		String packageNameWithSlashes = packageName.replaceAll("\\.", "/");

		int idx = fullBasePath.lastIndexOf(packageNameWithSlashes);
		result = fullBasePath.substring(0, idx);

		return result;
	}

	private static boolean containsAttributeInManifest(String attributeName) {
		return getAttributeFromManifest(attributeName) != null;
	}

	private static String getAttributeFromManifest(String attributeName) {
		String result = null;

		Enumeration<URL> resources = null;
		try {
			resources = Main.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (resources != null) {
			while (resources.hasMoreElements()) {
				try {
					Manifest manifest = new Manifest(resources.nextElement().openStream());
					Attributes attributes = manifest.getMainAttributes();
					if (attributes != null) {
						result = attributes.getValue(attributeName);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}
}
