package myproject.vkstatus;

import java.io.*;
import java.util.*;
import java.net.*;
import java.text.SimpleDateFormat;
import org.json.*;
import java.nio.charset.StandardCharsets;

public class VkTimePogodaStatus {
	private final static SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM | HH:mm", Locale.forLanguageTag(new Locale("ru", "RU").toLanguageTag()));;
	private static byte[] bytedate;
	private static Date date;
	private static String pogodaString;

	private static String getDateTime() {
		String resultdate = "";

		// Get date string
		try {
			bytedate = formatter.format(date.getTime()).getBytes("UTF-8");
			for (byte b : bytedate)
				resultdate += "%" + Integer.toHexString(b & 0xFF);
		} catch (UnsupportedEncodingException e) {
			String errorstr = e.toString() + ": Thread.sleep(1800000)";
			LogWrite.logwrite(errorstr);
		}

		return resultdate;
	}

	public static void main(String[] args) throws IOException {

		// YandexPogoda thread start
		ThreadYandexPogoda threadyandexpogoda = new ThreadYandexPogoda();
		Thread thread = new Thread(threadyandexpogoda);
		thread.start();

		// Sleep
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			String errorstr = e.toString() + ": Thread.sleep(5000)";
			LogWrite.logwrite(errorstr);
			Thread.currentThread().interrupt();
		}

		while (true) {
			String pogodaString = "";
			String resultdate = "";
			date = new Date();

			// If time is between 0 and 3 seconds then update VK status
			if ((date.getTime()/1000%60) >= 0 && (date.getTime()/1000%60) <= 3) {
				resultdate = VkTimePogodaStatus.getDateTime();
				pogodaString = threadyandexpogoda.pogodaString;		// Getting YandexPogoda info from another thread

				// Send API request to VK
				URL vkApiUrl = new URL("https://api.vk.com/method/status.set?text=" + "%E2%8C%9A%20" + resultdate + "%20%7C%20" + pogodaString + "&access_token=" + /* There must your VK API key as string*/ + &v=5.131");

				// Just open URL stream
				try {
					vkApiUrl.openStream();
				} catch (IOException e) {
					String errorstr = e.toString() + ": vkApiUrl.openStream()";
					LogWrite.logwrite(errorstr);
					// If there is an error then sleep 1 minute
					try {
						Thread.sleep(60000);
					} catch (InterruptedException ee) {
						String errorstr2 = ee.toString() + ": Thread.sleep(60000)";
						LogWrite.logwrite(errorstr2);
						Thread.currentThread().interrupt();
					}
				}

				// If everething okay then sleep 3 seconds
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					String errorstr = e.toString() + ": Thread.sleep(3000)";
					LogWrite.logwrite(errorstr);
					Thread.currentThread().interrupt();
				}
			}

			// 2 seconds interval
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				String errorstr = e.toString() + ": Thread.sleep(2000)";
				LogWrite.logwrite(errorstr);
				Thread.currentThread().interrupt();
			}
		}
	}
}

