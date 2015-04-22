package daotests;


import java.util.logging.Logger;

import junit.framework.TestCase;
import alltests.AllTests;
import business.customersubsystem.CustomerSubsystemFacade;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.DbClassCustomerProfileForTest;
import dbsetup.DbQueries;

public class DbClassCustomerProfileTest extends TestCase{
	
	static String name = "Read cutomer profile test";
	static Logger log = Logger.getLogger(DbClassCustomerProfileTest.class.getName());
	
	static {
		AllTests.initializeProperties();
	}
	
	public void testReadCustomerProfile() {
		CustomerProfile expected = DbQueries.readCustProfile();
		
		//test real dbclass address
		CustomerSubsystem css = new CustomerSubsystemFacade();
		DbClassCustomerProfileForTest dbclass = css.getGenericDbClassCustomerProfile();
		try {
			dbclass.readCustomerProfile(1);
			CustomerProfile found = dbclass.getCustomerProfile();
			assertTrue(expected.equals(found));
			
		} catch(Exception e) {
			fail("Address Lists don't match");
		}
		
	}

}
