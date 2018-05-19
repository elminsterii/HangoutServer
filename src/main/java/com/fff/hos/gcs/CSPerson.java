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

class CSPerson {

    private static final String PERSONS_ICONS_BUCKET_NAME = "persons_icons";
    private static final int PERSON_ICON_BUFFER_SIZE = 2 * 1024 * 1024;

    private GcsService gcsService;

    private StringTool m_stringTool;
    private GcsTool m_gcsTool;

    CSPerson(GcsService service) throws GcsServiceNullException {
        if(service == null)
            throw new GcsServiceNullException("Throw GcsServiceNullException by CSPerson");

        gcsService = service;
        m_stringTool = new StringTool();
        m_gcsTool = new GcsTool(gcsService);
    }

    boolean uploadPersonIcon(String strIconName, InputStream is) throws IOException {
        if(!m_stringTool.checkStringNotNull(strIconName))
            return false;

        GcsFileOptions instance = GcsFileOptions.getDefaultInstance();
        GcsFilename fileName = new GcsFilename(PERSONS_ICONS_BUCKET_NAME, strIconName);
        GcsOutputChannel outputChannel = gcsService.createOrReplace(fileName, instance);

        return m_gcsTool.copy(is, Channels.newOutputStream(outputChannel), PERSON_ICON_BUFFER_SIZE);
    }

    boolean downloadPersonIcon(String strIconName, OutputStream os) throws IOException {
        if(!m_stringTool.checkStringNotNull(strIconName))
            return false;

        GcsFilename fileName = new GcsFilename(PERSONS_ICONS_BUCKET_NAME, strIconName);
        GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(fileName, 0, PERSON_ICON_BUFFER_SIZE);

        return m_gcsTool.copy(Channels.newInputStream(readChannel), os, PERSON_ICON_BUFFER_SIZE);
    }

    List<String> listPersonIcons(String strOwnerName, boolean bIncludeFolder) throws IOException {
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

        if(!bIncludeFolder && !lsResult.isEmpty())
            lsResult.remove(0);

        return lsResult;
    }

    boolean deletePersonIcon(String strIconName) throws IOException {
        if(!m_stringTool.checkStringNotNull(strIconName))
            return false;

        return gcsService.delete(new GcsFilename(PERSONS_ICONS_BUCKET_NAME, strIconName));
    }

    boolean deletePersonIcons(String strOwnerName, boolean bIncludeFolder) throws IOException {
        if(!m_stringTool.checkStringNotNull(strOwnerName))
            return false;

        List<String> lsIcons = listPersonIcons(strOwnerName, bIncludeFolder);

        boolean bRes = true;
        if(lsIcons != null) {
            for (String strIconName : lsIcons) {
                if (!gcsService.delete(new GcsFilename(PERSONS_ICONS_BUCKET_NAME, strIconName)))
                    bRes = false;
            }
        }

        return bRes;
    }

    void createPersonStorage(String strEmail) throws IOException  {
        m_gcsTool.createFolder(PERSONS_ICONS_BUCKET_NAME, strEmail);
    }
}
