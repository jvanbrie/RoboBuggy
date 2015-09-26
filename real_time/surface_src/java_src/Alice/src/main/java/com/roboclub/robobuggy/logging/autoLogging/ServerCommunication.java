package com.roboclub.robobuggy.logging.autoUploader;


/*
 * Copyright (c) 2012 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

//TODO add upload only if has not yet been uploaded
//TODO add ability to list the directory and see which ones have been download and chose what to download 
//TOOD package in a factory for easy use
//TODO add check for Internet connection
//TODO add needed lib files 

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import com.google.api.services.samples.drive.cmdline.DriveSample;
import com.google.api.services.samples.drive.cmdline.FileDownloadProgressListener;
import com.google.api.services.samples.drive.cmdline.FileUploadProgressListener;
import com.google.api.services.samples.drive.cmdline.View;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class ServerCommunication {

  // Be sure to specify the name of your application. If the application name is {@code null} or
  // blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".

  private static final String APPLICATION_NAME = "Trevor-Test";

  private static final String DIR_FOR_DOWNLOADS = "LOG_FILES";
  private static final java.io.File UPLOAD_FILE = new java.io.File("LOG_FILES");

  // / Directory to store user credentials.
  private static final java.io.File DATA_STORE_DIR = new java.io.File(
      System.getProperty("user.home"), ".store/drive_sample");


  // / Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
  // / globally shared instance across your application.
  private static FileDataStoreFactory dataStoreFactory;

  // / Global instance of the HTTP transport.
  private static HttpTransport httpTransport;

  // / Global instance of the JSON factory.
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  // / Global Drive API client.
  private static Drive drive;

  // / Authorizes the installed application to access user's protected data.
  private static Credential authorize() throws Exception {
    // load client secrets
    GoogleClientSecrets clientSecrets =
        GoogleClientSecrets.load(JSON_FACTORY,
            new InputStreamReader(DriveSample.class.getResourceAsStream("/client_secrets.json")));
    if (clientSecrets.getDetails().getClientId().startsWith("Enter")
        || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
      System.out
          .println("Enter Client ID and Secret from https://code.google.com/apis/console/?api=drive "
              + "into drive-cmdline-sample/src/main/resources/client_secrets.json");
      System.exit(1);
    }
    // set up authorization code flow
    GoogleAuthorizationCodeFlow flow =
        new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
            Collections.singleton(DriveScopes.DRIVE_FILE)).setDataStoreFactory(dataStoreFactory)
            .build();
    // authorize
    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
  }

  public static void main(String[] args) {
    System.out.println("Working directory" + System.getProperty("user.dir"));

    // Preconditions.checkArgument(
    // !UPLOAD_FILE_PATH.startsWith("Enter ") && !DIR_FOR_DOWNLOADS.startsWith("Enter "),
    // "Please enter the upload file path and download directory in %s", DriveSample.class);
    try {
      httpTransport = GoogleNetHttpTransport.newTrustedTransport();
      dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
      // authorization
      Credential credential = authorize();
      // set up the global Drive instance
      drive =
          new Drive.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(
              APPLICATION_NAME).build();

      ParentReference root_dir = new ParentReference();
      root_dir.setId("0B1IjfVrCn6dNZjZfems2ZUlXNlE");
      uploadFolder(UPLOAD_FILE, root_dir);

      /*
       * // run commands View.header1("Creating a new log folder"); File folder =
       * createFolder(false, "newLogFolder", Arrays.asList(new
       * ParentReference().setId("0B1IjfVrCn6dNZjZfems2ZUlXNlE")));
       * 
       * View.header1("Starting Resumable Media Upload"); File uploadedFile = uploadFile(false,
       * Arrays.asList(new ParentReference().setId(folder.getId())));
       */
      /*
       * 
       * View.header1("Updating Uploaded File Name"); File updatedFile =
       * updateFileWithTestSuffix(uploadedFile.getId());
       * 
       * View.header1("Starting Resumable Media Download"); downloadFile(false, updatedFile);
       * 
       * View.header1("Starting Simple Media Upload"); uploadedFile = uploadFile(true);
       * 
       * View.header1("Starting Simple Media Download"); downloadFile(true, uploadedFile);
       */

      View.header1("Success!");

      return;
    } catch (IOException e) {
      System.err.println(e.getMessage());
    } catch (Throwable t) {
      t.printStackTrace();
    }
    System.exit(1);

  }

  private static void uploadFolder(java.io.File inputFile, ParentReference whereToSave)
      throws IOException {
    // todo mark as uploaded
    String[] files = inputFile.list();
    for (int i = 0; i < files.length; i++) {
      java.io.File thisFile = new java.io.File(inputFile.getAbsolutePath() + "/" + files[i]);
      if (!thisFile.isHidden()) {
        if (thisFile.isDirectory()) {
          // upload directory
          File thisFolder = createFolder(false, files[i], Arrays.asList(whereToSave));
          ParentReference parent = new ParentReference();
          parent.setId(thisFolder.getId());
          uploadFolder(thisFile, parent);
        } else {
          // upload file
          uploadFile(false, Arrays.asList(whereToSave), thisFile);
        }
      }
    }

  }

  // create a new folder at a specfic location, set parent to be null if you want it to be root
  private static File createFolder(boolean useDirectUpload, String title,
      List<ParentReference> parentList) throws IOException {
    File body = new File();
    body.setTitle(title);
    body.setMimeType("application/vnd.google-apps.folder");
    body.setParents(parentList);
    File file = drive.files().insert(body).execute();
    return file;
  }

  /*
   * // If the folder has a uploaded textfile with the value "false" then Recursively walks down the
   * // folder uploading all files and sub folders private static File uploadFolder(boolean
   * useDirectUpload, File folder) { UPLOAD_FILE.list(); // TODO return folder; }
   */

  // TODO add download stuff

  /** Uploads a file using either resumable or direct media upload. */
  private static File uploadFile(boolean useDirectUpload, List<ParentReference> parentList,
      java.io.File fileToUpload) throws IOException {
    File fileMetadata = new File();
    fileMetadata.setTitle(fileToUpload.getName());
    fileMetadata.setParents(parentList);
    FileContent mediaContent = new FileContent("image/jpeg", fileToUpload);
    System.out.println(fileMetadata);
    System.out.println(mediaContent);
    Drive.Files.Insert insert = drive.files().insert(fileMetadata, mediaContent);
    MediaHttpUploader uploader = insert.getMediaHttpUploader();
    uploader.setDirectUploadEnabled(useDirectUpload);
    uploader.setProgressListener(new FileUploadProgressListener());
    return insert.execute();
  }

  /** Updates the name of the uploaded file to have a "drivetest-" prefix. */
  private static File updateFileWithTestSuffix(String id) throws IOException {
    File fileMetadata = new File();
    fileMetadata.setTitle("drivetest-" + UPLOAD_FILE.getName());

    Drive.Files.Update update = drive.files().update(id, fileMetadata);
    return update.execute();
  }

  /** Downloads a file using either resumable or direct media download. */
  private static void downloadFile(boolean useDirectDownload, File uploadedFile) throws IOException {
    // create parent directory (if necessary)
    java.io.File parentDir = new java.io.File(DIR_FOR_DOWNLOADS);
    if (!parentDir.exists() && !parentDir.mkdirs()) {
      throw new IOException("Unable to create parent directory");
    }
    OutputStream out = new FileOutputStream(new java.io.File(parentDir, uploadedFile.getTitle()));

    MediaHttpDownloader downloader =
        new MediaHttpDownloader(httpTransport, drive.getRequestFactory().getInitializer());
    downloader.setDirectDownloadEnabled(useDirectDownload);
    downloader.setProgressListener(new FileDownloadProgressListener());
    downloader.download(new GenericUrl(uploadedFile.getDownloadUrl()), out);
  }
}
