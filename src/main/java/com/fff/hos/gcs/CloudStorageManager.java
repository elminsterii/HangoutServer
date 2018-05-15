package com.fff.hos.gcs;

import com.fff.hos.tools.StringTool;
import com.google.appengine.tools.cloudstorage.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.List;

public class CloudStorageManager {

    /**
     * This is where backoff parameters are configured. Here it is aggressively retrying with
     * backoff, up to 10 times but taking no more that 15 seconds total to do so.
     */
    private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
            .initialRetryDelayMillis(10)
            .retryMaxAttempts(10)
            .totalRetryPeriodMillis(15000)
            .build());

    private static final String PERSONS_ICONS_BUCKET_NAME = "persons_icons";
    private static final int PERSON_ICON_BUFFER_SIZE = 2 * 1024 * 1024;

    private static final String ACTIVITIES_IMAGES_BUCKET_NAME = "activities_images";
    private static final int ACTIVITY_IMAGE_BUFFER_SIZE = 5 * 1024 * 1024;

    private StringTool m_stringTool = new StringTool();

    public boolean uploadPersonIcon(String strIconName, InputStream is) throws IOException {
        if(!m_stringTool.checkStringNotNull(strIconName))
            return false;

        GcsFileOptions instance = GcsFileOptions.getDefaultInstance();
        GcsFilename fileName = new GcsFilename(PERSONS_ICONS_BUCKET_NAME, strIconName);
        GcsOutputChannel outputChannel = gcsService.createOrReplace(fileName, instance);

        return copy(is, Channels.newOutputStream(outputChannel), PERSON_ICON_BUFFER_SIZE);
    }

    public boolean downloadPersonIcon(String strIconName, OutputStream os) throws IOException {
        if(!m_stringTool.checkStringNotNull(strIconName))
            return false;

        GcsFilename fileName = new GcsFilename(PERSONS_ICONS_BUCKET_NAME, strIconName);
        GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(fileName, 0, PERSON_ICON_BUFFER_SIZE);

        return copy(Channels.newInputStream(readChannel), os, PERSON_ICON_BUFFER_SIZE);
    }

    public List<String> listPersonIcons(String strOwnerName) throws IOException {
        if(!m_stringTool.checkStringNotNull(strOwnerName))
            return null;

        ListOptions.Builder optionBuilder = new ListOptions.Builder();
        optionBuilder.setPrefix(strOwnerName);

        ListResult result = gcsService.list(PERSONS_ICONS_BUCKET_NAME, optionBuilder.build());

        List<String> lsResult = new ArrayList<>();
        StringTool stringTool = new StringTool();

        for (; result.hasNext(); ) {
            ListItem item = result.next();
            String strIconName = item.getName();

            if(stringTool.checkStringNotNull(strIconName))
                lsResult.add(strIconName);
        }
        return lsResult;
    }

    public boolean deletePersonIcon(String strIconName) throws IOException {
        if(!m_stringTool.checkStringNotNull(strIconName))
            return false;

        return gcsService.delete(new GcsFilename(PERSONS_ICONS_BUCKET_NAME, strIconName));
    }

    public boolean deletePersonIcons(String strOwnerName) throws IOException {
        if(!m_stringTool.checkStringNotNull(strOwnerName))
            return false;

        List<String> lsIcons = listPersonIcons(strOwnerName);

        boolean bRes = true;
        if(lsIcons != null) {
            for (String strIconName : lsIcons) {
                if (!gcsService.delete(new GcsFilename(PERSONS_ICONS_BUCKET_NAME, strIconName)))
                    bRes = false;
            }
        }

        return bRes;
    }

    public void createPersonStorage(String strEmail) throws IOException  {
        createFolder(PERSONS_ICONS_BUCKET_NAME, strEmail);
    }

    public void createActivityStorage(String strID) throws IOException  {
        createFolder(ACTIVITIES_IMAGES_BUCKET_NAME, strID);
    }

    private void createFolder(String strBucketName, String strFolderName) throws IOException {
        Channels.newOutputStream(createFile(strBucketName, strFolderName + "/")
        ).close();
    }

    private GcsOutputChannel createFile(String strBucketName, String strFileName) throws IOException {
        return gcsService.createOrReplace(
                new GcsFilename(strBucketName, strFileName), GcsFileOptions.getDefaultInstance()
        );
    }

    private boolean copy(InputStream input, OutputStream output, final int iBufferSize) throws IOException {
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
}
