package com.utech.allinonevideodownloader.Interfaces;

public interface VideoDownloader {

    String createDirectory();

    String getVideoId(String link);

    void DownloadVideo();
}
