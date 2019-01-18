package controllers;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import models.FileAttachment;
import models.User;
import models.dao.DaoProvider;
import models.dao.FileAttachmentDao;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import views.html.attachmentTest;

import com.google.inject.Inject;

import common.ApplicationConstants;
import common.ConfigUtils;
import common.Roles;
import common.TextRandomizer;

public final class AttachmentsController extends AuthenticatedUserController {
    private static final Logger LOG = LoggerFactory.getLogger(AttachmentsController.class);
    private static final String CONFIG_ROOT_DIR = "attachments.rootdir";
    private final File rootDir;
    private final FileAttachmentDao fileAttachmentDao;
    private final TextRandomizer randomizer;
    @Inject
    public AttachmentsController(DaoProvider provider, TextRandomizer randomizer) {
        super(provider.userDao());
        this.fileAttachmentDao = provider.fileAttachmentDao();
        this.randomizer = randomizer;
        this.rootDir = new File(ConfigUtils.getConfigString(CONFIG_ROOT_DIR, true));
        if (!rootDir.isDirectory() && (!rootDir.exists() && !rootDir.mkdirs())) {
            throw new RuntimeException("Invalid root directory for files: " + rootDir.getAbsolutePath());
        }
    }

    public Result upload() {
        MultipartFormData body = request().body().asMultipartFormData();
        FilePart filePart = body.getFile("file");
        if (filePart != null) {
            String fileName = filePart.getFilename();
            String contentType = filePart.getContentType();
            File file = filePart.getFile();
            LOG.debug("Received from user: " + currentUser().id + " file: " + file.getAbsolutePath() + " size: "
                    + file.length() + " name: " + fileName + " content-type: " + contentType);

            User user = currentUser();
            File userDir = new File(rootDir, String.valueOf(user.id));
            String generatedName = generateFileName();
            File dest = new File(userDir, generatedName);

            if (!userDir.exists() && !userDir.mkdirs()) {
                LOG.error("Failed to create user directory: " + userDir.getAbsolutePath());
                return badRequest();
            }

            try {
                FileUtils.copyFile(file, dest);
                FileUtils.forceDelete(file);
            } catch (IOException ioe) {
                LOG.error("Failed to copy file from temp area to files directory temp file: " + file.getAbsolutePath()
                        + " destination: " + dest.getAbsolutePath(), ioe);
                return badRequest();
            }

            FileAttachment attachment = new FileAttachment();
            attachment.contentType = contentType;
            attachment.fileName = fileName;
            attachment.user = user;
            attachment.filePath = generatedName;
            attachment.uploadTime = new Date();
            
            fileAttachmentDao.save(attachment);

            return ok(String.valueOf(attachment.filePath));
        }

        return badRequest();
    }

    private String generateFileName() {
        return randomizer.nextRandomString(5) + "_" + System.currentTimeMillis();
    }

    public Result delete(String filePath) {
        User user = currentUser();
        FileAttachment attachment = fileAttachmentDao.findByFilePath(filePath, user);
        // User owns the file
        if (attachment != null) {
            File userDir = new File(rootDir, String.valueOf(user.id));
            File file = new File(userDir, attachment.filePath);
            fileAttachmentDao.delete(attachment);
            
            if (file.exists()) {
                // Ignore failure for now
                file.delete();
            } else {
                LOG.warn("Physical file: " + file.getAbsolutePath() + " not existent for file with id: " + attachment.id);
            }
        } else {
            LOG.warn("Attempt to delete a not existent or unauthorized file: " + filePath + " by user: " + user.id);
        }
        return ok("deleted");
    }

    public Result download(String id) {
      // We can add any user to download 
    	
      String email = session(ApplicationConstants.SESSION_KEY_USER);
      LOG.info("Found user id: " + email);
      User user =  userDao.findUniqueByEmail(email);
        if(user.hasRole(Roles.BUERO)||user.hasRole(Roles.ADMIN)){
        	FileAttachment attachment = fileAttachmentDao.findByFilePath(id);
        	if (attachment != null) {
                File file = retrieveFile(attachment);
                if (file.exists()) {
                    if (attachment.contentType != null && attachment.contentType.trim().length() > 0) {
                        response().setContentType(attachment.contentType);
                    } else {
                        response().setContentType("application/x-download");
                    }
                    
                    response().setHeader("Content-disposition","attachment; filename=" + attachment.fileName);
                    LOG.info("Attempt to download file with id "+id+" owned by "+attachment.user.id+" by "+user.id+" having roles "+user.showRoles(user.roles));
                    return ok(file);
                } else {
                    LOG.warn("File with id: " + id + " has record in database but physical file " + attachment.filePath + " seems to be deleted for user: " + user.id);
                }
            } else {
                LOG.warn("Attempt to download non-existent or unuthorized file with id: " + id + " by user: " + user.id);
            }
        	
        }
      
        return badRequest("Attempt to download non-existent or unuthorized file" );
    }
    
    public Result getApplicantImage(Long id) {
        User user =  currentUser();
        FileAttachment attachment = fileAttachmentDao.findByFilePathId(id);
      	if (attachment != null) {
              File file = retrieveFile(attachment);
              if (file.exists()) {
                  if (attachment.contentType != null && attachment.contentType.trim().length() > 0) {
                      response().setContentType(attachment.contentType);
                  } else {
                      response().setContentType("application/x-download");
                  }
                  
                  response().setHeader("Content-disposition","attachment; filename=" + attachment.fileName);
                  LOG.info("Attempt to Load file with id "+id+" owned by "+attachment.user.id+" by "+user.id+" having roles "+user.showRoles(user.roles));
                  return ok(file);
              } else {
                  LOG.warn("File with id: " + id + " has record in database but physical file " + attachment.filePath + " seems to be deleted for user: " + user.id);
              }
          } else {
              LOG.warn("Attempt to Load non-existent or unuthorized file with id: " + id + " by user: " + user.id);
          }
        
          return badRequest("Attempt to Load non-existent or unuthorized file" );
    }
    
    public Result test() {
        return ok(attachmentTest.render());
    }

    public File retrieveFile(FileAttachment attachment) {
        return new File(new File(rootDir, String.valueOf(attachment.user.id)), attachment.filePath);
    }
}
