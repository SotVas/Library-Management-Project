package project.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.library.entity.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {

    Book findById(int id);

    List<Book> findAll();
    List<Book> findAllByStockGreaterThan(Integer stock);
}
