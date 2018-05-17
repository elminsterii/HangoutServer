package com.fff.hos.gcs;

import com.fff.hos.tools.GcsTool;
import com.fff.hos.tools.StringTool;
import com.google.appengine.tools.cloudstorage.GcsService;

import java.io.IOException;

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

    public void createActivityStorage(String strId) throws IOException {
        m_gcsTool.createFolder(ACTIVITIES_IMAGES_BUCKET_NAME, strId);
    }
}
