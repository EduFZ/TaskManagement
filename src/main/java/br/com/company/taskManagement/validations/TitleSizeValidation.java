package br.com.company.taskManagement.validations;

public class TitleSizeValidation {

    public static boolean isMinTitleSize(String title) {
        return title.length() >= 6;
    }

    public static boolean isMaxTitleSize(String title) {
        return title.length() < 21;
    }

}
