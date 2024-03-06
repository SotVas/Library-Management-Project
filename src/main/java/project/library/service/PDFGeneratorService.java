package project.library.service;
import project.library.entity.User;

import java.io.ByteArrayInputStream;

public interface PDFGeneratorService {
    ByteArrayInputStream generatePDF(User user);
}
