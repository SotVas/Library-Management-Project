package project.library.service;

import project.library.entity.Book;

import java.io.IOException;
import java.util.List;

public interface BookService {

    Book save(Book book);

    Book findById(int id);

    List<Book> findAll();
    List<Book> findBooksByStockGreaterThanZero();
    void edit (int id , Book book ) throws IOException;

    void delete(Integer id);

}
