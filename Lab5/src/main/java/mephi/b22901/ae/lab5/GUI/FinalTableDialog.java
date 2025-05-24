
package mephi.b22901.ae.lab5.GUI;



import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import mephi.b22901.ae.lab5.ExcelExporter;

public class FinalTableDialog extends JDialog {

    private JTable recordsTable;
    private DefaultTableModel tableModel;
    private JButton closeButton;

    public FinalTableDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setTitle("Таблица рекордов");
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        // Заголовки таблицы
        String[] columnNames = {"Имя", "Очки"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            // Запрещаем редактирование ячеек
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        recordsTable = new JTable(tableModel);
        recordsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(recordsTable);

       
        closeButton = new JButton("Закрыть");
        closeButton.addActionListener(e -> dispose());

        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * Добавляет новую запись в таблицу
     *
     * @param name  имя игрока
     * @param score количество очков
     */
    public void addRecord(String name, int score) {
        tableModel.addRow(new Object[]{name, score});
    }
    
    
    
    public void exportToExcel(Component parent) {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Сохранить как Excel файл");
    fileChooser.setSelectedFile(new java.io.File("Рекорды.xlsx"));
    int userSelection = fileChooser.showSaveDialog(parent);

    if (userSelection == JFileChooser.APPROVE_OPTION) {
        java.io.File fileToSave = fileChooser.getSelectedFile();
        try {
            ExcelExporter.exportTable(recordsTable, fileToSave);
            JOptionPane.showMessageDialog(parent,
                "Данные успешно экспортированы в\n" + fileToSave.getAbsolutePath());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parent,
                "Ошибка экспорта: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

    /**
     * Очищает таблицу (например, при перезапуске игры)
     */
    public void clearRecords() {
        tableModel.setRowCount(0);
    }
}
