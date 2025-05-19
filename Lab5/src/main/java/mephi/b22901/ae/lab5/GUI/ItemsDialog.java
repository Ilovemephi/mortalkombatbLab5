

package mephi.b22901.ae.lab5.GUI;


import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ItemsDialog extends JDialog {

    private JList<String> itemsList;
    private DefaultListModel<String> listModel;
    private JButton useButton;

    // Хранилище предметов: имя -> количество
    private Map<String, Integer> itemQuantities;

    public ItemsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setTitle("Мешок предметов");
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setResizable(false);
        itemQuantities = new HashMap<>();
        initDefaultItems(); 
        initComponents();
    }

    private void initDefaultItems() {
        for (String itemName : allItems) {
            itemQuantities.put(itemName, 0); 
        }
    }

    private static final String[] allItems = {
        "Малое зелье лечения",
        "Большое зелье лечения",
        "Крест возрождения"
    };

    private void initComponents() {
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        
        JLabel titleLabel = new JLabel("Мешок предметов");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        
        listModel = new DefaultListModel<>();
        updateItemList(); 

        itemsList = new JList<>(listModel);
        itemsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(itemsList);

 
        useButton = new JButton("Использовать");
        useButton.addActionListener(e -> handleUseItem());


        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(useButton, BorderLayout.SOUTH);

        add(panel);
    }

    /**
     * Обновляет отображаемый список предметов на основе itemQuantities
     */
    private void updateItemList() {
        listModel.clear();
        for (String itemName : allItems) {
            int count = itemQuantities.getOrDefault(itemName, 0);
            listModel.addElement(itemName + ", " + count);
        }
    }

    private void handleUseItem() {
        int selectedIndex = itemsList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Пожалуйста, выберите предмет.");
            return;
        }

        String selectedItemName = allItems[selectedIndex];
        int count = itemQuantities.getOrDefault(selectedItemName, 0);

        if (count <= 0) {
            JOptionPane.showMessageDialog(this, "Этот предмет нельзя использовать — количество равно 0.");
            return;
        }

        // Уменьшаем количество предмета
        itemQuantities.put(selectedItemName, count - 1);
        updateItemList(); // Обновляем отображение
        JOptionPane.showMessageDialog(this, "Вы использовали: " + selectedItemName);
    }

    /**
     * Увеличивает количество указанного предмета
     *
     * @param itemName Название предмета
     * @param amount   Количество для добавления
     */
    public void addItem(String itemName, int amount) {
        if (itemQuantities.containsKey(itemName)) {
            itemQuantities.put(itemName, itemQuantities.get(itemName) + amount);
            updateItemList();
        } else {
            // Если предмет не входит в список по умолчанию
            JOptionPane.showMessageDialog(this, "Неизвестный предмет: " + itemName);
        }
    }

    /**
     * Устанавливает количество предмета напрямую
     */
    public void setItemCount(String itemName, int count) {
        if (itemQuantities.containsKey(itemName)) {
            itemQuantities.put(itemName, count);
            updateItemList();
        }
    }

    /**
     * Очищает все предметы
     */
    public void clearItems() {
        for (String itemName : allItems) {
            itemQuantities.put(itemName, 0);
        }
        updateItemList();
    }
}
