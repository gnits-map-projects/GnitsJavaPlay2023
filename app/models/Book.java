package models;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Book {
    public Integer id;
    public String title;
    public String author;
    public Integer price;

    public Book(Integer id, String title, String author, Integer price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
    }

    public Book() {

    }

    private static Set<Book> books;

    static {
        books = new HashSet<>();
        books.add(new Book(1, "C++", "user3", 400));
        books.add(new Book(2, "JAVA", "user2", 500));
        books.add(new Book(3, "PYTHON", "user3", 200));
        books.add(new Book(4, "GO", "user2", 100));
    }

    public static Set<Book> booksList(){
        return books;
    }

    public static Optional<Book> findBook(Integer id) {
        for (Book book : books) {
            if (book.id.equals(id)) {
                return Optional.of(book);
            }
        }
        return Optional.empty();
    }

    public static void add(Book book){
        books.add(book);
    }

    public static void remove(Book book){
        books.remove(book);
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Integer getPrice() {
        return price;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Boolean isValid(){
        return !(this.id == null || this.author == null || this.price == null || this.title == null);
    }

    public Boolean anyThingPassed(){
        return (this.id != null || this.author != null || this.price != null || this.title != null);
    }

    public void update(Book book){
        if (book.id !=null) {
            this.id = book.id;
        }
        if (book.author !=null) {
            this.author = book.author;
        }
        if (book.price !=null) {
            this.price = book.price;
        }
        if (book.title !=null) {
            this.title = book.title;
        }
    }
}
