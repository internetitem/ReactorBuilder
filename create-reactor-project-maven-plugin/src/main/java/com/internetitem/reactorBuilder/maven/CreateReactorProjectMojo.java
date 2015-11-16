package com.internetitem.reactorBuilder.maven;

import com.internetitem.reactorBuilder.*;
import com.internetitem.reactorBuilder.config.Configuration;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;

@Mojo(name = "create-reactor-project", defaultPhase = LifecyclePhase.GENERATE_RESOURCES, threadSafe = true)
public class CreateReactorProjectMojo extends AbstractMojo {

	/**
	 * Reactor configuration
	 */
	@Parameter(property = "reactorConfiguration", required = false)
	private Configuration reactorConfiguration;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (reactorConfiguration == null) {
			reactorConfiguration = new Configuration();
		}
		Inputter inputter = new FileInputter();
		Outputter outputter = new FileOutputter();
		ModuleLister lister = new FilesystemModuleLister();
		ReactorBuilder builder = new ReactorBuilder(inputter, outputter, lister);
		try {
			builder.buildReactorProject(reactorConfiguration);
		} catch (IOException e) {
			throw new MojoExecutionException("Failed to generate Reactor Project: " + e.getMessage(), e);
		}
	}
}
