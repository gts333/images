package com.company;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Drawer {
    public static Settings globalSettings = Tm3.globalSettings;
    public static int customizedBoardWidth = 0;
    public static int customizedBoardHeight = 0;

    // set default boarder size
    public static BufferedImage image;

    // create image
    public static Graphics2D graphics;

    // default station dot radius
    public static final int defaultStationRadius = 15;

    // default board color
    public static final Color defaultBoardColor = Color.white;

    // default tube line width
    public static final int defaultTubeWidth = 5;

    public static HashMap<String, Station> stationMap = new HashMap<String, Station>();

    public static HashMap<String, Color> tubeMap = new HashMap<String, Color>();

    public static HashMap<String, String> stationAndTube = new HashMap<String, String>();

    public static String fileLocation = "resource/wheelchairBlack.png";

    // generate board
    public static void handleBoard(int width, int height) throws IOException {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        graphics = image.createGraphics();
        customizedBoardWidth = width;
        customizedBoardHeight = height;
        if (globalSettings.isCustomColor()) {
            graphics.setColor(transferColor(globalSettings.getColorString()));
            graphics.fillRect(0, 0, width, height);
        } else {
            graphics.setColor(defaultBoardColor);
            graphics.fillRect(0, 0, width, height);
        }
        Rectangle rect = new Rectangle(0, 0, width, height);
        FileInputStream fis = null;
        ImageInputStream iis = null;
        File file = new File("TubeMap.jpg");
        if(file.exists()) {
            file.delete();
        }
        File f = new File("TubeMap.jpg");
        System.out.println("================");
        try {
            ImageIO.write(image, "jpg", f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fis = new FileInputStream("TubeMap.jpg");

        iis = ImageIO.createImageInputStream(fis);

        ImageReader reader = ImageIO.getImageReadersBySuffix("jpg").next();
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        param.setSourceRegion(rect);
        BufferedImage image = reader.read(0, param);
        ImageIO.write(image, "jpg", new File("TubeMap.jpg"));
    }

    // add station
    public static void handleStation(String stationName, int x, int y, boolean isStationAccessible) throws IOException {
        // remove underscore
        String name = stationName.replace("_", " ");
        stationMap.put(name, new Station(name, x, y, isStationAccessible, false));
    }

    // draw station
    public static void drawStation() throws IOException {
        for (String stationName : stationMap.keySet()) {
            graphics.setColor(Color.black);
            Station station = stationMap.get(stationName);
            if (globalSettings.isCustomRadius()) {
                if (station.isAccessible) {
                    BufferedImage sourceImage = ImageIO.read(new File(fileLocation));
                    // accessible image
                    graphics.drawImage(sourceImage, station.x - globalSettings.getRadius(), customizedBoardHeight - station.y - globalSettings.getRadius(), globalSettings.getRadius() * 2, defaultStationRadius * 2, null);
                    // station label
                    graphics.drawString(station.stationName, station.x - globalSettings.getRadius(), customizedBoardHeight - station.y + globalSettings.getRadius() + 5);
                } else if (station.isCrossStation) {
                    graphics.setColor(Color.white);
                    graphics.fillOval(station.x - defaultStationRadius, customizedBoardHeight - station.y - defaultStationRadius, defaultStationRadius * 2, defaultStationRadius * 2);
                    graphics.setColor(Color.black);
                    graphics.drawOval(station.x - defaultStationRadius, customizedBoardHeight - station.y - defaultStationRadius, defaultStationRadius * 2, defaultStationRadius * 2);
                    graphics.drawString(station.stationName, station.x - globalSettings.getRadius(), customizedBoardHeight - station.y + globalSettings.getRadius() + 5);
                } else {
                    graphics.fillOval(station.x - defaultStationRadius, customizedBoardHeight - station.y - defaultStationRadius, defaultStationRadius * 2, defaultStationRadius * 2);
                    graphics.drawString(station.stationName, station.x - globalSettings.getRadius(), customizedBoardHeight - station.y + globalSettings.getRadius() + 5);
                }
            } else {
                if (stationMap.get(stationName).isAccessible) {
                    BufferedImage sourceImage = ImageIO.read(new File(fileLocation));
                    // accessible image
                    graphics.drawImage(sourceImage, station.x - defaultStationRadius, customizedBoardHeight - station.y - defaultStationRadius, defaultStationRadius * 2, defaultStationRadius * 2, null);
                    // station label
                    graphics.drawString(station.stationName, station.x - defaultStationRadius, customizedBoardHeight - station.y + defaultStationRadius + 5);
                } else if (station.isCrossStation) {
                    graphics.setColor(Color.white);
                    graphics.fillOval(station.x - defaultStationRadius, customizedBoardHeight - station.y - defaultStationRadius, defaultStationRadius * 2, defaultStationRadius * 2);
                    graphics.setColor(Color.black);
                    graphics.drawOval(station.x - defaultStationRadius, customizedBoardHeight - station.y - defaultStationRadius, defaultStationRadius * 2, defaultStationRadius * 2);
                    graphics.drawString(station.stationName, station.x - defaultStationRadius, customizedBoardHeight - station.y + defaultStationRadius + 5);
                } else {
                    graphics.fillOval(station.x - defaultStationRadius, customizedBoardHeight - station.y - defaultStationRadius, defaultStationRadius * 2, defaultStationRadius * 2);
                    graphics.drawString(station.stationName, station.x - defaultStationRadius, customizedBoardHeight - station.y + defaultStationRadius + 5);
                }
            }
        }
        File f = new File("TubeMap.jpg");
        try {
            ImageIO.write(image, "jpg", f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // tube settings
    public static void handleTube(String tubeName, String colorString) {
        String name = tubeName.replace("_", " ");
        tubeMap.put(name, transferColor(colorString));
    }

    public static void handleConnection(String tubeName, String stations) throws IOException {
        Color tubeColor = tubeMap.get(tubeName.replace("_", " "));
        List<String> finalStations = Arrays.asList(stations.replace("_", " ").split(":"));

        // cross station
        for (String stationName : finalStations) {
            if (stationAndTube.containsKey(stationName)) {
                if (!stationAndTube.get(stationName).equals(tubeName)) {
                    Station station = stationMap.get(stationName);
                    station.isCrossStation = true;
                    stationMap.put(stationName, station);
                }
            }
            stationAndTube.put(stationName, tubeName);
        }

        for (int i = 0; i < finalStations.size() - 1; i++) {
            handleSingleConnection(tubeColor, finalStations.get(i), finalStations.get(i + 1));
        }

        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        File f = new File("TubeMap.jpg");
        try {
            ImageIO.write(image, "jpg", f);
        } catch (IOException e) {
            e.printStackTrace();
        }

        drawStation();
        generateLegend();
        joinImagesHorizontal();
    }

    // draw single connection between to stations
    public static void handleSingleConnection(Color tubeColor, String station1Name, String station2Name) {
        if (globalSettings.isCustomTubeWidth()) {
            graphics.setStroke(new BasicStroke(globalSettings.getTubeWidth(), BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
        } else {
            graphics.setStroke(new BasicStroke(defaultTubeWidth, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
        }
        graphics.setColor(tubeColor);
        graphics.drawLine(stationMap.get(station1Name).x, customizedBoardHeight - stationMap.get(station1Name).y, stationMap.get(station2Name).x, customizedBoardHeight - stationMap.get(station2Name).y);
    }

    // generate legend for tube line
    public static void generateLegend() {

        BufferedImage legendImage = new BufferedImage(300, customizedBoardHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D legendG = legendImage.createGraphics();
        if (globalSettings.isCustomColor()) {
            legendG.setColor(transferColor(globalSettings.getColorString()));
        } else {
            legendG.setColor(defaultBoardColor);
        }

        legendG.fillRect(0, 0, 300, customizedBoardHeight);
        // title
        legendG.setColor(Color.black);
        legendG.setFont(new Font("times", Font.BOLD, 20));
        legendG.drawString("Key to lines:", 20, 20);
        int i = 0;
        // text
        for (String tubeName : tubeMap.keySet()) {
            // rectangle
            legendG.setStroke(new BasicStroke(20, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
            legendG.setColor(tubeMap.get(tubeName));
            legendG.drawLine(20, i + 70, 20 + 50, i + 70);
            // text
            legendG.setColor(Color.black);
            legendG.drawString(tubeName, 120, i + 70 + 5);
            i = i + 50;
        }
        File f = new File("Legend.jpg");
        try {
            ImageIO.write(legendImage, "jpg", f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // combine two pic together
    public static void joinImagesHorizontal() {
        try {
            File fileOne = new File("TubeMap.jpg");
            BufferedImage imageOne = ImageIO.read(fileOne);
            int width = imageOne.getWidth();
            int height = imageOne.getHeight();

            int[] imageArrayOne = new int[width * height];
            imageArrayOne = imageOne.getRGB(0, 0, width, height, imageArrayOne, 0, width);

            File fileTwo = new File("Legend.jpg");
            BufferedImage imageTwo = ImageIO.read(fileTwo);
            int width2 = imageTwo.getWidth();
            int height2 = imageTwo.getHeight();
            int[] ImageArrayTwo = new int[width2 * height2];
            ImageArrayTwo = imageTwo.getRGB(0, 0, width2, height2, ImageArrayTwo, 0, width2);

            // generate new file
            BufferedImage imageNew = new BufferedImage(width + width2, height, BufferedImage.TYPE_INT_RGB);
            imageNew.setRGB(0, 0, width, height, imageArrayOne, 0, width);
            imageNew.setRGB(width, 0, width2, height2, ImageArrayTwo, 0, width2);

            File outFile = new File("TubeMapWithLegend.jpg");
            ImageIO.write(imageNew, "jpg", outFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // rgb transfer into color
    public static Color transferColor(String originalColor) {
        Color newColor = new Color(255, 255, 255);
        switch (originalColor) {
            case "RED":
                newColor = new Color(255, 0, 0);
                break;
            case "GREEN":
                newColor = new Color(0, 255, 0);
                break;
            case "BLUE":
                newColor = new Color(0, 0, 255);
                break;
            case "YELLOW":
                newColor = new Color(255, 255, 0);
                break;
            case "MAGENTA":
                newColor = new Color(255, 0, 255);
                break;
            case "CYAN":
                newColor = new Color(0, 100, 100);
                break;
            case "WHITE":
                newColor = new Color(255, 255, 255);
                break;
            case "BLACK":
                newColor = new Color(0, 0, 0);
                break;
            case "GRAY":
                newColor = new Color(128, 128, 128);
                break;
            case "LIGHT_GRAY":
                newColor = new Color(211, 211, 211);
                break;
            case "DARK_GRAY":
                newColor = new Color(169, 169, 169);
                break;
            case "PINK":
                newColor = new Color(255, 192, 203);
                break;
            case "ORANGE":
                newColor = new Color(255, 165, 0);
                break;
            default:
                List<String> rgbColor = new ArrayList<String>();
                rgbColor = Arrays.asList(originalColor.split(":"));
                newColor = new Color(Integer.valueOf(rgbColor.get(0)), Integer.valueOf(rgbColor.get(1)), Integer.valueOf(rgbColor.get(2)));
                break;

        }
        return newColor;
    }

    // station definition
    public static class Station {

        private String stationName;

        private int x;

        private int y;

        private Boolean isAccessible;

        private Boolean isCrossStation;

        public Station(String stationName, int x, int y, Boolean isAccessible, Boolean isCrossStation) {
            this.stationName = stationName;
            this.x = x;
            this.y = y;
            this.isAccessible = isAccessible;
            this.isCrossStation = isCrossStation;
        }
    }
}
