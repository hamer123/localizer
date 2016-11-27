package com.pw.localizer.singleton;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class ResourceDirectionStartup implements Serializable{

	@Inject
	private LocalizerProperties localizerProperties;

	public void initializeResourcesDirections(@Observes @Initialized(ApplicationScoped.class) Object init) throws IOException {
		createRootDirection();
		createOtherDirections();
	}

	private void createOtherDirections() throws IOException {
		createDirection(localizerProperties.ResourcePath().AVATAR);
	}

	private void createRootDirection() throws IOException {
		createDirection(localizerProperties.ResourcePath().RESOURCE_MAIN);
		createDirection(localizerProperties.ResourcePath().IMAGE);
		createDirection(localizerProperties.ResourcePath().VIDEO);
	}

	private void createDirection(String resourcePath) throws IOException {
		Path path = Paths.get(resourcePath);
		if(!Files.exists(path) || !Files.isDirectory(path)){
			Files.createDirectories(path);
		}
	}
}
