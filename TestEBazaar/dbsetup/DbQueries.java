package dbsetup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;











import business.customersubsystem.CustomerSubsystemFacade;
import business.exceptions.BackendException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.Order;
import business.externalinterfaces.OrderItem;
import business.externalinterfaces.Product;
import business.ordersubsystem.OrderSubsystemFacade;
import business.productsubsystem.ProductImpl;
import middleware.DbConfigProperties;import middleware.externalinterfaces.DbConfigKey;
import alltests.*;
public class DbQueries {
	static {
		AllTests.initializeProperties();
	}
	static final DbConfigProperties PROPS = new DbConfigProperties();
	static Connection con = null;
	static Statement stmt = null;
	static final String USER = PROPS.getProperty(DbConfigKey.DB_USER.getVal()); 
    static final String PWD = PROPS.getProperty(DbConfigKey.DB_PASSWORD.getVal()); 
    static final String DRIVER = PROPS.getProperty(DbConfigKey.DRIVER.getVal());
    static final int MAX_CONN = Integer.parseInt(PROPS.getProperty(DbConfigKey.MAX_CONNECTIONS.getVal()));
    static final String PROD_DBURL = PROPS.getProperty(DbConfigKey.PRODUCT_DB_URL.getVal());
    static final String ACCT_DBURL = PROPS.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
	static Connection prodCon = null;
	static Connection acctCon = null;
    String insertStmt = "";
	String selectStmt = "";
	
	/* Connection setup */
	static {
		try {
			Class.forName(DRIVER);
		}
		catch(ClassNotFoundException e){
			//debug
			e.printStackTrace();
		}
		try {
			prodCon = DriverManager.getConnection(PROD_DBURL, USER, PWD);
			acctCon = DriverManager.getConnection(ACCT_DBURL, USER, PWD);
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	// just to test this class
	public static void testing() {
		try {
			stmt = prodCon.createStatement();
			stmt.executeQuery("SELECT * FROM Product");
			stmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	//////////////// The public methods to be used in the unit tests ////////////
	/**
	 * Returns a String[] with values:
	 * 0 - query
	 * 1 - product id
	 * 2 - product name
	 */
	public static String[] insertProductRow() {
		String[] vals = saveProductSql();
		String query = vals[0];
		try {
			stmt = prodCon.createStatement();
			
			stmt.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if(rs.next()) {
				vals[1] = (new Integer(rs.getInt(1)).toString());
			}
			stmt.close();
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return vals;
	}
	
	public static List<Integer> readProductIds() {
		
		List<Integer> prodIds = new ArrayList<Integer>();
		String query = "SELECT * FROM PRODUCT";
		try {
			stmt = prodCon.createStatement();			
			ResultSet rs = stmt.executeQuery(query);
			Product prod;
			
			while(rs.next()) {
				
				int prodId = Integer.parseInt(rs.getString("productid"));
				prodIds.add(prodId);
			}
			stmt.close();			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return prodIds;
	}
	
	/**
	 * Returns a String[] with values:
	 * 0 - query
	 * 1 - catalog id
	 * 2 - catalog name
	 */
	
	public static List<Address> readCustAddresses() {
		String query = readAddressesSql();
		List<Address> addressList = new LinkedList<Address>();
		try {
			stmt = acctCon.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			    
                while(rs.next()) {
                    
                    String street = rs.getString("street");
                    String city = rs.getString("city");
                    String state = rs.getString("state");
                    String zip = rs.getString("zip");
                    
             
                    Address addr 
                      = CustomerSubsystemFacade.createAddress(street,city,state,zip,true,true);
                   
                    addressList.add(addr);
                }  
                stmt.close();        
		}
		catch(SQLException e) {
			e.printStackTrace();
			
		}
		return addressList;
		
	}
	
/*My tests*/	
	public static CustomerProfile readCustProfile() {
		String query = readCustomerProfileSql();
		CustomerProfile cust = null;
		try {
			stmt = acctCon.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if(rs.next()) {
                    String custid = rs.getString("custid");
                    String fname = rs.getString("fname");
                    String lname = rs.getString("lname");            
                     cust = CustomerSubsystemFacade.createCustProfile(Integer.parseInt(custid), fname, lname, true);
                   
                }  
                stmt.close();        
		}
		catch(SQLException e) {
			e.printStackTrace();
			
		}
		return cust;
	}
	
	//for testing read orders
	
	public static List<OrderItem> readOrderItem() {
		String query = readOrderItemsSql();

		List<OrderItem> orderItems = new LinkedList<OrderItem>();
		try {
			stmt = acctCon.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
                    String prodId = rs.getString("productid");
                    String orderId = rs.getString("orderid");
                    String quantityReq = rs.getString("quantity");        
                    String totalPrice = rs.getString("totalPrice");           
                     try {
						OrderItem ord = OrderSubsystemFacade.createOrderItem(Integer.parseInt(prodId), Integer.parseInt(orderId), Integer.parseInt(quantityReq), Double.parseDouble(totalPrice));
					    orderItems.add(ord);
                     } catch (NumberFormatException | BackendException e) {
						e.printStackTrace();
					}
                   
                }  
                stmt.close();        
		}
		catch(SQLException e) {
			e.printStackTrace();
			
		}
		return orderItems;
	}
	
	/**
	 * Returns a String[] with values:
	 * 0 - query
	 * 1 - catalog id
	 * 2 - catalog name
	 */
	public static String[] insertCatalogRow() {
		String[] vals = saveCatalogSql();
		String query = vals[0];
		try {
			stmt = prodCon.createStatement();
			stmt.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if(rs.next()) {
				vals[1] = (new Integer(rs.getInt(1)).toString());
			}
			stmt.close();
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return vals;
	}
	
	/**
	 * Returns a String[] with values:
	 * 0 - query
	 * 1 - customer id
	 * 2 - cust fname
	 * 3 - cust lname
	 */
	public static String[] insertCustomerRow() {
		String[] vals = saveCustomerSql();
		String query = vals[0];
		try {
			stmt = acctCon.createStatement();
			stmt.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if(rs.next()) {
				vals[1] = (new Integer(rs.getInt(1)).toString());
			}
			stmt.close();
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return vals;
	}
	public static void deleteCatalogRow(Integer catId) {
		try {
			stmt = prodCon.createStatement();
			stmt.executeUpdate(deleteCatalogSql(catId));
			stmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public static void deleteProductRow(Integer prodId) {
		try {
			stmt = prodCon.createStatement();
			stmt.executeUpdate(deleteProductSql(prodId));
			stmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public static void deleteCustomerRow(Integer custId) {
		try {
			stmt = acctCon.createStatement();
			stmt.executeUpdate(deleteCustomerSql(custId));
			stmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static int readIdShoppingCart(){
		String query = readSavedShoppingCart();
		int id = 0;
		try {
			stmt = acctCon.createStatement();
			ResultSet rs = stmt.executeQuery(query); 
                if(rs.next()) {
                 id = rs.getInt("shopcartid");   
                }  
                stmt.close(); 
		}
		catch(SQLException e) {
			e.printStackTrace();
			
		}
		return id;
	}
	
	
	///queries
	public static String readAddressesSql() {
		return "SELECT * from altaddress WHERE custid = 1";
	}
	
	///queries
		public static String readCustomerProfileSql() {
			return "SELECT custid,fname,lname "+
                "FROM Customer "+
                "WHERE custid = 1";
		}

		public static String readOrderItemsSql() {
					return "SELECT * FROM OrderItem WHERE orderid =  1";
				}
	public static String[] saveCatalogSql() {
		String[] vals = new String[3];
		
		String name = "testcatalog";
		vals[0] =
		"INSERT into CatalogType "+
		"(catalogid,catalogname) " +
		"VALUES(NULL, '" + name+"')";	  
		vals[1] = null;
		vals[2] = name;
		return vals;
	}
	public static String[] saveProductSql() {
		String[] vals = new String[3];
		String name = "testprod";
		vals[0] =
		"INSERT into Product "+
		"(productid,productname,totalquantity,priceperunit,mfgdate,catalogid,description) " +
		"VALUES(NULL, '" +
				  name+"',1,1,'12/12/00',1,'test')";				  
		vals[1] = null;
		vals[2] = name;
		return vals;
	}
	public static String[] saveCustomerSql() {
		String[] vals = new String[3];
		
		String address = "testAddress";
		vals[0] =
				"INSERT INTO Customer VALUES (NULL,NULL, NULL,NULL,NULL,NULL,NULL,  NULL,   NULL,    NULL, NULL, NULL, '" + address+"', NULL, NULL , NULL, NULL ,NULL ,NULL ,NULL, NULL )";	  
		vals[1] = null;
		vals[2] = address;
		return vals;
	}
	public static String deleteProductSql(Integer prodId) {
		return "DELETE FROM Product WHERE productid = "+prodId;
	}
	public static String deleteCatalogSql(Integer catId) {
		return "DELETE FROM CatalogType WHERE catalogid = "+catId;
	}
	
	public static String deleteCustomerSql(Integer custId) {
		return "DELETE FROM Customer WHERE custid = "+custId;
	}
	
	public static void main(String[] args) {
		readAddressesSql();
		//System.out.println(System.getProperty("user.dir"));
		/*
		String[] results = DbQueries.insertCustomerRow();
		System.out.println("id = " + results[1]);
		DbQueries.deleteCustomerRow(Integer.parseInt(results[1]));
		results = DbQueries.insertCatalogRow();
		System.out.println("id = " + Integer.parseInt(results[1]));
		DbQueries.deleteCatalogRow(Integer.parseInt(results[1]));*/
	}
	public static String deleteAddressSql(int custId) {
		return "DELETE FROM Customer WHERE custid = "+custId;
	}
	
	public static String readSavedShoppingCart(){
		return "SELECT * from shopcarttbl WHERE custid= 1";
	}
	
	
}
