package Util;

import Model.Fan;
import Model.Transaction;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExportUtil {

    // Helper method to get file path from file chooser
    public static String getExportFilePath(String dialogTitle, String defaultFileName, String extension) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(dialogTitle);
        fileChooser.setSelectedFile(new File(defaultFileName + extension));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            // Ensure file has correct extension
            String path = fileToSave.getAbsolutePath();
            if (!path.toLowerCase().endsWith(extension.toLowerCase())) {
                path += extension;
            }
            return path;
        }
        return null;
    }

  public static boolean exportTransactionsToPDF(List<Transaction> transactions, String filePath) {
    try {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Add title
        com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(
            com.itextpdf.text.Font.FontFamily.HELVETICA, 
            18, 
            com.itextpdf.text.Font.BOLD
        );
        Paragraph title = new Paragraph("Amavubi FanHub - Transactions Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Add summary
        com.itextpdf.text.Font summaryFont = new com.itextpdf.text.Font(
            com.itextpdf.text.Font.FontFamily.HELVETICA, 
            12, 
            com.itextpdf.text.Font.NORMAL
        );
        Paragraph summary = new Paragraph(
            "Total Transactions: " + transactions.size() + 
            " | Generated on: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), 
            summaryFont
        );
        summary.setAlignment(Element.ALIGN_CENTER);
        document.add(summary);

        document.add(Chunk.NEWLINE);

        // Create table with 8 columns
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1f, 2f, 1.5f, 3f, 1.5f, 2f, 1.5f, 2f}); // Set column widths

        // Add headers
        String[] headers = {"ID", "Fan Name", "Type", "Item/Match", "Amount", "Payment", "Status", "Date"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        // Format for amounts
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        
        // Add data
        for (Transaction t : transactions) {
            // ID
          PdfPCell idCell = new PdfPCell(new Phrase(
    String.valueOf(t.getTransactionId()) // Remove null check for primitive int
));
            idCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(idCell);
            
            // Fan Name
            table.addCell(t.getFanName() != null ? t.getFanName() : "N/A");
            
            // Type
            PdfPCell typeCell = new PdfPCell(new Phrase(
                t.getTransactionType() != null ? t.getTransactionType() : "N/A"
            ));
            typeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(typeCell);
            
            // Item/Match
            table.addCell(t.getTransactionDescription() != null ? t.getTransactionDescription() : "N/A");
            
            // Amount
            PdfPCell amountCell = new PdfPCell(new Phrase(
               // t.getAmount() != null ? currencyFormat.format(t.getAmount()) : "N/A"
            ));
            amountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(amountCell);
            
            // Payment Method
            PdfPCell paymentCell = new PdfPCell(new Phrase(
                t.getPaymentMethod() != null ? t.getPaymentMethod() : "N/A"
            ));
            paymentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(paymentCell);
            
            // Status
            PdfPCell statusCell = new PdfPCell(new Phrase(
                t.getStatus() != null ? t.getStatus() : "N/A"
            ));
            statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(statusCell);
            
            // Date
            PdfPCell dateCell = new PdfPCell(new Phrase(
                t.getTransactionDate() != null ? 
                    new SimpleDateFormat("yyyy-MM-dd HH:mm").format(t.getTransactionDate()) : 
                    "N/A"
            ));
            dateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(dateCell);
        }

        document.add(table);
        document.close();
        return true;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
    public static boolean exportFansToPDF(List<Fan> fans, String filePath) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Add title
          com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD);
            Paragraph title = new Paragraph("Amavubi FanHub - Fans Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Add summary
            com.itextpdf.text.Font summaryFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.NORMAL);
            Paragraph summary = new Paragraph("Total Registered Fans: " + fans.size() + 
                " | Generated on: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), summaryFont);
            summary.setAlignment(Element.ALIGN_CENTER);
            document.add(summary);

            document.add(Chunk.NEWLINE);

            // Create table
            PdfPTable table = new PdfPTable(7); // 7 columns
            table.setWidthPercentage(100);

            // Add headers
            String[] headers = {"ID", "National ID", "Name", "Email", "Phone", "Tier", "Role"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }

            // Add data
            for (Fan f : fans) {
                table.addCell(String.valueOf(f.getFanId()));
                table.addCell(f.getNationalId() != null ? f.getNationalId() : "N/A");
                table.addCell(f.getName() != null ? f.getName() : "N/A");
                table.addCell(f.getEmail() != null ? f.getEmail() : "N/A");
                table.addCell(f.getPhone() != null ? f.getPhone() : "N/A");
                table.addCell(f.getTier() != null ? f.getTier() : "N/A");
                table.addCell(f.getRole() != null ? f.getRole() : "N/A");
            }

            document.add(table);
            document.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean exportToExcel(List<?> data, String filePath, String type) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(type.equals("transactions") ? "Transactions" : "Fans");

            // Create header row
            Row headerRow = sheet.createRow(0);
            
            if (type.equals("transactions")) {
                List<Transaction> transactions = (List<Transaction>) data;
                String[] headers = {"ID", "Fan Name", "Type", "Item/Match", "Amount", "Payment", "Status", "Date"};
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                }

                // Add data
                int rowNum = 1;
                for (Transaction t : transactions) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(t.getTransactionId());
                    row.createCell(1).setCellValue(t.getFanName() != null ? t.getFanName() : "N/A");
                    row.createCell(2).setCellValue(t.getTransactionType() != null ? t.getTransactionType() : "N/A");
                    row.createCell(3).setCellValue(t.getTransactionDescription() != null ? t.getTransactionDescription() : "N/A");
                    row.createCell(4).setCellValue(t.getAmount());
                    row.createCell(5).setCellValue(t.getPaymentMethod() != null ? t.getPaymentMethod() : "N/A");
                    row.createCell(6).setCellValue(t.getStatus() != null ? t.getStatus() : "N/A");
                    row.createCell(7).setCellValue(t.getTransactionDate() != null ? t.getTransactionDate().toString() : "N/A");
                }
            } else {
                List<Fan> fans = (List<Fan>) data;
                String[] headers = {"ID", "National ID", "Name", "Email", "Phone", "Tier", "Role"};
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                }

                // Add data
                int rowNum = 1;
                for (Fan f : fans) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(f.getFanId());
                    row.createCell(1).setCellValue(f.getNationalId() != null ? f.getNationalId() : "N/A");
                    row.createCell(2).setCellValue(f.getName() != null ? f.getName() : "N/A");
                    row.createCell(3).setCellValue(f.getEmail() != null ? f.getEmail() : "N/A");
                    row.createCell(4).setCellValue(f.getPhone() != null ? f.getPhone() : "N/A");
                    row.createCell(5).setCellValue(f.getTier() != null ? f.getTier() : "N/A");
                    row.createCell(6).setCellValue(f.getRole() != null ? f.getRole() : "N/A");
                }
            }

            // Auto-size columns
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to file
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
            }
            workbook.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean exportToCSV(List<?> data, String filePath, String type) {
        try {
            StringBuilder sb = new StringBuilder();
            
            if (type.equals("transactions")) {
                List<Transaction> transactions = (List<Transaction>) data;
                // Header
                sb.append("ID,Fan Name,Type,Item/Match,Amount,Payment Method,Status,Date\n");
                
                // Data
                for (Transaction t : transactions) {
                    sb.append(t.getTransactionId()).append(",");
                    sb.append("\"").append(t.getFanName() != null ? t.getFanName() : "N/A").append("\",");
                    sb.append("\"").append(t.getTransactionType() != null ? t.getTransactionType() : "N/A").append("\",");
                    sb.append("\"").append(t.getTransactionDescription() != null ? t.getTransactionDescription() : "N/A").append("\",");
                    sb.append(t.getAmount()).append(",");
                    sb.append("\"").append(t.getPaymentMethod() != null ? t.getPaymentMethod() : "N/A").append("\",");
                    sb.append("\"").append(t.getStatus() != null ? t.getStatus() : "N/A").append("\",");
                    sb.append("\"").append(t.getTransactionDate() != null ? t.getTransactionDate() : "N/A").append("\"\n");
                }
            } else {
                List<Fan> fans = (List<Fan>) data;
                // Header
                sb.append("ID,National ID,Name,Email,Phone,Tier,Role\n");
                
                // Data
                for (Fan f : fans) {
                    sb.append(f.getFanId()).append(",");
                    sb.append("\"").append(f.getNationalId() != null ? f.getNationalId() : "N/A").append("\",");
                    sb.append("\"").append(f.getName() != null ? f.getName() : "N/A").append("\",");
                    sb.append("\"").append(f.getEmail() != null ? f.getEmail() : "N/A").append("\",");
                    sb.append("\"").append(f.getPhone() != null ? f.getPhone() : "N/A").append("\",");
                    sb.append("\"").append(f.getTier() != null ? f.getTier() : "N/A").append("\",");
                    sb.append("\"").append(f.getRole() != null ? f.getRole() : "N/A").append("\"\n");
                }
            }

            // Write to file
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                outputStream.write(sb.toString().getBytes());
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}