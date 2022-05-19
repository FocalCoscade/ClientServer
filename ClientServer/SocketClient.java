import java.net.*;
import java.io.*;

public class SocketClient {
	public static void main(String[] args) throws IOException, InterruptedException {

		String strin = "";
		String strout = "";
		Socket socket = new Socket();
		PrintWriter printwriter;
		BufferedReader msgforserver = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
		BufferedReader msgfromserver;

		// Trying to connect the server
		while (!socket.isConnected()) {
			//socket.setSoTimeout(15000);
			try {
				socket = new Socket(/**/, 3333); // Server ip adress as string in first argument
			}
			catch (ConnectException e) {
				System.out.println("\n[No connection]");
				Thread.sleep(3000);
				continue;
			}
			System.out.println("\n[Connected]\n");
		}

		// Sending messages to server
		while(true) {
			System.out.print("Send message: ");
			strout = msgforserver.readLine();
			System.out.println();
			printwriter = new PrintWriter(socket.getOutputStream());

				printwriter.println(strout);
				printwriter.flush();
			if (strout.toLowerCase().equals("!exit")) {
				break;
			}
			msgfromserver = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			try {
				strin = msgfromserver.readLine();
			}
			catch (SocketException e) {
				System.out.println("\n[Connection lost]");
				break;
			}
			if (strin == null || strin.toLowerCase().equals("!disconnect")) {
				System.out.println("[Disconnected]");
				break;
			} else if (!strin.equals("null")) {
				System.out.println("Message: " + strin);
			}
		}

		socket.close();

	}
}