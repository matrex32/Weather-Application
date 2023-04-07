package Utils;

/**
 * Weather Application
 * @author Denis
 */


public class WeatherAPIConstants {



        /**
         * Through this key we receive permission to make requests to the Open Weather API
         */
        public static final String API_KEY = "f411d3adeed86ed3d90f19284f59435d";

        /**
         * Through this URL we make requests to the Open Weather API
         */
        public static final String request_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";

}
