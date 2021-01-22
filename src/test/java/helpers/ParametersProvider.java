package helpers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Class provides configuration parameters from files set by system
 * properties.
 */
public final class ParametersProvider {
    /**
     * List of parameters holders.
     */
    private ArrayList<Properties> propertiesList = new ArrayList<>();

    /**
     * Gets configuration file names from system properties.
     * @return list of configuration file names
     */
    private static ArrayList<String> getConfigFileNames() {
        ArrayList<String> configFileNames = new ArrayList<>();
        for (String key: System.getProperties().stringPropertyNames()) {
            if (key.startsWith("config.location")) {
                String[] fileNames = System.getProperties().getProperty(key)
                        .split(";");
                for (String fileName: fileNames) {
                    configFileNames.add(fileName);
                }
            }
        }
        return configFileNames;
    }

    /**
     * ParametersProvider constructor.
     * @throws IOException when config file is not available
     */
    private ParametersProvider() throws IOException {
        ArrayList<String> configFileNames = getConfigFileNames();
        for (String fileName: configFileNames) {
            Properties properties = new Properties();
            properties.loadFromXML(new FileInputStream(fileName));
            propertiesList.add(properties);
        }
    }

    /**
     * ParametersProvider instance holder.
     */
    private static ParametersProvider instance;

    /**
     * ParametersProvider instance accessor.
     * @return ParametersProvider instance
     * @throws IOException when config file is not available
     */
    private static ParametersProvider getInstance() throws IOException {
        if (instance == null) {
            instance = new ParametersProvider();
        }
        return instance;
    }

    /**
     * Gets parameter by its key from list of parameters holder.
     * @param key of parameter to find in configuration
     * @return parameter or empty string, if it is not found
     * @throws IOException when config file is not available
     */
    public static String getProperty(final String key) throws IOException {
        for (Properties properties: getInstance().propertiesList) {
            String result = properties.getProperty(key, null);
            if (result != null) {
                return result;
            }
        }
        return "";
    }
}
