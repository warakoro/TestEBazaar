package subsystemtests;

import java.util.logging.Logger;

import middleware.exceptions.DatabaseException;
import junit.framework.TestCase;
import alltests.AllTests;
import business.customersubsystem.CustomerSubsystemFacade;
import business.exceptions.BackendException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CustomerSubsystem;
import dbsetup.DbQueries;

public class CustomerSSBuillingAddress extends TestCase{
	

	static String name = "Test Save address CustomerProfile";
	static Logger log = Logger.getLogger(CustomerSSBuillingAddress.class.getName());
	
	static {
		AllTests.initializeProperties();
	}
	
	
	public void testCatalogListStep()   {
		// Add row in CatalogType table for testing
		String[] vals = DbQueries.insertCustomerRow();
		String expectedBillingAddress = vals[2];
				
		// Perform test
        CustomerSubsystem css = new CustomerSubsystemFacade();
        Address address = null;
        String billingAddress = null;
        
			try {
				address = css.getDefaultBillingAddressForTest(Integer.parseInt(vals[1]));
				billingAddress = address.getStreet();
			} catch (BackendException | DatabaseException e) {
				e.printStackTrace();
			}
		finally {
			//assertTrue(address != null);
			boolean addressFound = false;
			if(billingAddress != null&&billingAddress.equals(expectedBillingAddress)){
				addressFound= true;				
			}
			assertTrue(addressFound);
			// Clean up table
			DbQueries.deleteAddressSql(Integer.parseInt(vals[1]));
			
		}
	}


}
