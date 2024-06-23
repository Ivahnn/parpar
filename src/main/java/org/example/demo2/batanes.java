package org.example.demo2;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class batanes implements Initializable {

    @FXML
    private ComboBox<String> hotelComboBox;

    @FXML
    private ComboBox<String> topattractionComboBox;

    @FXML
    private ComboBox<String> activitiesComboBox;

    @FXML
    private ComboBox<String> breakfastComboBox;

    @FXML
    private ComboBox<String> lunchComboBox;

    @FXML
    private ComboBox<String> dinnerComboBox;

    @FXML
    private TextField usernameField;

    @FXML
    private DatePicker durationField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Add items to the hotel ComboBox
        hotelComboBox.getItems().addAll("Fundacion Pacita Batanes Nature Lodge", "Batanes Resort Hotel", "Amboy Hometel");

        // Add items to the top attraction ComboBox
        topattractionComboBox.getItems().addAll("Vayang Rolling Hills", "Basco Lighthouse", "Nakabuang Arch");

        activitiesComboBox.getItems().addAll("Biking around Basco", "Hiking to Mount Iraya", "Island Hopping to Sabtang Island");

        breakfastComboBox.getItems().addAll("Ivatan Platter", "Daing na Dibang", "Tapa with Fried Rice");

        lunchComboBox.getItems().addAll("Uvud Balls", "Lunis (Ivatan Adobo)", "Coconut Crab");

        dinnerComboBox.getItems().addAll("Grilled Flying Fish", "Yellow Rice with Local Herbs", "Beef Stew with Vegetables");
    }

    @FXML
    private void handlePrintPdfButton() {
        String selectedHotel = hotelComboBox.getValue();
        String selectedAttraction = topattractionComboBox.getValue();
        String selectedActivity = activitiesComboBox.getValue();
        String selectedBreakfast = breakfastComboBox.getValue();
        String selectedLunch = lunchComboBox.getValue();
        String selectedDinner = dinnerComboBox.getValue();
        String username = usernameField.getText();
        String duration = (durationField.getValue() != null) ? durationField.getValue().toString() : "N/A";

        try {
            String pdfPath = "output.pdf";
            PdfWriter writer = new PdfWriter(pdfPath);
            com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            Document document = new Document(pdfDoc);

            document.add(new Paragraph("Travel Plan"));
            document.add(new Paragraph("Traveler's Name: " + username));
            document.add(new Paragraph("Duration: " + duration));
            document.add(new Paragraph("Hotel: " + selectedHotel));
            document.add(new Paragraph("Top Attraction: " + selectedAttraction));
            document.add(new Paragraph("Activities: " + selectedActivity));
            document.add(new Paragraph("Breakfast: " + selectedBreakfast));
            document.add(new Paragraph("Lunch: " + selectedLunch));
            document.add(new Paragraph("Dinner: " + selectedDinner));

            document.close();
            System.out.println("PDF created at: " + pdfPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
