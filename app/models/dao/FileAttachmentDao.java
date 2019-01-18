package models.dao;

import models.FileAttachment;
import models.User;

public interface FileAttachmentDao extends Dao<Long, FileAttachment> {
	FileAttachment findById(String id,User user);
	FileAttachment findByFilePath(String filepath, User user);
	FileAttachment findByFilePath(String filepath);
	FileAttachment findByFilePathId(Long filepath);
}
