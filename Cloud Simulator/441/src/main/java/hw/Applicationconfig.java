package hw;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
public class Applicationconfig {

    public static final Config simulation_config = ConfigFactory.load("simulation_data.conf");
    public static final Config complete_appconfig = ConfigFactory.load();


}
