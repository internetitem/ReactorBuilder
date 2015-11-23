ReactorBuilder
==============

Programmatically build a Maven Reactor project

There are currently two "runnable" versions of this project:
* A command-line runner which takes command line parameters
* A Maven Mojo

The command-line runner is mostly for demonstration and testing purposes, as the Mojo is significantly easier to use
(you don't need to worry about the classpath, for example).

The Mojo can either be used from the command line with no POM, or can be configured using a POM.

Using the POM version looks like this:

        <build>
            <plugins>
                <plugin>
                    <groupId>com.internetitem</groupId>
                    <artifactId>create-reactor-project-maven-plugin</artifactId>
                    <version>1.0.1</version>
                    <executions>
                        <execution>
                            <id>create-reactor</id>
                            <phase>generate-sources</phase>
                            <goals>
                                <goal>create-reactor-project</goal>
                            </goals>
                            <configuration>
                                <xmlns></xmlns> <!-- Defaults to http://maven.apache.org/POM/4.0.0 -->
                                <templateFile></templateFile> <!-- non-default POM to start from -->
                                <groupId></groupId> <!-- If set, add a <groupId> element -->
                                <artifactId></artifactId> <!-- If set, add a <artifactId> element -->
                                <version></version> <!-- If set, add a <version> element -->
                                <packaging></packaging> <!-- defaults to pom -->
                                <prependModules></prependModules> <!-- Hardcoded list of modules to prepend to the "discovered" module list (separated by ":") -->
                                <appendModules></appendModules>  <!-- Hardcoded list of modules to append to the "discovered" module list (separated by ":") -->
                                <relativeTo></relativeTo> <!-- Make paths relative to the given directory --> 
                                <outputFile></outputFile> <!-- File to write the POM to (otherwise standard output is used) -->
                                <moduleSearchDirectories></moduleSearchDirectories>  <!-- List of directories to search for modules
                                   (non-recursive search for directories containing a pom.xml).
                                   Default to the current directory (separated by ":") -->
                            </configuration>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </build>

Note that all of the above configuration elements are optional.

To specify any of the above options from the command line, use a property consisting of "`create-reactor.`" followed by the element name.
For example, to specify the outputFile, you would use `-Dcreate-reactor.outputFile=whatever`.

To run the Mojo from the command line without any existing POM, you can run something like this:

    mvn \
       -Dcreate-reactor.relativeTo=/some/path \
       -Dcreate-reactor.moduleSearchDirectories=/some/path \
       -Dcreate-reactor.outputFile=/some/path/pom.xml \
       -Dcreate-reactor.groupId=com.my.group \
       -Dcreate-reactor.artifactId=MyArtifact \
       -Dcreate-reactor.version=1.0.0 \
       com.internetitem:create-reactor-project-maven-plugin:1.0.1:create-reactor-project

The above invocation would be equivalent to the following `<configuration>` in a POM:

    <configuration>
        <groupId>com.my.group</groupId>
        <artifactId>MyArtifact</artifactId>
        <version>1.0.0</version>
        <relativeTo>/some/path</relativeTo> 
        <outputFile>/some/path/pom.xml</outputFile>
        <moduleSearchDirectories>/some/path</moduleSearchDirectories>
    </configuration>
