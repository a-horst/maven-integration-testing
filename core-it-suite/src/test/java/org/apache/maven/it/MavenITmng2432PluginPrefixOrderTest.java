package org.apache.maven.it;

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

import java.io.File;

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

/**
 * This is a test set for <a href="http://jira.codehaus.org/browse/MNG-2432">MNG-2432</a>
 * 
 * @author Benjamin Bentmann
 * @version $Id$
 */
public class MavenITmng2432PluginPrefixOrderTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng2432PluginPrefixOrderTest()
    {
        super( "[3.0-alpha-2,)" );
    }

    /**
     * Verify that when resolving plugin prefixes the plugins from the POM are searched before the plugin groups
     * from the settings.
     */
    public void testitMNG2432()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-2432" );

        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.deleteArtifacts( "org.apache.maven.its.mng2432.pom" );
        verifier.deleteArtifacts( "org.apache.maven.its.mng2432.settings" );
        verifier.filterFile( "settings-template.xml", "settings.xml", "UTF-8", verifier.newDefaultFilterProperties() );
        verifier.getCliOptions().add( "--settings" );
        verifier.getCliOptions().add( "settings.xml" );
        verifier.executeGoal( "it:touch" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        verifier.assertFileNotPresent( "target/touch-settings.txt" );
        verifier.assertFilePresent( "target/touch-pom.txt" );
    }

}