package project.library.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.library.entity.Book;
import project.library.helper.OrderNotFoundException;
import project.library.repository.BookRepository;
import project.library.service.BookService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {


    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository){this.bookRepository= bookRepository;}
    @Override
    public List<Book> findBooksByStockGreaterThanZero() {
        return bookRepository.findAllByStockGreaterThan(0);
    }


    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book findById(int id) {return bookRepository.findById(id);}

    @Override
    public void delete(Integer bookId) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        bookRepository.deleteById(bookId);
        if (optionalBook.isPresent()) {
            // Delete the order
            bookRepository.deleteById(bookId);
        } else {
            throw new OrderNotFoundException("Book not found with ID: " + bookId);
        }
    }


    @Override
    public List<Book> findAll() {return bookRepository.findAll();}

    @Override
    @Transactional
    public void edit(int id, Book book) throws IOException {
        if (book == null || book.getTitle() == null || book.getAuthor() == null || book.getGenre() == null) {
            throw new IllegalArgumentException("Invalid book data");
        }

        Book found = bookRepository.findById(id);

        if (found != null) {
            found.setTitle(book.getTitle());
            found.setAuthor(book.getAuthor());
            found.setGenre(book.getGenre());
            found.setStock(book.getStock());
            try {
                bookRepository.save(found);
            } catch (Exception e) {
                throw new IOException("Error updating book: " + e.getMessage());
            }
        } else {
            throw new IOException("Invalid Book ID");
        }
    }



    }

