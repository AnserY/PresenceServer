package Message;


	

	import java.io.ByteArrayOutputStream;
	import java.io.File;
	import java.io.IOException;
	import java.io.ObjectOutputStream;
	import java.net.DatagramPacket;
	import java.net.DatagramSocket;
	import java.net.InetAddress;
	import java.net.SocketException;
	import java.net.UnknownHostException;


	
	



	public class UDPsender {
		
		public DatagramSocket socket; // This class represents a socket for sending and receiving datagram packets
	
		
	        /*
	         * __Contruct  
	         */
		public UDPsender(){
			try {
				this.socket = new DatagramSocket();
			} catch (SocketException e) {
				System.err.println("Socket couldn't be created.");
				e.printStackTrace();
			}
		}
		
		public UDPsender(DatagramSocket sock){
			this.socket=sock;
		}
		
		/*
		 * Method that send a given message in UDP mode
		 */
		public void sendMess(Message mes, InetAddress iptosend) {
			
			int port = 1234;
			byte[] buf = new byte[2048];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(mes);
				oos.close();
				buf=baos.toByteArray();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			DatagramPacket mestosend = new DatagramPacket(buf, buf.length, iptosend, port);
			try {
				this.socket.send(mestosend);
			} catch (IOException e) {
				System.err.println("Message failed to send");
				e.printStackTrace();
			}
		}
		
		public void sendBye(String adr) {
	        InetAddress addr = null;
	        try {
	            addr = InetAddress.getByName(adr);
	        } catch (UnknownHostException e) {
	            e.printStackTrace();
	        }
	        MsgGoodbye mes = new MsgGoodbye();
	        this.sendMess(mes, addr);
	    }
		

	}


