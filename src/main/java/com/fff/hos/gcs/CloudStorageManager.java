package com.fff.hos.gcs;

import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Logger;

public class CloudStorageManager {

    private static final Logger LOGGER = Logger.getLogger(CloudStorageManager.class.getName());

    /**
     * This is where backoff parameters are configured. Here it is aggressively retrying with
     * backoff, up to 10 times but taking no more that 15 seconds total to do so.
     */
    private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
            .initialRetryDelayMillis(10)
            .retryMaxAttempts(10)
            .totalRetryPeriodMillis(15000)
            .build());

    private CSPerson m_csPerson = null;
    private CSActivity m_csActivity = null;

    public CloudStorageManager() {

    }

    // --------------------------------- Person control functions ---------------------------------
    public boolean uploadPersonIcon(String strIconName, InputStream is) throws IOException {
        return getCsPerson().uploadPersonIcon(strIconName, is);
    }

    public boolean downloadPersonIcon(String strIconName, OutputStream os) throws IOException {
        return getCsPerson().downloadPersonIcon(strIconName, os);
    }

    public List<String> listPersonIcons(String strOwnerName) throws IOException {
        return getCsPerson().listPersonIcons(strOwnerName);
    }

    public boolean deletePersonIcon(String strIconName) throws IOException {
        return getCsPerson().deletePersonIcon(strIconName);
    }

    public boolean deletePersonIcons(String strOwnerName) throws IOException {
        return getCsPerson().deletePersonIcons(strOwnerName);
    }

    public void createPersonStorage(String strEmail) throws IOException  {
        getCsPerson().createPersonStorage(strEmail);
    }

    private CSPerson getCsPerson(){
        if(m_csPerson == null) {
            try {
                m_csPerson = new CSPerson(gcsService);
            } catch (GcsServiceNullException e) {
                LOGGER.warning(e.getMessage());
            }
        }
        return m_csPerson;
    }

    // --------------------------------- Activity control functions ---------------------------------
    public boolean uploadActivityImage(String strImageName, InputStream is) throws IOException {
        return getCsActivity().uploadActivityImage(strImageName, is);
    }

    public boolean downloadActivityImage(String strImageName, OutputStream os) throws IOException {
        return getCsActivity().downloadActivityImage(strImageName, os);
    }

    public List<String> listActivityImages(String strOwnerName) throws IOException {
        return getCsActivity().listActivityImages(strOwnerName);
    }

    public boolean deleteActivityImage(String strImageName) throws IOException {
        return getCsActivity().deleteActivityImage(strImageName);
    }

    public boolean deleteActivityImages(String strActivityId) throws IOException {
        return getCsActivity().deleteActivityImages(strActivityId);
    }

    public void createActivityStorage(String strId) throws IOException  {
        getCsActivity().createActivityStorage(strId);
    }

    private CSActivity getCsActivity(){
        if(m_csActivity == null) {
            try {
                m_csActivity = new CSActivity(gcsService);
            } catch (GcsServiceNullException e) {
                LOGGER.warning(e.getMessage());
            }
        }
        return m_csActivity;
    }
}
