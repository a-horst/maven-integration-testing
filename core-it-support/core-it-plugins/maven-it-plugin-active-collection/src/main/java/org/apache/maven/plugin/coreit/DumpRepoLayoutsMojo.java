package org.apache.maven.plugin.coreit;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Dumps the role hints of the available repository layouts to a properties file.
 * 
 * @goal dump-repo-layouts
 * @phase validate
 * 
 * @author Benjamin Bentmann
 */
public class DumpRepoLayoutsMojo
    extends AbstractMojo
{

    /**
     * Project base directory used for manual path alignment.
     * 
     * @parameter default-value="${basedir}"
     * @readonly
     */
    private File basedir;

    /**
     * The available repository layouts, as a map.
     * 
     * @component role="org.apache.maven.artifact.repository.layout.ArtifactRepositoryLayout"
     */
    private Map repositoryLayouts;

    /**
     * The available repository layouts, as a list.
     * 
     * @component role="org.apache.maven.artifact.repository.layout.ArtifactRepositoryLayout"
     */
    private List repoLayouts;

    /**
     * The path to the properties file used to dump the repository layouts.
     * 
     * @parameter property="collections.layoutsFile"
     */
    private File layoutsFile;

    /**
     * Runs this mojo.
     * 
     * @throws MojoFailureException If the output file could not be created.
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        Properties layoutProperties = new Properties();

        getLog().info( "[MAVEN-CORE-IT-LOG] Dumping repository layouts" );

        layoutProperties.setProperty( "layouts", Integer.toString( repositoryLayouts.size() ) );

        for ( Iterator it = repositoryLayouts.keySet().iterator(); it.hasNext(); )
        {
            String roleHint = (String) it.next();
            Object repoLayout = repositoryLayouts.get( roleHint );
            if ( repoLayout != null )
            {
                layoutProperties.setProperty( "layouts." + roleHint, repoLayout.getClass().getName() );
            }
            else
            {
                layoutProperties.setProperty( "layouts." + roleHint, "" );
            }
            getLog().info( "[MAVEN-CORE-IT-LOG]   " + roleHint + " = " + repoLayout );
        }

        if ( repoLayouts.size() != repositoryLayouts.size() )
        {
            throw new MojoExecutionException( "Inconsistent collection: " + repoLayouts + " vs " + repositoryLayouts );
        }

        if ( !layoutsFile.isAbsolute() )
        {
            layoutsFile = new File( basedir, layoutsFile.getPath() );
        }

        getLog().info( "[MAVEN-CORE-IT-LOG] Creating output file " + layoutsFile );

        OutputStream out = null;
        try
        {
            layoutsFile.getParentFile().mkdirs();
            out = new FileOutputStream( layoutsFile );
            layoutProperties.store( out, "MAVEN-CORE-IT-LOG" );
        }
        catch ( IOException e )
        {
            throw new MojoExecutionException( "Output file could not be created: " + layoutsFile, e );
        }
        finally
        {
            if ( out != null )
            {
                try
                {
                    out.close();
                }
                catch ( IOException e )
                {
                    // just ignore
                }
            }
        }

        getLog().info( "[MAVEN-CORE-IT-LOG] Created output file " + layoutsFile );
    }

}
