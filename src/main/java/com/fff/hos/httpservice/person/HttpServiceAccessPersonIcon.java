package com.fff.hos.httpservice.person;

import com.fff.hos.data.Person;
import com.fff.hos.database.CloudSQLManager;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.tools.cloudstorage.*;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;

@WebServlet(name = "HttpServiceAccessPersonIcon", urlPatterns = {"/accesspersonicon/*"},
        initParams = { @WebInitParam(name = "gcsBucketName", value = "persons_icons")})
public class HttpServiceAccessPersonIcon extends HttpServlet {

    private static final boolean SERVE_USING_BLOBSTORE_API = false;

    /**
     * This is where backoff parameters are configured. Here it is aggressively retrying with
     * backoff, up to 10 times but taking no more that 15 seconds total to do so.
     */
    private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
            .initialRetryDelayMillis(10)
            .retryMaxAttempts(10)
            .totalRetryPeriodMillis(15000)
            .build());

    /**Used below to determine the size of chucks to read in. Should be > 1kb and < 10MB */
    private static final int BUFFER_SIZE = 2 * 1024 * 1024;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("image/jpg");

        String strBucketName = getInitParameter("gcsBucketName");
        String strFileName = getFileName(request);

        GcsFilename fileName = new GcsFilename(strBucketName, strFileName);

        if (SERVE_USING_BLOBSTORE_API) {
            BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
            BlobKey blobKey = blobstoreService.createGsBlobKey(
                    "/gs/" + fileName.getBucketName() + "/" + fileName.getObjectName());
            blobstoreService.serve(blobKey, response);
        } else {
            GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(fileName, 0, BUFFER_SIZE);

            if(!copy(Channels.newInputStream(readChannel), response.getOutputStream())) {
                response.setContentType("application/json");
                JsonObject jsonObj = new JsonObject();
                jsonObj.addProperty("statuscode", 1);
                jsonObj.addProperty("status", "access fail, icon is not exist");
                response.getOutputStream().print(jsonObj.toString());
                response.flushBuffer();
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        JsonObject jsonObj = new JsonObject();

        String strBucketName = getInitParameter("gcsBucketName");
        String strOwnerName = getOwnerName(request);
        String strFileName = getFileName(request);

        CloudSQLManager sqlManager = new CloudSQLManager();

        //TODO - There has a concern that is no verify user password
        if(sqlManager.checkPersonExist(strOwnerName)) {
            GcsFileOptions instance = GcsFileOptions.getDefaultInstance();
            GcsFilename fileName = new GcsFilename(strBucketName, strFileName);
            GcsOutputChannel outputChannel;
            outputChannel = gcsService.createOrReplace(fileName, instance);

            if(copy(request.getInputStream(), Channels.newOutputStream(outputChannel))) {
                Person person = new Person();
                person.setEmail(strOwnerName);
                person.setIcon(strFileName);
                sqlManager.updatePerson(person);
            }

            jsonObj.addProperty("statuscode", 0);
        } else {
            jsonObj.addProperty("statuscode", 1);
            jsonObj.addProperty("status", "access fail, user is not exist");
        }

        response.getWriter().print(jsonObj.toString());
        response.flushBuffer();
    }

    //e.g. https://hangouttw.appspot.com/accesspersonicon/jimmy2@gmail.com/jimmy2@gmail.com_icon01.jpg
    //           //         0           /       1        /       2        /         3
    private String getOwnerName(HttpServletRequest req) {
        String[] splits = req.getRequestURI().split("/");
        return splits[2];
    }

    private String getFileName(HttpServletRequest req) {
        String[] splits = req.getRequestURI().split("/");
        return splits[3];
    }

    private boolean copy(InputStream input, OutputStream output) throws IOException {
        boolean bRes = true;

        try {
            byte[] buffer = new byte[BUFFER_SIZE];
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
