package controllers;

import models.Book;
import models.Login;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AuthenticationController extends Controller {

    @Inject FormFactory formFactory;

    public Result index(Http.Request request) {
        return ok(views.html.Authentication.login.render(request));
    }

    public Result homeView(Http.Request request, String username) {
        return ok(views.html.Authentication.index.render(request, username, Login.getLoggedInUsers()));
    }

    public Result login(Http.Request request){
        Form<Login> loginForm = formFactory.form(Login.class).bindFromRequest(request);
        if (loginForm.hasErrors()){
            return redirect(routes.AuthenticationController.index());
        } else {
            Login login = loginForm.get();
            Login.addUser(login);
            return redirect(routes.AuthenticationController.homeView(login.username));
        }
    }

    public Result logout(String username){
        Optional<Login> user = Login.findUser(username);
        if (user.isPresent()){
            Login.removeUser(user.get());
            return redirect(routes.AuthenticationController.index());
        } else return badRequest();
    }
}