package controllers;

import models.Book;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.CSRF;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class SessionController extends Controller {
    @Inject
    FormFactory formFactory;

    public CompletionStage<Result> sessionIndex(Http.Request request) {
        Optional<String> cookieUsername = request.session().get("userCookie");
        if(cookieUsername.isPresent()){
            return CompletableFuture.completedFuture(redirect(routes.SessionController.sessionView()));
        } else return CompletableFuture.completedFuture(ok(views.html.Authentication.loginSession.render(request)));
    }

    public Result sessionView(Http.Request request){
        Optional<String> cookieUsername = request.session().get("userCookie");
        if(cookieUsername.isPresent()){
            Optional<User> userRecord = User.findUser(cookieUsername.get());
            if (userRecord.isPresent()){
                if (userRecord.get().isAdmin()){
                    return ok(views.html.Books.list.render(request, cookieUsername.get(), new ArrayList<>(Book.booksList()), userRecord.get().role));
                } else {
                    List<Book> booksToShow = Book.booksList().stream().filter(book -> book.author.equals(cookieUsername.get())).collect(Collectors.toList());
                    return ok(views.html.Books.list.render(request, cookieUsername.get(), booksToShow, userRecord.get().role));
                }
            } else return notFound();
        } else return redirect(routes.SessionController.sessionIndex());
    }

    public Result loginWithSession(Http.Request request){
        Form<User> loginForm = formFactory.form(User.class).bindFromRequest(request);
        if (loginForm.hasErrors()){
            return redirect(routes.SessionController.sessionIndex());
        } else {

            User login = loginForm.get();
            Optional<User> userRecord = User.findUser(login.username);
            if (userRecord.isPresent()){
                if (userRecord.get().validateCreds(login)) {
                    return redirect(routes.SessionController.sessionView()).addingToSession(request, "userCookie", login.username);
                } else return unauthorized();
            } else return unauthorized(); // notFound();
        }
    }

    public Result logoutWithSession(Http.Request request){
        Optional<String> cookieUsername = request.session().get("userCookie");
        Optional<CSRF.Token> token = CSRF.getToken(request);
        if(cookieUsername.isPresent()){
            Optional<User> user = User.findUser(cookieUsername.get());
            if (user.isPresent()){
                return redirect(routes.SessionController.sessionIndex()).withNewSession();
            } else return forbidden();
        } else return redirect(routes.SessionController.sessionIndex());
    }

    public Result deleteUserAndSession(Http.Request request){
        Optional<String> cookieUsername = request.session().get("userCookie");
        Optional<CSRF.Token> token = CSRF.getToken(request);
        if(cookieUsername.isPresent()){
            Optional<User> user = User.findUser(cookieUsername.get());
            if (user.isPresent()){
                User.remove(user.get());
                return redirect(routes.SessionController.sessionIndex()).withNewSession();
            } else return forbidden();
        } else return redirect(routes.SessionController.sessionIndex());
    }
}
