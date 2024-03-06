package project.library.entity;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="book_id")
    private Integer id;

    @Column(name="title",nullable = false)
    private String title;

    @Column(name="author",nullable = false)
    private String author;

    @Column(name="genre",nullable = false)
    private String genre;

    @Column(name="stock", nullable = false)
    private Integer stock;

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", stock=" + stock +
                '}';
    }
}

