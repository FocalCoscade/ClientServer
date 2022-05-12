package myproject.vkstatus;

import java.io.*;
import java.util.*;
import java.net.*;
import java.text.SimpleDateFormat;
import org.json.*;
import java.nio.charset.StandardCharsets;

// Class needs to be runned as thread from VkSetStatus
class ThreadYandexPogoda implements Runnable {
	public static volatile String pogodaString;

	public void run() {
		String responseContent = "";
		YandexPogoda yandexpogoda;
		JSONObject jsonObj;
		int temp;
		String condition;
		String daytime;
		yandexpogoda = new YandexPogoda("56.326887", "44.005986", /* There must be your YandexPogoda API key as string! */);

		while (true) {

			// If response code from YandexPogoda is not equals 200 then set empty string and sleep 30 minutes
			try {
				if (yandexpogoda.getResponseCode() != HttpURLConnection.HTTP_OK) {
					this.pogodaString = "";
					try {
						Thread.sleep(1800000); // 30 minutes
					} catch (InterruptedException ee) {
						String errorstr = ee.toString() + ": Thread.sleep(1800000)";
						LogWrite.logwrite(errorstr);
						Thread.currentThread().interrupt();
					}
					continue;
				}
			} catch (IOException e) {
				this.pogodaString = "";
				String errorstr = e.toString() + ": System.out.println(yandexpogoda.getResponseCode());";
				LogWrite.logwrite(errorstr);
				try {
						Thread.sleep(1800000); // 30 minutes
					} catch (InterruptedException ee) {
						String errorstr2 = ee.toString() + ": Thread.sleep(1800000)";
						LogWrite.logwrite(errorstr);
						Thread.currentThread().interrupt();
					}
				continue;
			}

			try {
				responseContent = yandexpogoda.getResponseContent();
			} catch (IOException e) {
				String errorstr = e.toString() + ": String responseContent = yandexpogoda.getResponseContent();";
				LogWrite.logwrite(errorstr);
			}

			jsonObj = new JSONObject(new JSONTokener(responseContent));	// Create JSON object based in response content
			jsonObj = jsonObj.getJSONObject("fact");	// Getting "fact" keyword
			temp = jsonObj.getInt("temp");			// Getting temperature from "fact"
			condition = jsonObj.getString("condition");	// Getting condition from "fact"
			daytime = jsonObj.getString("daytime");		// Getting daytime from "fact"
			this.pogodaString = "%20" + yandexpogoda.getConditionIcon((condition.equals("clear")) ? (condition + "_" + daytime) : condition) + "%20";
			this.pogodaString += ((temp > 0) ? ("%2B" + temp) : Integer.toString(temp)) + "%C2%B0C";

			// 30 minutes interval
			try {
				Thread.sleep(1800000); // 30 minutes
			} catch (InterruptedException e) {
				String errorstr = e.toString() + ": Thread.sleep(1800000)";
				LogWrite.logwrite(errorstr);
				Thread.currentThread().interrupt();
			}
		}
	}
}
