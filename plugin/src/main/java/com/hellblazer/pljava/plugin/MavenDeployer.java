package com.hellblazer.pljava.plugin;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

public class MavenDeployer extends AbstractMojo {
    /**
     * Location of the file.
     * 
     * @parameter expression="${project.build.directory}"
     * @required
     */
    @SuppressWarnings("unused")
    private File outputDirectory;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        // TODO Auto-generated method stub

    }

}
