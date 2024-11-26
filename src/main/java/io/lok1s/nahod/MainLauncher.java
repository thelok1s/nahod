package io.lok1s.nahod;

/**
 * Класс MainLauncher используется для запуска основного приложения Nahod.
 * Этот класс необходим для обхода ограничений некоторых окружений, таких как пакеты JavaFX,
 * где точка входа должна находиться в отдельном классе.
 */
public class MainLauncher {
    /**
     * Точка входа для запуска приложения Nahod.
     *
     * @param args Аргументы командной строки.
     */
    public static void main(String[] args) {
        Nahod.main(args);
    }
}