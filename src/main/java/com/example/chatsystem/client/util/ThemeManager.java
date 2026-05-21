package com.example.chatsystem.client.util;

import javafx.scene.paint.Color;

public class ThemeManager {
    private static boolean isDarkMode = true;

    public static boolean isDarkMode() {
        return isDarkMode;
    }

    public static void setDarkMode(boolean darkMode) {
        isDarkMode = darkMode;
    }

    public static String getRootBackground() {
        return isDarkMode ? "#0d0d0d" : "#f3f4f6";
    }

    public static String getSidebarBackground() {
        return isDarkMode ? "#111827" : "#ffffff";
    }

    public static String getSidebarBorder() {
        return isDarkMode ? "#1f2937" : "#e5e7eb";
    }

    public static String getCardBackground() {
        return isDarkMode ? "#1f2937" : "#ffffff";
    }
    
    public static String getSecondaryCardBackground() {
        return isDarkMode ? "#374151" : "#f9fafb";
    }

    public static String getPrimaryText() {
        return isDarkMode ? "#e5e7eb" : "#111827";
    }

    public static String getSecondaryText() {
        return isDarkMode ? "#9ca3af" : "#6b7280";
    }
    
    public static String getSubLabelText() {
        return isDarkMode ? "#6b7280" : "#9ca3af";
    }

    public static String getAccentColor() {
        return "#00b4d8"; // Blue accent remains same
    }

    public static String getAccentHover() {
        return "#0096c7";
    }
    
    public static String getTextFieldBackground() {
        return isDarkMode ? "#1f2937" : "#f3f4f6";
    }
    
    public static String getDialogBackground() {
        return isDarkMode ? "#1f2937" : "#ffffff";
    }
}
