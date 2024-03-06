package project.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.library.entity.Order;
import project.library.entity.User;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
    Order findById(int id);

    List<Order> findAll();

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.bookByBookId")
    List<Order> findAllWithBooks();
    List<Order> findByUserByUserId(User user);



    @Query("SELECT o FROM Order o WHERE o.dateOfReturn < CURRENT_TIMESTAMP")
    List<Order> findOverdueOrders();

    @Query("SELECT o FROM Order o WHERE o.userByUserId = :user AND o.dateOfReturn < CURRENT_TIMESTAMP")
    List<Order> findUserOverdueOrders(User user);
    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.due = true WHERE o.dateOfReturn < CURRENT_TIMESTAMP AND o.due = false")
    void updateDueOrders();
}
