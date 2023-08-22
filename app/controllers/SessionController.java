package controllers;

import models.Book;
import models.Login;
import play.api.mvc.RequestHeader;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SessionController extends Controller {
    @Inject
    FormFactory formFactory;

    public Result sessionIndex(Http.Request request) {
        return ok(views.html.Authentication.loginSession.render());
    }

    public Result sessionView(Http.Request request){
        Optional<String> cookieUsername = request.session().get("userCookie");
        if(cookieUsername.isPresent()){
            List<Book> booksToShow = Book.booksList().stream().filter(book -> book.author.equals(cookieUsername.get())).collect(Collectors.toList());
            return ok(views.html.Books.list.render(cookieUsername.get(), booksToShow));
        } else return redirect(routes.SessionController.sessionIndex());
    }

    public Result loginWithSession(Http.Request request){
        Form<Login> loginForm = formFactory.form(Login.class).bindFromRequest(request);
        if (loginForm.hasErrors()){
            return redirect(routes.SessionController.sessionIndex());
        } else {
            Login login = loginForm.get();
            Login.addUser(login);
            return redirect(routes.SessionController.sessionView()).addingToSession(request, "userCookie", login.username);
        }
    }

    public Result logoutWithSession(Http.Request request){
        Optional<String> cookieUsername = request.session().get("userCookie");
        if(cookieUsername.isPresent()){
            Optional<Login> user = Login.findUser(cookieUsername.get());
            if (user.isPresent()){
                Login.removeUser(user.get());
                return redirect(routes.SessionController.sessionIndex()).withNewSession();
            } else return ok(views.html.Authentication.logout.render()).withNewSession();
        } else return redirect(routes.SessionController.sessionIndex()).withNewSession();
    }
}
