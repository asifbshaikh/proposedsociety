package models;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import play.db.ebean.Model;

@Entity
@Table(name = "attachment")
public class FileAttachment extends Model {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Long id;
    
    public String contentType;
    public String fileName;
    public String filePath;
    public Date uploadTime;
    
    @ManyToOne
    public User user;
}
