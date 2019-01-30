package Message;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class Model {

	public Connection con ;
	
	public Model() throws ClassNotFoundException, SQLException {
		 Class.forName("org.mariadb.jdbc.Driver");
		    System.out.println("Driver loaded");
		    // Try to connect
		    this.con = DriverManager.getConnection("jdbc:mysql://localhost/serverTest", "root", "Beats+ipod09"); 
	}
	
}
