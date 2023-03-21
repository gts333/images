package com.company;
import java.io.File;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Tm3 {
    private final static Logger logger = Logger.getLogger(Tm3.class);
    public static Settings globalSettings;
    public static void main(String[] args) {
        try {
            //configuring log4j
            String log4jFolder = System.getProperty("user.dir") + File.separator + "log4j" ;
            System.setProperty("logfile.log", log4jFolder + File.separator + "logfile.log");
            PropertyConfigurator.configure(log4jFolder + File.separator + "log4j.properties");
            globalSettings = new Settings.SettingsBuilder()
                    .setCustomBoardColor(true, "255:255:255")
                    .setCustomStationRadius(true, 5)
                    .setCustomTubeWidth(true, 5)
                    .build();
            Drawer.handleBoard(800, 600);
            //main execution of drawing
            Drawer.handleTube("Central_Line", "ORANGE");
            Drawer.handleStation("Marble_Arch", 200, 250, false);
            Drawer.handleStation("Bond_Street", 230, 250, true);
            Drawer.handleStation("Oxford_Circus", 260, 250, false);
            Drawer.handleStation("Tottenham_Court_Road", 290, 250, true);
            Drawer.handleStation("Holbron", 320, 250, false);
            Drawer.handleConnection("Central_Line",  "Marble_Arch:Bond_Street:Oxford_Circus:Tottenham_Court_Road:Holbron");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
