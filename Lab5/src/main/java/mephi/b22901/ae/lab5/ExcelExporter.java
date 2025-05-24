
package mephi.b22901.ae.lab5;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelExporter {

    /**
     * Экспортирует содержимое JTable в указанный Excel-файл (.xlsx)
     */
    public static void exportTable(JTable table, File fileToSave) throws IOException {
        TableModel model = table.getModel();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Таблица");


        Row headerRow = sheet.createRow(0);
        for (int col = 0; col < model.getColumnCount(); col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(model.getColumnName(col));
        }


        for (int row = 0; row < model.getRowCount(); row++) {
            Row excelRow = sheet.createRow(row + 1);
            for (int col = 0; col < model.getColumnCount(); col++) {
                Cell cell = excelRow.createCell(col);
                Object value = model.getValueAt(row, col);
                if (value != null)
                    cell.setCellValue(value.toString());
            }
        }

        try (FileOutputStream out = new FileOutputStream(fileToSave)) {
            workbook.write(out);
        }
        workbook.close();
    }
}
