import java.net.*;
import java.io.*;

public class SocketServer {
	public static void main(String[] args) throws IOException {

		String strin = "";
		String strout = "";
		Socket socket = new Socket();
		ServerSocket serversocket = new ServerSocket(3333);
		PrintWriter printwriter;
		BufferedReader msgforclient = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader msgfromclient;
		boolean isfisrtconnect = true;

		// Checking for connections
		while (true) {
			serversocket.setSoTimeout(15000);
			try {
				socket = serversocket.accept();
			}
			catch (SocketTimeoutException e) {
				System.out.println("\n[Timeout: No client has connected]");
				break;
			}
			System.out.println(isfisrtconnect ? "\n[Connected]\n" : "[Connected]\n");
			
			// Getting messages
			while (true) {
				msgfromclient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				try {
					strin = msgfromclient.readLine();
				}
				catch (SocketException e) {
					System.out.println("\n[Connection lost]");
					break;
				}
				// =====================
				if (strin == null || strin.toLowerCase().equals("!exit")) {
					System.out.println("[Disconnected]");
					break;
				} else if (!strin.equals("null")) {
					System.out.println("Message: " + strin);
				}
				// =====================
				System.out.print("Send message: ");
				strout = msgforclient.readLine();
				System.out.println();
				if (strout.toLowerCase().equals("!exit")) {
					socket.close();
					serversocket.close();
					System.exit(0);
				}
				printwriter = new PrintWriter(socket.getOutputStream());
					printwriter.println(strout);
					printwriter.flush();
				if (strout.toLowerCase().equals("!disconnect")) {
					break;
				}
			}
			isfisrtconnect = false;
		}
	}
}