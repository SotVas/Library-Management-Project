package project.library.service.serviceImpl;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.stereotype.Controller;
import project.library.entity.User;
import project.library.service.PDFGeneratorService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.itextpdf.layout.properties.TextAlignment.LEFT;

@Controller
public class PDFGeneratorServiceImpl implements PDFGeneratorService {

    @Override
    public ByteArrayInputStream generatePDF(User user) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (PdfWriter writer = new PdfWriter(outputStream);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf, PageSize.A7)) {

            document.setMargins(20, 20, 20, 20);
            document.getPdfDocument().setDefaultPageSize(PageSize.A7.rotate());

            // Add user details to the left
            Paragraph userDetails = new Paragraph()
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.LEFT)
                    .add("User Details\n")
                    .add("ID: " + user.getId() + "\n")
                    .add("Name: " + user.getFirstname() + " " + user.getLastname() + "\n")
                    .add("Username: " + user.getUsername());
            String barcodeImagePath = "classpath:static/img/qr.png";
            Image barcodeImage = new Image(ImageDataFactory.create(barcodeImagePath));
            barcodeImage.setHorizontalAlignment(HorizontalAlignment.RIGHT);
            float scale = 0.108f; //
            barcodeImage.scale(scale,scale);
            document.add(barcodeImage);
            document.add(userDetails);

            // Add barcode image to the right


        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}