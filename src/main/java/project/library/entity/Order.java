package project.library.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    private Integer id;

    private Boolean due;
    private Timestamp dateoforder;
    private User userByUserId;
    private Book bookByBookId;
    private Integer status;
    private Timestamp dateOfReturn;

    public Order(Integer id, Timestamp dateoforder, User userByUserId, Book bookByBookId, Integer status, Timestamp dateOfReturn,boolean due) {
        this.id = id;
        this.dateoforder = dateoforder;
        this.userByUserId = userByUserId;
        this.bookByBookId = bookByBookId;
        this.status = status;
        this.dateOfReturn = dateOfReturn;
        this.due = due;
    }

    public Order() {
    }

    @Id
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "dateoforder")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Timestamp getDateoforder() {
        return dateoforder;
    }
   @Basic
   @Column(name="due")
   public Boolean isDue(){return due;}

    public void setDue(Boolean due) {
        this.due = due;
    }

    public void setDateoforder(Timestamp dateoforder) {
        this.dateoforder = dateoforder;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    public User getUserByUserId() {
        return userByUserId;
    }

    public void setUserByUserId(User userByUserId) {
        this.userByUserId = userByUserId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "book_id", nullable = false)
    public Book getBookByBookId() {
        return bookByBookId;
    }

    public void setBookByBookId(Book bookByBookId) {
        this.bookByBookId = bookByBookId;
    }

    @Basic
    @Column(name = "date_of_return")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Timestamp getDateOfReturn() {
        return dateOfReturn;
    }

    public void setDateOfReturn(Timestamp dateOfReturn) {
        this.dateOfReturn = dateOfReturn;
    }

    @PrePersist
    protected void onCreate() {
        // Set dateOfReturn to 7 days after dateoforder when the entity is persisted
        if (dateoforder != null) {
            LocalDateTime orderLocalDateTime = dateoforder.toLocalDateTime();
            LocalDateTime returnDate = orderLocalDateTime.plusDays(7);
            this.dateOfReturn = Timestamp.valueOf(returnDate);
        }
    }

}
