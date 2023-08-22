package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.Config;
import models.Login;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class HomeController extends Controller {

    private final Config config;

    @Inject
    public HomeController(Config config) {
        this.config = config;
    }

    public Result index(){
        Set<Login> loggedInUsers = Login.getLoggedInUsers();
        return ok(views.html.index.render("", loggedInUsers));
    }

    public Result welcome(){
        List<String> locations = config.getStringList("college.gnits.location");
        return ok(views.html.welcome.render(locations));
    }

    public Result getKey(){
        String key = config.getString("play.http.secret.key");
        return ok(views.html.welcome1.render("", key));
    }

    public Result welcome1(String name){
        return ok(views.html.welcome1.render(name, ""));
    }

    public Result welcome2(Http.Request request, String name){
        Optional<String> lastName = request.queryString("lasName");
        if (lastName.isPresent()){
            return ok(views.html.welcome1.render(name, lastName.get()));
        } else return ok(views.html.welcome1.render(name, ""));
    }

    public Result asText(){
        List<String> locations = config.getStringList("college.gnits.location");
        return ok(locations.toString());
    }

    public Result asJson(){
        // Object conversion
//        class Person {
//            public String name;
//        }
//        Person obj = new Person();
//        obj.name = "someTestUser";
//        return ok(Json.toJson(obj));

        List<String> locations = config.getStringList("college.gnits.location");
        ObjectNode jsonObject = Json.newObject();
        jsonObject.put("locations", Json.toJson(locations));
        return ok(jsonObject);
    }
}

