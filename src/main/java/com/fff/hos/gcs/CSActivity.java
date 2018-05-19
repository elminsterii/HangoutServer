package com.fff.hos.gcs;

import com.fff.hos.tools.GcsTool;
import com.fff.hos.tools.StringTool;
import com.google.appengine.tools.cloudstorage.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.List;

class CSActivity {

    private static final String ACTIVITIES_IMAGES_BUCKET_NAME = "activities_images";
    private static final int ACTIVITY_IMAGE_BUFFER_SIZE = 5 * 1024 * 1024;

    private GcsService gcsService;

    private StringTool m_stringTool;
    private GcsTool m_gcsTool;

    CSActivity(GcsService service) throws GcsServiceNullException {
        if(service == null)
            throw new GcsServiceNullException("Throw GcsServiceNullException by CSActivity");

        gcsService = service;
        m_stringTool = new StringTool();
        m_gcsTool = new GcsTool(gcsService);
    }

    boolean uploadActivityImage(String strImageName, InputStream is) throws IOException {
        if(!m_stringTool.checkStringNotNull(strImageName))
            return false;

        GcsFileOptions instance = GcsFileOptions.getDefaultInstance();
        GcsFilename fileName = new GcsFilename(ACTIVITIES_IMAGES_BUCKET_NAME, strImageName);
        GcsOutputChannel outputChannel = gcsService.createOrReplace(fileName, instance);

        return m_gcsTool.copy(is, Channels.newOutputStream(outputChannel), ACTIVITY_IMAGE_BUFFER_SIZE);
    }

    boolean downloadActivityImage(String strImageName, OutputStream os) throws IOException {
        if(!m_stringTool.checkStringNotNull(strImageName))
            return false;

        GcsFilename fileName = new GcsFilename(ACTIVITIES_IMAGES_BUCKET_NAME, strImageName);
        GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(fileName, 0, ACTIVITY_IMAGE_BUFFER_SIZE);

        return m_gcsTool.copy(Channels.newInputStream(readChannel), os, ACTIVITY_IMAGE_BUFFER_SIZE);
    }

    List<String> listActivityImages(String strActivityId) throws IOException {
        if(!m_stringTool.checkStringNotNull(strActivityId))
            return null;

        ListOptions.Builder optionBuilder = new ListOptions.Builder();
        optionBuilder.setPrefix(strActivityId);

        ListResult result = gcsService.list(ACTIVITIES_IMAGES_BUCKET_NAME, optionBuilder.build());

        List<String> lsResult = new ArrayList<>();
        StringTool stringTool = new StringTool();

        for (; result.hasNext(); ) {
            ListItem item = result.next();
            String strImageName = item.getName();

            if(stringTool.checkStringNotNull(strImageName))
                lsResult.add(strImageName);
        }
        return lsResult;
    }

    boolean deleteActivityImage(String strImageName) throws IOException {
        if(!m_stringTool.checkStringNotNull(strImageName))
            return false;

        return gcsService.delete(new GcsFilename(ACTIVITIES_IMAGES_BUCKET_NAME, strImageName));
    }

    boolean deleteActivityImages(String strActivityId) throws IOException {
        if(!m_stringTool.checkStringNotNull(strActivityId))
            return false;

        List<String> lsImages = listActivityImages(strActivityId);

        boolean bRes = true;
        if(lsImages != null) {
            for (String strImageName : lsImages) {
                if (!gcsService.delete(new GcsFilename(ACTIVITIES_IMAGES_BUCKET_NAME, strImageName)))
                    bRes = false;
            }
        }

        return bRes;
    }

    void createActivityStorage(String strId) throws IOException {
        m_gcsTool.createFolder(ACTIVITIES_IMAGES_BUCKET_NAME, strId);
    }
}
