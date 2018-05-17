package com.fff.hos.tools;

import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.util.Objects;

public class GcsTool {

    private GcsService gcsService;

    public GcsTool(GcsService service) {
        gcsService = service;
    }

    public void createFolder(String strBucketName, String strFolderName) throws IOException {
        Channels.newOutputStream(Objects.requireNonNull(createFile(strBucketName, strFolderName + "/"))
        ).close();
    }

    public boolean copy(InputStream input, OutputStream output, final int iBufferSize) throws IOException {
        boolean bRes = true;

        try {
            byte[] buffer = new byte[iBufferSize];
            int bytesRead = input.read(buffer);
            while (bytesRead != -1) {
                output.write(buffer, 0, bytesRead);
                bytesRead = input.read(buffer);
            }
        } catch (FileNotFoundException e) {
            bRes = false;
        } finally {
            input.close();
            output.close();
        }
        return bRes;
    }

    private GcsOutputChannel createFile(String strBucketName, String strFileName) throws IOException {
        return gcsService.createOrReplace(
                new GcsFilename(strBucketName, strFileName), GcsFileOptions.getDefaultInstance()
        );
    }
}
