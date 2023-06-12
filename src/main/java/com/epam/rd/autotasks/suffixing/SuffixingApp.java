package com.epam.rd.autotasks.suffixing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
            logger.log(Level.SEVERE, "No suffix is configured");
            return;
        }

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


    private static String addSuffixToFile(String fileName, String suffix) {
        int dotIndex = fileName.lastIndexOf(File.separator);
        String nameWithoutPath = (dotIndex == -1) ? fileName : fileName.substring(dotIndex + 1);
        int dotIndex2 = nameWithoutPath.lastIndexOf('.');
        String nameWithoutExtension = (dotIndex2 == -1) ? nameWithoutPath : nameWithoutPath.substring(0, dotIndex2);
        String extension = (dotIndex2 == -1) ? "" : nameWithoutPath.substring(dotIndex2);
        return nameWithoutExtension + suffix + extension;
    }

    private static void logFileOperation(String sourcePath, String destinationPath) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("INFO: ");
        logMessage.append(sourcePath.replace('\\', '/'));
        logMessage.append(" -> ");
        logMessage.append(destinationPath.replace('\\', '/'));
        logger.log(Level.INFO, logMessage.toString());
    }

}
