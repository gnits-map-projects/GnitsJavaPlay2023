package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Book;
import models.responses.BookResponse;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookController extends Controller {

    public Result editBook(Http.Request request, Integer id){
        Optional<Book> book = Book.findBook(id);
        if (book.isPresent() && book.get().anyThingPassed()){
            Optional<Book> bookOpt = request.body().parseJson(Book.class);
            book.get().update(bookOpt.get());
            return ok(Json.newObject());
        } else return notFound();
    }

    public Result addBook(Http.Request request){
        Book book = Json.fromJson(request.body().asJson(), Book.class);
        Optional<Book> bookOpt = request.body().parseJson(Book.class);
        if (bookOpt.isPresent() && bookOpt.get().isValid()){
            Book.add(bookOpt.get());
            return ok(Json.newObject());
        } else return badRequest();
    }

    public Result deleteBook(Integer id){
        Optional<Book> book = Book.findBook(id);
        if (book.isPresent()){
            Book.remove(book.get());
            return ok(Json.newObject());
        } else return notFound();
    }

    public Result listBooks(){
        List<JsonNode> books = Book.booksList().stream().map(book -> Json.toJson(book)).collect(Collectors.toList());
        BookResponse response = new BookResponse(books);
        return ok(Json.toJson(response));
    }
}
