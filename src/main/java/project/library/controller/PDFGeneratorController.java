package project.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.library.entity.User;
import project.library.service.PDFGeneratorService;
import project.library.service.UserService;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Controller
public class PDFGeneratorController {

    private final UserService userService;
    private final PDFGeneratorService pdfGeneratorService;

    @Autowired
    public PDFGeneratorController(UserService userService, PDFGeneratorService pdfGeneratorService) {
        this.userService = userService;
        this.pdfGeneratorService = pdfGeneratorService;
    }

    @GetMapping("/export/user/{id}")
    public void generatePDF(HttpServletResponse response, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Optional<User> userOptional = Optional.ofNullable(userService.findById(id));

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                response.setContentType("application/pdf");
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String currentDate = dateFormat.format(new Date());
                String headerKey = "Content-Disposition";
                String headerValue = "attachment; filename=Library-card " + currentDate + ".pdf";
                response.setHeader(headerKey, headerValue);

                // Get the generated PDF content as a byte array
                ByteArrayInputStream pdfBytes = pdfGeneratorService.generatePDF(user);

                // Write the byte array to the response output stream
                byte[] buffer = new byte[1024];
                int bytesRead;
                try (OutputStream os = response.getOutputStream()) {
                    while ((bytesRead = pdfBytes.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                }
                redirectAttributes.addFlashAttribute("successMessage", "Library Card successfully generated and exported!");
            } else {
                // Handle user not found
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (IOException e) {
            // Handle IO exception
            e.printStackTrace();
        }
    }
}
