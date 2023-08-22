package models.responses;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public class BookResponse {
    public void setBooks(List<JsonNode> books) {
        this.books = books;
    }

    public List<JsonNode> getBooks() {
        return books;
    }

    public List<JsonNode> books;

    public BookResponse(List<JsonNode> books){
        this.books = books;
    }
}