import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import org.json.JSONObject;
import Utils.*;

/**
 * Weather Application
 * @author Denis 
 */
public class WeatherFrame extends JFrame {

    /**
     * We declare the buttons, the labels and the text area
     */
    public final JButton refreshButton;
    public final JButton exitButton;
    public final JTextField cityTextField;
    public final JLabel temperatureLabel;
    public  final JLabel humidityLabel;
    public final JLabel descriptionLabel;
    public final JLabel windSpeedLabel;
    public final JLabel visibilityLabel;
    public final JLabel feelsLikeLabel;
    public final JLabel titleLabel;
    public final JLabel iconLabel;

    public WeatherFrame() {
        this.setTitle(Messages.TITLE_STRING);

        titleLabel = new JLabel(Messages.WEATHER_STRING);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
        titleLabel.setForeground(new Color(0x000000));


        refreshButton = new JButton(Messages.REFRESH_STRING);
        refreshButton.setBackground(new Color(0x00FF06));
        refreshButton.setForeground(new Color(0xFF0005FD, true));

        exitButton = new JButton(Messages.EXIT_STRING);
        exitButton.setBackground(new Color(0xFF0000));
        exitButton.setForeground(new Color(0xFFFFFF));

        refreshButton.addActionListener(createRefreshListener());
        exitButton.addActionListener(createExitListener());

        cityTextField = new JTextField(10);
        temperatureLabel = new JLabel("");
        feelsLikeLabel = new JLabel("");
        humidityLabel = new JLabel("");
        visibilityLabel = new JLabel("");
        windSpeedLabel = new JLabel("");
        descriptionLabel = new JLabel("");

        iconLabel = new JLabel();

        JPanel titlePanel = new JPanel();
        titlePanel.add(titleLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.add(exitButton);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel(Messages.CITY_STRING));
        inputPanel.add(cityTextField);

        JPanel weatherPanel = createWeatherPanel(7, 1, temperatureLabel, feelsLikeLabel, humidityLabel, windSpeedLabel, visibilityLabel, descriptionLabel, iconLabel);


        JPanel contentPanel = createGridBagPanel(titlePanel, inputPanel, weatherPanel, buttonPanel);


        this.setContentPane(contentPanel);
        this.setSize(800, 600);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }


    /**
     * Through this method we scale icons for all labels that contain weather data
     */
    public static ImageIcon createScaledIcon(String path, int width, int height) throws Exception {
        ImageIcon icon;
        if (path.startsWith("http")) {
            icon = new ImageIcon(new URL(path));
        } else {
            icon = new ImageIcon(path);
        }
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);

        }

    /**
     * Through this method we display icons for all labels that contain weather data
     */
    private void setWeatherIcons(JSONObject weatherData) {
        try {
            String iconCode = weatherData.getJSONArray("weather").getJSONObject(0).getString("icon");

            String iconUrl = "http://openweathermap.org/img/wn/" + iconCode + ".png";

            ImageIcon iconDescriptionLabel = createScaledIcon(iconUrl, 50, 50);
            ImageIcon iconHumidityLevel = createScaledIcon("http://openweathermap.org/img/w/09d.png", 50, 50);
            ImageIcon iconWindSpeedLabel = createScaledIcon("icons/50n.png", 50, 50);
            ImageIcon iconTemperatureLabel = createScaledIcon("icons/temperature.png", 45, 45);
            ImageIcon iconVisibilityLabel = createScaledIcon("icons/eye2.png", 50, 50);
            ImageIcon iconFeelsLikeLabel = createScaledIcon("icons/temperature.png", 45, 45);

            descriptionLabel.setIcon(iconDescriptionLabel);
            temperatureLabel.setIcon(iconTemperatureLabel);
            visibilityLabel.setIcon(iconVisibilityLabel);
            feelsLikeLabel.setIcon(iconFeelsLikeLabel);
            humidityLabel.setIcon(iconHumidityLevel);
            windSpeedLabel.setIcon(iconWindSpeedLabel);
        } catch (Exception ae) {
            ae.printStackTrace();
        }
    }

    /**
     * Through this method we create the weather data related panels
     */
    public JPanel createWeatherPanel(int rows, int cols, JLabel... labels) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(rows, cols));
        for (JLabel label : labels) {
            panel.add(label);
        }
        return panel;
    }

    /**
     * Through this method we add four more panels to it in order, using the GridBagLayout constraints
     */
    public JPanel createGridBagPanel(JPanel titlePanel, JPanel inputPanel, JPanel weatherPanel, JPanel buttonPanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.insets = new Insets(20, 20, 5, 20);
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel contentPanel = new JPanel(new GridBagLayout());

        gbc.gridy = 1;
        contentPanel.add(titlePanel, gbc);

        gbc.gridy = 2;
        contentPanel.add(inputPanel, gbc);

        gbc.gridy = 3;
        contentPanel.add(weatherPanel, gbc);

        gbc.gridy = 4;
        contentPanel.add(buttonPanel, gbc);

        contentPanel.setBackground(new Color(0x00D0FF));

        return contentPanel;
    }



    /**
     * Through this method we display the messages that appear to us in case the city does not exist
     */
    private void displayFailedSearch(String city) {
        temperatureLabel.setText(Messages.FAILED_STRING + city);
        feelsLikeLabel.setText("");
        humidityLabel.setText("");
        windSpeedLabel.setText("");
        visibilityLabel.setText("");
        descriptionLabel.setText("");
    }

    /**
     * Through this method we display the weather data provided by the API
     */
    public void displayWeatherData(String city) {
        if (!city.isEmpty()) {
            String apiUrl = String.format(WeatherAPIConstants.request_URL, city, WeatherAPIConstants.API_KEY);
            JSONObject weatherData = OpenWeatherAPI.getWeatherData(apiUrl);
            if (weatherData != null) {
                temperatureLabel.setText( Messages.TEMPERATURE_STRING + weatherData.getJSONObject("main").getInt("temp") + Messages.CELSIUS_STRING);
                feelsLikeLabel.setText( Messages.FEELS_STRING + weatherData.getJSONObject("main").getInt("feels_like") + Messages.CELSIUS_STRING);
                humidityLabel.setText( Messages.HUMIDITY_STRING + weatherData.getJSONObject("main").getInt("humidity") + Messages.PERCENTAGE_STRING);
                windSpeedLabel.setText( Messages.WIND_STRING + weatherData.getJSONObject("wind").getInt("speed") + Messages.METERS_PER_SECOND_STRING);
                visibilityLabel.setText( Messages.VISIBILITY_STRING + weatherData.getDouble("visibility")/1000 + Messages.KM_STRING);
                descriptionLabel.setText( Messages.DESCRIPTION_STRING + weatherData.getJSONArray("weather").getJSONObject(0).getString("description"));
            }
        }
    }

    /**
     * Through this method we create the functionality of the EXIT button
     */
    private ActionListener createExitListener(){
        return e -> System.exit(0);
    }

    /**
     * Through this method we create the functionality of the REFRESH button
     */
    private ActionListener createRefreshListener(){
        return (ActionEvent e) -> {

                String city = cityTextField.getText();
                String apiUrl = String.format(WeatherAPIConstants.request_URL, city, WeatherAPIConstants.API_KEY);
                JSONObject weatherData = OpenWeatherAPI.getWeatherData(apiUrl);

                if(weatherData!=null){

                        displayWeatherData(cityTextField.getText());

                        setWeatherIcons(weatherData);

                    } else {
                        displayFailedSearch(city);
                    }


            };
        }
     }



