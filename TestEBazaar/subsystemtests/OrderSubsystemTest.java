package subsystemtests;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import middleware.exceptions.DatabaseException;
import junit.framework.TestCase;
import alltests.AllTests;
import business.customersubsystem.CustomerSubsystemFacade;
import business.exceptions.BackendException;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.Order;
import business.externalinterfaces.OrderSubsystem;
import business.ordersubsystem.OrderSubsystemFacade;
import dbsetup.DbQueries;
public class OrderSubsystemTest extends TestCase{
    static String name = "Order Subsystem Test";
    static Logger log = Logger.getLogger(OrderSubsystemTest.class.getName());
    static {
        AllTests.initializeProperties();
    }
    public void testGetOrderHistory() {
        List<Integer> expected = DbQueries.readOrderHistory();
        CustomerSubsystem css = new CustomerSubsystemFacade();
        OrderSubsystem scs = new OrderSubsystemFacade(css.getGenericCustomerProfile());
        try {
            List<Integer> found = new LinkedList<Integer>();
            try {
                for (Order o : scs.getOrderHistory()) {
                    Integer orderid = o.getOrderId();
                    found.add(orderid);
                }
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
            assertTrue(expected.size() == found.size());
        }
        catch (BackendException e) {
            e.printStackTrace();
        }
    }
}