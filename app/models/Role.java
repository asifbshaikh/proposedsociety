package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Role extends Model {
    private static final long serialVersionUID = 1L;

    public Role() {
    }

    public Role(User user, String role) {
        super();
        this.user = user;
        this.role = role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(cascade = {CascadeType.DETACH})
    @Required
    public User user;

    @Required
    public String role;
}
