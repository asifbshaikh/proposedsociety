package controllers;

import models.Page;
import models.dao.DaoProvider;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import views.html.admin.pages.show;

import com.google.inject.Inject;

public class Pages extends AuthenticatedUserController {

	@Inject
	public Pages(DaoProvider provider) {
		super(provider.userDao());
	}

	public Result show(String title) {
		Page page = Page.findByFormattedTitle(title);
		if (page != null) {
			return ok(show.render(page, currentUser()));
		} else {
			return notFound();
		}
	}
	
	@BodyParser.Of(BodyParser.Json.class)
	public Result showPages(){
		JsonNode json = request().body().asJson();
		JsonNode jsonTitle = json.get("title");
		String title = jsonTitle.getTextValue();
		@SuppressWarnings("unused")
		ObjectNode response = Json.newObject();
		Page page = Page.findByFormattedTitle(title);
		return ok(page.content);
	}
}