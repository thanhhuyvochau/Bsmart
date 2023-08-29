package fpt.project.bsmart.entity.dto;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileDto implements Serializable {
    private String url;
    private String name;
    private String extension;
    private long size;

    public FileDto(String url) {
        this.url = url;
        this.name = new File(url).getName();
        this.extension = getFileExtension();
        this.size = getFileSize();
    }

    private String getFileExtension() {
        int dotIndex = name.lastIndexOf(".");
        if (dotIndex != -1 && dotIndex < name.length() - 1) {
            return name.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }

    private long getFileSize() {
        try {
            URL url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                long size = connection.getContentLengthLong();
                connection.disconnect();
                return size;
            } else {
                // Handle the case when the URL does not return the file size correctly
                throw new IOException("Failed to retrieve file size. Response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
