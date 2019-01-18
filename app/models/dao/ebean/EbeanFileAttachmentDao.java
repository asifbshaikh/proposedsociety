package models.dao.ebean;

import com.avaje.ebean.Ebean;

import play.db.ebean.Model.Finder;
import models.FileAttachment;
import models.User;
import models.dao.FileAttachmentDao;

public class EbeanFileAttachmentDao extends AbstractEbeanDao<Long, FileAttachment> implements FileAttachmentDao {

    public EbeanFileAttachmentDao() {
        super(new Finder<Long, FileAttachment>(Long.class, FileAttachment.class));
    }

    @Override
    public FileAttachment findById(String id, User user) {
        String query = "find attachment fetch user where id = :id and user.id = :user_id";
        return Ebean.createQuery(FileAttachment.class, query)
                .setParameter("id", id)
                .setParameter("user_id", user.id).findUnique();
    }

	@Override
	public FileAttachment findByFilePath(String filepath, User user) {
			return Ebean.find(FileAttachment.class).where().eq("file_path",filepath).eq("user_id", user.id).findUnique();
	}

	@Override
	public FileAttachment findByFilePath(String filepath) {
		// TODO Auto-generated method stub
		return Ebean.find(FileAttachment.class).where().eq("file_path",filepath).findUnique();
	} 
	
	@Override
	public FileAttachment findByFilePathId(Long id) {
		// TODO Auto-generated method stub
		return Ebean.find(FileAttachment.class).where().eq("id",id).findUnique();
	}
}
