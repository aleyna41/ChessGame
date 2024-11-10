package com.example.backend;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

// Wird momentan nicht benutzt -> löschen, wenn Profilbild fertig ist
public class EmptyMultipartFile implements MultipartFile {

    @Override
    public String getName() {
        return "emptyFile";
    }
    @Override
    public String getOriginalFilename() {
        return "empty.txt";
    }
    @Override
    public String getContentType() {
        return "text/plain";
    }
    @Override
    public boolean isEmpty() {
        return true;
    }
    @Override
    public long getSize() {
        return 0;
    }
    @Override
    public byte[] getBytes() throws IOException {
        return new byte[0];
    }
    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(new byte[0]);
    }
    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {

    }
    @Override
    public void transferTo(java.nio.file.Path dest) throws IOException, IllegalStateException {
        // Implementieren Sie dies je nach Bedarf oder lassen Sie es leer
    }
}