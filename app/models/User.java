package models;

import static utils.CryptUtils.decrypt;
import static utils.CryptUtils.desalt;
import static utils.CryptUtils.encrypt;
import static utils.CryptUtils.salt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;


import org.mindrot.jbcrypt.BCrypt;

import play.data.validation.Constraints;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.Email;
import play.db.ebean.Model;
import common.TextRandomizer;
import common.Utils;

@Entity
public class User extends Model  {
    private static final long serialVersionUID = 1L;
    public interface Update{ }
    public interface Create{ }
    
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
    @Constraints.Required(groups = { Create.class },message = "Required Field!")
    @Email public String email;
    
    @Constraints.Required(groups = { Create.class},message = "Required Field!")  public String name;
    @Constraints.Required(groups = { Create.class},message = "Required Field!")  public String mobile;
	public String password;
	public String applicantPassword; // For storing only applicant password
	public String authcode_hash;
	@OneToOne(cascade = CascadeType.ALL) public ApplicationForm form;
	@OneToMany(cascade = CascadeType.ALL) public List<Role> roles;
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH}, mappedBy = "user") 
	public List<Invoice> invoices; 

	@OneToOne(cascade = CascadeType.ALL) public String resetPassword;
	
	public Boolean interestedInAgent;
	public String authcode;
	public Boolean emailVerified;
	public Boolean phoneVerified;
	@Column(name = "joining_date") public Date joiningDate;
	public Date lastLogin;
	public String lastLoginIp;
	public String api_key;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	public List<FileAttachment> attachments;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	public List<Query> queryList;
	
	//Handling agent filling application form behalf of applicant
	@OneToOne(cascade = CascadeType.ALL)
	public Agent agent ;
	
	private static final Pattern EMAIL_PATTERN = Pattern.compile("[A-z0-9._]+@[A-z0-9]+" + Pattern.quote(".") + "[A-z0-9]+");
	private static final Pattern MOBILE_NUMBER_PATTERN = Pattern.compile("[" + Pattern.quote("+") + "]?[0-9]+");

	private static final Random RANDOM = new Random(System.currentTimeMillis());
	private static final Integer AUTH_CODE_LENGTH = 8;
	private static final Integer API_KEY_LENGTH = 20;
	public static final Finder<Long, User> FIND = new Finder<Long, User>(Long.class, User.class);

	public static void validateEmail(List<ValidationError> errors, String email) {
		if (email == null || email.length() == 0) {
			errors.add(new ValidationError("email", "Email is mandatory"));
		} else if (!EMAIL_PATTERN.matcher(email).matches()) {
			errors.add(new ValidationError("email", "Email is in invalid format"));
		}
	}
	
	public static void validateMobile(List<ValidationError> errors, String mobile) {
		if (mobile == null || mobile.length() == 0) {
			errors.add(new ValidationError("mobile", "Mobile number is mandatory"));
		} else if (!MOBILE_NUMBER_PATTERN.matcher(mobile).matches()) {
			errors.add(new ValidationError("mobile", "Mobile number is in invalid format"));
		}
	}

	public static void validateName(List<ValidationError> errors, String name) {
		if (name == null || name.length() == 0) {
			errors.add(new ValidationError("name", "Name is mandatory"));
		} else if (name.trim().length() == 0) {
			errors.add(new ValidationError("name", "Name cannot be blank"));
		}
	}

	public static String generateCode() {
		return String.valueOf(RANDOM.nextInt(100000));
	}

	public Applicant getApplicant() {
	    return form != null && form.applicant != null ? form.applicant : null; 
	}
	
	public Applicant getCoApplicant() {
	    return form != null && form.co_applicant != null ? form.co_applicant : null; 
	}

	public boolean hasRole(String role) {
	    if (roles != null) {
	        for (Role r : roles) {
	            if (r.role.equalsIgnoreCase(role)) {
	                return true;
	            }
	        }
	    }
	    
	    return false;
	}
	public static User create(String name, String email, String mobile, String role) {
    	User user = new User();
		user.name = name;
    	user.email = email;
    	user.mobile = mobile;
    	user.roles = new ArrayList<Role>();
    	
    	Role userRole = new Role();
    	userRole.role = role;
    	userRole.user = user;
    	
    	user.roles.add(userRole);
    	return user;
	}
	public static User create(String name, String email, String mobile, String role,Agent agent) {
    	User user = new User();
		user.name = name;
    	user.email = email;
    	user.mobile = mobile;
    	user.roles = new ArrayList<Role>();
    	
    	Role userRole = new Role();
    	userRole.role = role;
    	userRole.user = user;
    	user.agent = agent ;
    	user.roles.add(userRole);
    	return user;
	}

	public static String generateAuthCode() {
		return generateRandomString();
	}

	public static  String generatePassword() {
		return generateRandomString();
	}
// This generated password is used to pass unique text in url of enterResetPassword template. I increased password length to 20.
	public static String generateResetPassword(){
		return Utils.TEXT_RANDOMIZER.nextRandomString(TextRandomizer.ALPHA_NUMERIC_ONLY_SMALL_LETTERS, 20);
	}
	
	private static String generateRandomString() {
		return Utils.TEXT_RANDOMIZER.nextRandomString(TextRandomizer.ALPHA_NUMERIC_ONLY_SMALL_LETTERS, AUTH_CODE_LENGTH);
	}
	
	public static String generateRandomApiKey() {
		return Utils.TEXT_RANDOMIZER.nextRandomString(TextRandomizer.ALPHA_NUMERIC_ONLY_SMALL_LETTERS, API_KEY_LENGTH);
	}
	
	public boolean isCreatedByAgent(){
		return agent != null ? true : false;
	}
	
	public  boolean checkPassword(String password){
		return BCrypt.checkpw(password.trim(),this.password);
	}
	
	
	public String showRoles(List<Role> roles) {
		String roleList="";
    	if (! roles.isEmpty()){
	        for (Role r : roles) {
	            roleList =roleList.concat(r.role +",");
	        }
	        return roleList.substring(0, roleList.length()-1);
	    }
	    return roleList;
	}
	//To pass in URL
	public String encryptEmail() {
        String salted = salt(String.valueOf(this.email));
        String encrypted = encrypt(salted);
        return encrypted;
    }
	public static String decryptEmail(String encryptedEmail)  {
	    String email = desalt(decrypt(encryptedEmail));
	    return email;
	}

}