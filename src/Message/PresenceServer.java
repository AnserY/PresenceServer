package Message;

import java.beans.Statement;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PresenceServer
 */
@WebServlet("/PresenceServer")
public class PresenceServer extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Model mo ;
    public String pseudo ;
    public String ipAdresse ;
    public String result;
    public String result2;
    public ArrayList<DataAgent> connectedList ;
    public MsgTxt msg ;
    public UDPsender send;
	public ArrayList<InetAddress> connected;
	
     	
    /**
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @see HttpServlet#HttpServlet()
     */
    public PresenceServer() throws ClassNotFoundException, SQLException {
        super();
        this.mo =  new Model();
        this.send = new UDPsender();
        this.connectedList = new ArrayList<DataAgent>();
        this.connected = new ArrayList<InetAddress>();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		pseudo = request.getParameter("param1");
		ipAdresse = request.getParameter("param2");
		
		System.out.println(ipAdresse);
		
		String query = "DELETE from conversation WHERE pseudo LIKE \""+pseudo+"\" and ip_adresse LIKE \""+ipAdresse+"\"";
		try {
			PreparedStatement ps = this.mo.con.prepareStatement(query);
			ps.executeUpdate();
			informGoodBy();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
			pseudo = request.getParameter("param1");
			ipAdresse = request.getParameter("param2");
			  String query = "INSERT INTO conversation(pseudo,ip_adresse) VALUES (?,?)";
              try (PreparedStatement ps =mo.con.prepareStatement(query)){
                ps.setString(1,pseudo) ;
                ps.setString(2,ipAdresse) ;
                ps.executeUpdate() ;
              }
             catch (SQLException e) {
                System.out.println(e.getMessage()) ;
              }
              
             String query2 = "select pseudo,ip_adresse from conversation where ip_adresse NOT LIKE \""+ipAdresse+"\"";
              java.sql.Statement stmt;
			
				try {
					stmt = this.mo.con.createStatement();
					ResultSet rs = stmt.executeQuery(query2);  
		              while(rs.next()){
		                  result=rs.getString("pseudo");
		                  result2=rs.getString("ip_adresse");
		                  if(!connected.contains(InetAddress.getByName(result2))) {
		                	  connected.add(InetAddress.getByName(result2));
		                  }
		                  DataAgent agent = new DataAgent(result,result2);
		                  if(!connectedList.contains(agent)) {
		                	  connectedList.add(agent);
		                  }
		              }
		             System.out.println(connected);
		             System.out.println(connectedList);
		             msg = new MsgTxt(connectedList); 
		             send.sendMess(msg, InetAddress.getByName(ipAdresse));
		            this.inform(new DataAgent(pseudo,ipAdresse),connected);
		             
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			} 
	
	private void inform(DataAgent dt,ArrayList<InetAddress> connected ) {
		for (InetAddress co : connected) {
			send.sendMess(dt , co);
		}
	}
	
	private void informGoodBy() throws SQLException {
		String query ="SELECT ip_adresse from conversation";
		java.sql.Statement stmt;
		stmt = this.mo.con.createStatement();
		ResultSet rs = stmt.executeQuery(query);  
          while(rs.next()){
              result=rs.getString("ip_adresse");
              send.sendBye(result);
          }
	}
}


