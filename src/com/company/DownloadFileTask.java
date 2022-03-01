package com.company;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DownloadFileTask implements Runnable {

    private String url;
    private final String toPath;

    public DownloadFileTask(String url, String toPath) {
        this.url = url;
        this.toPath = toPath;
    }

    @Override
    public void run() {
        try (InputStream in = URI.create(url).toURL().openStream()) {
            Files.copy(in, Paths.get(toPath));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}
