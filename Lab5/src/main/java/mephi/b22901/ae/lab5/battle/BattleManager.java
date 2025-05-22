
package mephi.b22901.ae.lab5.battle;

import javax.swing.JOptionPane;
import mephi.b22901.ae.lab5.GUI.FightFrame;
import mephi.b22901.ae.lab5.GUI.ItemsDialog;
import mephi.b22901.ae.lab5.Player;

public class BattleManager {
    private Player player;
    private Player enemy;
    
    
    public BattleManager(Player player) {
        this.player = player;
        
    }
    
    /**
    * Обрабатывает использование выбранного предмета из мешка.
    * Поддерживает: малое зелье, большое зелье, крест возрождения.
    *
    * @param player игрок, применяющий предмет
    * @param itemsDialog окно мешка предметов
    */
    public void useSelectedItemFromBag(Player player, ItemsDialog itemsDialog, FightFrame frame) {
        int selectedIndex = itemsDialog.getSelectedIndex();

        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(null, "Пожалуйста, выберите предмет.");
            return;
        }

        String selectedItemName = ItemsDialog.allItems[selectedIndex];

        if ("Малое зелье лечения".equals(selectedItemName)) {
            useSmallPotion(player, itemsDialog, frame);
        } else if ("Большое зелье лечения".equals(selectedItemName)) {
            useBigPotion(player, itemsDialog, frame);
        } else if ("Крест возрождения".equals(selectedItemName)) {
            useReviveCross(player, itemsDialog, frame);
        }
    }
   
   private void useSmallPotion(Player player, ItemsDialog dialog, FightFrame frame) {
        int count = dialog.getItemCount("Малое зелье лечения");
        if (count > 0) {
            player.setHealth((int) (player.getMaxHealth() * 0.25));
            dialog.addItem("Малое зелье лечения", -1); 
            dialog.dispose(); // закрываем окно

            // Обновляем интерфейс игрока
            frame.updatePlayerUI(player);
        } else {
            JOptionPane.showMessageDialog(dialog, "Нет доступных предметов");
        }
    }

    private void useBigPotion(Player player, ItemsDialog dialog, FightFrame frame) {
        int count = dialog.getItemCount("Большое зелье лечения");
        if (count > 0) {
            player.setHealth((int) (player.getMaxHealth() * 0.5));
            dialog.addItem("Большое зелье лечения", -1);
            dialog.dispose();

            frame.updatePlayerUI(player);
        } else {
            JOptionPane.showMessageDialog(dialog, "Нет доступных предметов");
        }
    }

    private void useReviveCross(Player player, ItemsDialog dialog, FightFrame frame) {
        int count = dialog.getItemCount("Крест возрождения");
        if (count > 0 && player.getHealth() <= 0) {
            player.setHealth((int) (player.getMaxHealth() * 0.05)); 
            dialog.addItem("Крест возрождения", -1);
            dialog.dispose();

            frame.updatePlayerUI(player);
        } else if (count > 0) {
            JOptionPane.showMessageDialog(dialog, "Крест можно использовать только если здоровье <= 0");
        } else {
            JOptionPane.showMessageDialog(dialog, "Нет доступных предметов");
        }
    }

   

    
}
