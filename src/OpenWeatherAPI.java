import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *  Weather Application
 *  @author Denis
 *
 * This class gets the information from the OpenWeather API
 */
public class OpenWeatherAPI {

    /**
     * Through this method we use the Java class "URL" and the class "HttpURLConnection" to perform a GET request to the URL specified by the "apiUrl" parameter.
     * It then uses a "BufferedReader" object and a "StringBuilder" to read the server's response
     */
    public static JSONObject getWeatherData(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            return new JSONObject(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}