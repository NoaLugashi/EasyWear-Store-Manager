package com.example.demo.log;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/logs")
public class LogController {

    private final LogService logService;


    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/info")
    public List<String> getInfoLogs() {
        return logService.getInfoLogs();
    }

    @GetMapping("/warn")
    public List<String> getWarnLogs() {
        return logService.getWarnLogs();
    }

    @GetMapping("/debug")
    public List<String> getDebugLogs() {
        return logService.getDebugLogs();
    }

    @GetMapping("/all")
    public List<String> getAllLogs() {
        List<String> allLogs = new ArrayList<>();
        allLogs.addAll(logService.getInfoLogs());
        allLogs.addAll(logService.getWarnLogs());
        allLogs.addAll(logService.getDebugLogs());
        return allLogs;
    }

    @GetMapping("/audit")
    public List<String> getAuditLogs() {
        try {
            // Read the file content into a List of strings (each line)
            List<String> logs = Files.readAllLines(Paths.get("logs\\application.log"));
            return logs.stream()
                    .limit(100) // Limit to last 100 lines for performance
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of("Error reading log file");
        }
    }

}