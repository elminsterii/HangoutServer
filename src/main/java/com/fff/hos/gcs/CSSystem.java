package com.fff.hos.gcs;

import com.fff.hos.tools.GcsTool;
import com.fff.hos.tools.StringTool;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;

class CSSystem {

    private static final String SYSTEM_RESOURCE_BUCKET_NAME = System.getProperty("cs_system_resource");
    private static final String SYSTEM_RESOURCE_FOLDER_EMAIL = System.getProperty("cs_system_resource_email");

    private static final int SYSTEM_RESOURCE_BUFFER_SIZE = 2 * 1024 * 1024;

    private GcsService gcsService;

    private StringTool m_stringTool;
    private GcsTool m_gcsTool;

    CSSystem(GcsService service) throws GcsServiceNullException {
        if(service == null)
            throw new GcsServiceNullException("Throw GcsServiceNullException by CSSystem");

        gcsService = service;
        m_stringTool = new StringTool();
        m_gcsTool = new GcsTool(gcsService);
    }

    void initialize() {
        createSystemResourceStorage();
    }

    InputStream getEmailTitleImage(String strFullPath) {
        if(!m_stringTool.checkStringNotNull(strFullPath))
            return null;

        GcsFilename fileName = new GcsFilename(SYSTEM_RESOURCE_BUCKET_NAME, strFullPath);
        GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(fileName, 0, SYSTEM_RESOURCE_BUFFER_SIZE);

        return Channels.newInputStream(readChannel);
    }

    private void createSystemResourceStorage() {
        //create folder for store email resource.
        try {
            m_gcsTool.createFolder(SYSTEM_RESOURCE_BUCKET_NAME, SYSTEM_RESOURCE_FOLDER_EMAIL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
