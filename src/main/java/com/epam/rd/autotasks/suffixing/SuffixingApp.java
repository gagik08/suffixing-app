package com.epam.rd.autotasks.suffixing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
<<<<<<< HEAD
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SuffixingApp {
    private static final Logger logger = Logger.getLogger(SuffixingApp.class.getName());

    public static void main(String[] args) {
        if (args.length == 0) {
            logger.log(Level.SEVERE, "Config file path is missing.");
            return;
        }

        String configFilePath = args[0];
        Properties properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream(configFilePath)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading the config file: " + configFilePath, e);
            return;
        }

        String mode = properties.getProperty("mode");
        String suffix = properties.getProperty("suffix");
        String fileList = properties.getProperty("files");

        if (mode == null) {
            logger.log(Level.SEVERE, "No mode specified in the config file.");
            return;
        }

        if (suffix == null || suffix.isEmpty()) {
=======
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.*;

public class SuffixingApp {
    private static final Logger logger = Logger.getLogger(SuffixingApp.class.getName());
    private String mode;
    private String suffix;
    private List<String> files;

    public SuffixingApp(String configFile) {
        readConfig(configFile);
    }

    private void readConfig(String configFile) {
        Properties properties = new Properties();

        try (FileInputStream fis = new FileInputStream(configFile)) {
            properties.load(fis);
            mode = properties.getProperty("mode");
            suffix = properties.getProperty("suffix");
            String filesStr = properties.getProperty("files");

            if (filesStr == null || filesStr.isEmpty()) {
                logger.log(Level.WARNING, "No files are configured to be copied/moved");
                files = Collections.emptyList();
                return;
            }

            files = Arrays.asList(filesStr.split(":"));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading config file", e);
            logger.log(Level.SEVERE, "No such file: " + configFile);
        }
    }

    public void renameFiles() {
        if (mode == null) {
            logger.log(Level.SEVERE, "No mode is configured");
            return;
        }

        switch (mode.toLowerCase()) {
            case "copy":
                copyFiles();
                break;
            case "move":
                moveFiles();
                break;
            default:
                logger.log(Level.SEVERE, "Mode is not recognized: " + mode);
                break;
        }
    }

    private void copyFiles() {
        if (suffix == null) {
>>>>>>> origin/master
            logger.log(Level.SEVERE, "No suffix is configured");
            return;
        }

<<<<<<< HEAD
        if (fileList == null || fileList.isEmpty()) {
            logger.log(Level.WARNING, "No files are configured to be copied/moved");
            return;
        }

        String[] files = fileList.split(":");

        for (String filePath : files) {
            File file = new File(filePath.replace('/', File.separatorChar));

            if (!file.exists()) {
                logger.log(Level.SEVERE, "No such file: " + filePath);
                continue;
            }

            String newFileName = addSuffixToFile(file.getName(), suffix);
            File newFile = new File(file.getParent(), newFileName);

            if (mode.equalsIgnoreCase("copy")) {
                if (file.renameTo(newFile)) {
                    logFileOperation(file.getPath(), newFile.getPath());
                } else {
                    logger.log(Level.SEVERE, "Failed to copy the file: " + file.getPath());
                }
            } else if (mode.equalsIgnoreCase("move") || mode.equalsIgnoreCase("lower")) {
                if (file.renameTo(newFile)) {
                    logFileOperation(file.getPath(), newFile.getPath());
                } else {
                    logger.log(Level.SEVERE, "Failed to move the file: " + file.getPath());
                }
            } else {
                logger.log(Level.SEVERE, "Mode is not recognized: " + mode);
                return;
            }
        }
    }

    private static void logFileOperation(String sourcePath, String destinationPath) {
        String logMessage = "INFO: " +
                sourcePath.replace('\\', '/') +
                " -> " +
                destinationPath.replace('\\', '/');
        logger.log(Level.INFO, logMessage);
    }


    private static String addSuffixToFile(String fileName, String suffix) {
        int dotIndex = fileName.lastIndexOf('.');
        String nameWithoutExtension = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
        String extension = (dotIndex == -1) ? "" : fileName.substring(dotIndex);
        return nameWithoutExtension + suffix + extension;
    }
}
=======
        if (files.isEmpty()) {
            logger.log(Level.WARNING, "No files are configured to be copied");
            return;
        }

        for (String filePath : files) {
            File file = new File(filePath);
            if (!file.exists()) {
                logger.log(Level.SEVERE, "No such file: " + filePath.replace('\\', '/'));
                continue;
            }

            String originalName = file.getName();
            String newName = addSuffix(originalName, suffix);

            File newFile = new File(file.getParent(), newName);
            file.renameTo(newFile);

            logger.log(Level.INFO, "INFO: " + file.getPath().replace('\\', '/') + " -> " + newFile.getPath().replace('\\', '/'));
        }
    }

    private void moveFiles() {
        if (suffix == null) {
            logger.log(Level.SEVERE, "No suffix is configured");
            return;
        }

        if (files.isEmpty()) {
            logger.log(Level.WARNING, "No files are configured to be moved");
            return;
        }

        for (String filePath : files) {
            File file = new File(filePath);
            if (!file.exists()) {
                logger.log(Level.SEVERE, "No such file: " + filePath.replace('\\', '/'));
                continue;
            }

            String originalName = file.getPath().replace('\\', '/');
            String newName = addSuffix(originalName, suffix);

            File newFile = new File(newName);
            file.renameTo(newFile);

            logger.log(Level.INFO, originalName + " -> " + newFile.getPath().replace('\\', '/'));
        }
    }

    private String addSuffix(String fileName, String suffix) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1) {
            String baseName = fileName.substring(0, dotIndex);
            String extension = fileName.substring(dotIndex);
            return baseName + suffix + extension;
        }
        return fileName + suffix;
    }

    public static void main(String[] args) {
        // Remove the default handlers
        Logger rootLogger = Logger.getLogger("");
        rootLogger.removeHandler(rootLogger.getHandlers()[0]);

        // Create a new console handler
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);

        // Create a custom log formatter without the date
        SimpleFormatter formatter = new SimpleFormatter() {
            @Override
            public String format(java.util.logging.LogRecord record) {
                return record.getLevel() + ": " + record.getMessage() + "\n";
            }
        };

        // Set the custom formatter for the console handler
        consoleHandler.setFormatter(formatter);

        // Add the console handler to the logger
        logger.addHandler(consoleHandler);

        // Check if the config file path is provided as a command-line argument
        if (args.length == 0) {
            System.out.println("Config file path is missing.");
            System.out.println("Usage: java -jar suffixing.jar <config-file>");
            return;
        }

        String configFile = args[0];
        SuffixingApp app = new SuffixingApp(configFile);
        app.renameFiles();
    }
}
>>>>>>> origin/master
