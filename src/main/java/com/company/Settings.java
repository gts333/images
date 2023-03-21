package com.company;

public class Settings {
    //custom color parameters
    private boolean isCustomColor;
    private String colorString;
    //custom radius parameters
    private boolean isCustomRadius;
    private int radius;
    //custom tube width parameters
    private boolean isCustomTubeWidth;
    private int tubeWidth;

    public boolean isCustomColor() {
        return isCustomColor;
    }

    public String getColorString() {
        return colorString;
    }

    public boolean isCustomRadius() {
        return isCustomRadius;
    }

    public int getRadius() {
        return radius;
    }

    public boolean isCustomTubeWidth() {
        return isCustomTubeWidth;
    }

    public int getTubeWidth() {
        return tubeWidth;
    }

    private Settings(SettingsBuilder sb) {
        this.isCustomColor = sb.isCustomColor;
        this.colorString = sb.colorString;

        this.isCustomRadius = sb.isCustomRadius;
        this.radius = sb.radius;

        this.isCustomTubeWidth = sb.isCustomTubeWidth;
        this.tubeWidth = sb.tubeWidth;
    }


    public static class SettingsBuilder {
        //custom color parameters
        private boolean isCustomColor;
        private String colorString;
        //custom radius parameters
        private boolean isCustomRadius;
        private int radius;
        //custom tube width parameters
        private boolean isCustomTubeWidth;
        private int tubeWidth;

        public SettingsBuilder() {
        }

        public SettingsBuilder setCustomBoardColor(boolean isCustomColor, String colorString) {
            this.isCustomColor = isCustomColor;
            this.colorString = colorString;
            return this;
        }

        public SettingsBuilder setCustomStationRadius(boolean isCustomRadius, int radius) {
            this.isCustomRadius = isCustomRadius;
            this.radius = radius;
            return this;
        }

        public SettingsBuilder setCustomTubeWidth(boolean isCustomTubeWidth, int tubeWidth) {
            this.isCustomTubeWidth = isCustomTubeWidth;
            this.tubeWidth = tubeWidth;
            return this;
        }

        public Settings build(){
            return new Settings(this);
        }

    }
}
