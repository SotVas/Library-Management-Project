package project.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.library.entity.Book;
import project.library.entity.User;
import project.library.service.BookService;
import project.library.service.UserService;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add";
    }

    @PostMapping("/add")
    public String saveBook(@ModelAttribute Book book) {
        bookService.save(book);
        return "/add";
    }


    @GetMapping("/{id}/delete")
    public String showDeleteConfirmation(@PathVariable Integer id, Model model) {
        Book book = bookService.findById(id);

        if (book == null) {
            return "redirect:/books";
        }

        model.addAttribute("book", book);
        model.addAttribute("deleteMode", true);
        return "list";
    }

    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {

            Book book = bookService.findById(id);


            if (book != null) {
                bookService.delete(id);
                redirectAttributes.addFlashAttribute("successMessage", "Book deleted successfully.");
            } else {

                redirectAttributes.addFlashAttribute("errorMessage", "Book with ID " + id + " not found.");
            }
        } catch (EmptyResultDataAccessException e) {

            redirectAttributes.addFlashAttribute("errorMessage", "Book with ID " + id + " not found.");
        }


        return "redirect:/books";
    }




    @GetMapping("/{id}")
    public String getBookById(@PathVariable int id, Model model) {
        Book book = bookService.findById(id);
        if (book != null) {
            model.addAttribute("book", book);
            return "/details";
        } else {
            return "redirect:/books";
        }
    }

    @GetMapping
    public String getAllBooks(Model model, Principal principal) {

        String username = principal.getName();

        // Load the user by username
        User user = userService.findByUsername(username);

        // Add the user to the model
        model.addAttribute("user", user);
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "/list";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Book book = bookService.findById(id);

        if (book == null) {
            return "redirect:/books";
        }

        model.addAttribute("book", book);
        model.addAttribute("editMode", true);  // βάζω εδώ το editMode για να γινει trigger η λειτουργία στη σελίδα
        return "details";
    }


    @PostMapping("/{id}/edit")
    public String editBook(@PathVariable Integer id, @ModelAttribute Book book) {
        try {
            bookService.edit(id, book);
            System.out.println("Book updated successfully.");


            return "redirect:/books/" + id;
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }


}





