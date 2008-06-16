package org.apache.maven.integrationtests;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.it.util.ResourceExtractor;
import org.apache.maven.it.Verifier;

import java.io.File;

public class MavenITmng3536AppendedAbsolutePaths extends AbstractMavenIntegrationTestCase {
	
	public MavenITmng3536AppendedAbsolutePaths()
		throws InvalidVersionSpecificationException
	{
		super( "(2.0.9,)");
	}

    public void testitMNG3536() throws Exception {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(),
                                                                 "/mng-3536-appendedAbsolutePaths" );
        File pluginDir = new File( testDir, "plugin" );
        Verifier verifier = new Verifier( pluginDir.getAbsolutePath() );

        verifier.executeGoal( "install" );

        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        File projectDir = new File( testDir, "project" );
        verifier = new Verifier( projectDir.getAbsolutePath() );

        verifier.executeGoal( "verify" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}