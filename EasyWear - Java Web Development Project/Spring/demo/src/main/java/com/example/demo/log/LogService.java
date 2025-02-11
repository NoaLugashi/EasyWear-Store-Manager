package com.example.demo.log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;

@Service
public class LogService {

    private final List<String> infoLogs = new CopyOnWriteArrayList<>();
    private final List<String> warnLogs = new CopyOnWriteArrayList<>();
    private final List<String> debugLogs = new CopyOnWriteArrayList<>();

    // Add log messages here
    public void addInfoLog(String log) {
        infoLogs.add(log);
    }

    public void addWarnLog(String log) {
        warnLogs.add(log);
    }

    public void addDebugLog(String log) {
        debugLogs.add(log);
    }

    public List<String> getInfoLogs() {
        return infoLogs;
    }

    public List<String> getWarnLogs() {
        return warnLogs;
    }

    public List<String> getDebugLogs() {
        return debugLogs;
    }
}