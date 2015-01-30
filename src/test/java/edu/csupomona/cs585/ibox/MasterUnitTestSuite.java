package edu.csupomona.cs585.ibox;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import edu.csupomona.cs585.ibox.sync.GoogleDriveFileSyncManagerTest;

/**
 * A test-suite that calls two other test-suites.
 * 
 * @version $Id: MasterTestSuite.java 552 2010-03-06 11:48:47Z paranoid12 $
 */
@RunWith(value = Suite.class)
@SuiteClasses(value = { GoogleDriveFileSyncManagerTest.class,
		WatchDirTest.class, AppTestInt.class })
public class MasterUnitTestSuite {
}