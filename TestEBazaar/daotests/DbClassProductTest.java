package daotests;

import integrationtests.BrowseAndSelectTest;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import junit.framework.TestCase;
import alltests.AllTests;
import business.externalinterfaces.DbClassProductForTest;
import business.externalinterfaces.Product;
import business.externalinterfaces.ProductSubsystem;
import business.productsubsystem.ProductSubsystemFacade;
import dbsetup.DbQueries;

public class DbClassProductTest extends TestCase {
	
	static String name = "Product Subsystem Test";
	static Logger log = Logger.getLogger(BrowseAndSelectTest.class.getName());
	
	static {
		AllTests.initializeProperties();
	}
	
	public void testGetCatalogNames() {

		List<Integer> expectedList = DbQueries.readProductIds();
		
		
		ProductSubsystem pss = new ProductSubsystemFacade();
		DbClassProductForTest dbclass = pss.getGenericDbClassProductForTest();
		try {
			Product prod = dbclass.readProduct(1);	
			
			//the the readed product should be in the product lists
			assertTrue(expectedList.contains(prod.getProductId()));
			
		} catch(Exception e) {
			fail("Inserted value not found");
		} 
	
	}
	

}
