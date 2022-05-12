package myproject.vkstatus;

import java.io.*;
import java.util.*;
import java.net.*;
import java.text.SimpleDateFormat;
import org.json.*;
import java.nio.charset.StandardCharsets;

// This class needs only to write exceptions to file
class LogWrite {
	static BufferedWriter bufferedwriter;
	static File errorfile = new File("Errorlog.txt");
	static Writer writer;
	static void logwrite(String errorstr) {
		try {
			System.out.println(errorstr);
			writer = new OutputStreamWriter(new FileOutputStream(errorfile), StandardCharsets.UTF_8);
			bufferedwriter = new BufferedWriter(writer);
			bufferedwriter.write(errorstr);
			bufferedwriter.close();
			//System.exit(1);
		} catch (IOException e) {}
	}
}