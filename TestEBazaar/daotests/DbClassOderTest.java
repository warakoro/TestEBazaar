package daotests;

import java.util.List;
import java.util.logging.Logger;

import junit.framework.TestCase;
import alltests.AllTests;
import business.customersubsystem.CustomerSubsystemFacade;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.DbClassCustomerProfileForTest;
import business.externalinterfaces.DbClassOrderForTest;
import business.externalinterfaces.Order;
import business.externalinterfaces.OrderItem;
import business.externalinterfaces.OrderSubsystem;
import business.ordersubsystem.OrderSubsystemFacade;
import dbsetup.DbQueries;

public class DbClassOderTest extends TestCase {
	
	static String name = "Read order List";
	static Logger log = Logger.getLogger(DbClassOderTest.class.getName());
	
	static {
		AllTests.initializeProperties();
	}
	
	
	public void testReadCustomerProfile() {
		List<OrderItem> expected = DbQueries.readOrderItems(1);
		
		//test real dbclass address
		CustomerSubsystem css = new CustomerSubsystemFacade();
		OrderSubsystem oss = new OrderSubsystemFacade(css.getGenericCustomerProfile());
		DbClassOrderForTest dbclass = oss.getGenericDbClassOrder();
		try {
			//dbclass.getOrderItems();
			List<OrderItem> found = dbclass.getOrderItems(orderId);			
			assertTrue(expected.size()==found.size());
			
		} catch(Exception e) {
			fail("Address Lists don't match");
		}
		
	}

}
