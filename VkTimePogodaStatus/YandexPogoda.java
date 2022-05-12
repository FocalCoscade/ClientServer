package myproject.vkstatus;

import java.io.*;
import java.util.*;
import java.net.*;
import java.text.SimpleDateFormat;
import org.json.*;
import java.nio.charset.StandardCharsets;

class YandexPogoda {
	private final static HashMap<String, String> conditionIcons = new HashMap<String, String>();
	private HttpURLConnection httpConnection;

	// Create "conditionIcons" hashmap, where condition status as string equals condition status as emoji
	private static void conditionIconsInit() {
		conditionIcons.put("clear_d", "%E2%98%80");
		conditionIcons.put("clear_n", "%F0%9F%8C%99");
		conditionIcons.put("partly-cloudy", "%E2%9B%85");
		conditionIcons.put("cloudy", "%E2%9B%85");
		conditionIcons.put("overcast", "%E2%98%81");
		conditionIcons.put("drizzle", "%F0%9F%8C%A6");
		conditionIcons.put("light-rain", "%F0%9F%8C%A7");
		conditionIcons.put("rain", "%F0%9F%8C%A7");
		conditionIcons.put("moderate-rain", "%F0%9F%8C%A7");
		conditionIcons.put("heavy-rain", "%F0%9F%8C%A7");
		conditionIcons.put("continuous-heavy-rain", "%F0%9F%8C%A7");
		conditionIcons.put("showers", "%F0%9F%8C%A7");
		conditionIcons.put("wet-snow", "%F0%9F%8C%A8");
		conditionIcons.put("light-snow", "%F0%9F%8C%A8");
		conditionIcons.put("snow", "%F0%9F%8C%A8");
		conditionIcons.put("snow-showers", "%F0%9F%8C%A8");
		conditionIcons.put("hail", "%E2%98%84");
		conditionIcons.put("thunderstorm", "%F0%9F%8C%A9");
		conditionIcons.put("thunderstorm-with-rain", "%E2%9B%88");
		conditionIcons.put("thunderstorm-with-hail", "%E2%9B%88");
	}

	/*
	 * lat - latitude on map
	 * lon - longitude on map
	 * apikey - YandexPogoda API key
	*/
	public YandexPogoda(String lat, String lon, String apikey) {
		this.conditionIconsInit();
		try {
			this.httpConnection = (HttpURLConnection)(new URL("https://api.weather.yandex.ru/v2/informers?lat=" + lat + "&lon=" + lon + "&lang=ru_RU").openConnection());
		} catch (MalformedURLException e) {
			String errorstr = e.toString() + ": this.httpConnection";
			LogWrite.logwrite(errorstr);
		} catch (IOException e) {
			String errorstr = e.toString() + ": this.httpConnection";
			LogWrite.logwrite(errorstr);
		}
		try {
			this.httpConnection.setRequestMethod("GET");
		} catch (ProtocolException e) {
			String errorstr = e.toString() + ": this.httpConnection.setRequestMethod(\"GET\")";
			LogWrite.logwrite(errorstr);
		}
		this.httpConnection.setRequestProperty("X-Yandex-API-Key", apikey);
	}

	// Getting response code after opening connection
	public int getResponseCode() throws IOException {
		return this.httpConnection.getResponseCode();
	}

	// Method returns response content from YandexPogoda API
	public String getResponseContent() throws IOException {
		String line = "";
		StringBuilder responseContent = new StringBuilder();
		// try {
			BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
			while ((line = bufferedreader.readLine()) != null) {
				responseContent.append(line);
			}
			bufferedreader.close();
		// } catch (IOException e) {
		// 	String errorstr = e.toString() + ": BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()))";
		// 	LogWrite.logwrite(errorstr);
		// }
		return responseContent.toString();
	}

	public String getConditionIcon(String condition) {
		return conditionIcons.get(condition);
	}
}