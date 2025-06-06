

package mephi.b22901.ae.lab5.GUI;


import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import mephi.b22901.ae.lab5.ItemUsedListener;
import mephi.b22901.ae.lab5.Player;

public class ItemsDialog extends JDialog {
    
    private ItemUsedListener itemUsedListener;

    private JList<String> itemsList;
    private DefaultListModel<String> listModel;
    private JButton useButton;
    

    // Хранилище предметов: имя -> количество
    private Map<String, Integer> itemQuantities;
    
    public void setItemUsedListener(ItemUsedListener listener) {
    this.itemUsedListener = listener;
}

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

    public static final String[] allItems = {
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
        useButton.addActionListener(e -> {
            int selectedIndex = itemsList.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(this, "Пожалуйста, выберите предмет.");
                return;
            }

            String selectedItemName = allItems[selectedIndex];

            if (itemUsedListener != null) {
                itemUsedListener.onItemUsed(selectedItemName);
            }
        });


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
            if (count > 0) {
                listModel.addElement(itemName + ", " + count + " шт.");
            } else {
                listModel.addElement(itemName + ", 0 шт.");
            }
        }
    }

   

    /**
     * Увеличивает количество указанного предмета
     *
     * @param itemName Название предмета
     * @param amount   Количество для добавления
     */
    /**
 * Добавляет указанный предмет в мешок
 */
public void addItem(String itemName, int amount) {
    if (!itemQuantities.containsKey(itemName)) {
        JOptionPane.showMessageDialog(this, "Предмет " + itemName + " не существует");
        return;
    }

    int current = itemQuantities.get(itemName);
    int newCount = current + amount;

    if (newCount >= 0 && newCount <= 99) {
        itemQuantities.put(itemName, newCount);
        updateItemList(); // обновляем список в JList
    } else {
            JOptionPane.showMessageDialog(this, "Невозможно изменить количество предмета: " + itemName);
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
    
    
    /**
    * Возвращает индекс выбранного элемента
    */
   public int getSelectedIndex() {
       return itemsList.getSelectedIndex();
   }

   /**
    * Получает количество указанного предмета
    */
   public int getItemCount(String itemName) {
       return itemQuantities.getOrDefault(itemName, 0);
   }
   
   
   
   
    
}
