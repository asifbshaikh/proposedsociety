package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Page extends Model {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
	public String formattedTitle;
	@Required public String title;
	@Column(length = 65000) public String content;	 

	public static final Finder<Long, Page> FIND = new Finder<Long, Page>(Long.class, Page.class);

	public static Page findById(Long id) {
		return FIND.byId(id);
	}

	public static Page findByFormattedTitle(String title) {
		return FIND.where().eq("formattedTitle", title).findUnique();
	}

	public static List<Page> findAll() {
		return FIND.all();
	}

	public static String formatTitle(String title) {
		return title.toLowerCase().replaceAll(" ", "-");
	}
}