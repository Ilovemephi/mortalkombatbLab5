
package mephi.b22901.ae.lab5.battle;

import javax.swing.JOptionPane;
import mephi.b22901.ae.lab5.CharacterAction;
import mephi.b22901.ae.lab5.GUI.FightFrame;
import mephi.b22901.ae.lab5.GUI.ItemsDialog;
import mephi.b22901.ae.lab5.GUI.WinOrLoseDialog;
import mephi.b22901.ae.lab5.Human;
import mephi.b22901.ae.lab5.Player;
import mephi.b22901.ae.lab5.ShaoKahn;

public class BattleManager {
    
    private Human player;
    private Player currentEnemy;
    private int[] enemyBehavior; // шаблон действий врага
    private int behaviorIndex = -1;
    private boolean isStunned = false;

    private final CharacterAction characterAction;

    public BattleManager(Human player, CharacterAction action) {
        this.player = player;
        this.characterAction = action;
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
           
            frame.updatePlayerUI((Human) player);
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

            frame.updatePlayerUI((Human) player);
        } else {
            JOptionPane.showMessageDialog(dialog, "Нет доступных предметов");
        }
    }

    private void useReviveCross(Player player, ItemsDialog dialog, FightFrame frame) {
        int count = dialog.getItemCount("Крест возрождения");
        if (count > 0 && player.getHealth() <= 0) {
            player.setHealth((int) (player.getMaxHealth() * 0.8)); 
            dialog.addItem("Крест возрождения", -1);
            dialog.dispose();
            frame.updatePlayerUI((Human) player);
        } else if (count > 0) {
            JOptionPane.showMessageDialog(dialog, "Крест можно использовать только если здоровье <= 0");
        } else {
            JOptionPane.showMessageDialog(dialog, "Нет доступных предметов");
        }
    }
    
   

    /**
     * Начинает новый раунд с новым врагом.
     * Выбирает случайного противника и его поведение.
     *
     * @param frame GUI для обновления
     */
    public void startNewRound(FightFrame frame) {
        // Выбираем нового врага
        currentEnemy = characterAction.chooseEnemy();

        // Получаем его поведение
        enemyBehavior = characterAction.selectEnemyBehavior(currentEnemy);
        behaviorIndex = -1;

        // Сбрасываем оглушение
        isStunned = false;
        
        player.setNewHealth(player.getMaxHealth());
        currentEnemy.setNewHealth(currentEnemy.getMaxHealth());


        // Обновляем интерфейс
        frame.updatePlayerUI((Human) player);
        frame.updateEnemyUI(currentEnemy);
        
        checkAndHandleLevelUp(player, frame);
    }
    
    
    public void startSpecificEnemyRound(FightFrame frame, Player enemy) {
        this.currentEnemy = enemy;

        // Поведение для обычных врагов (для босса можно сделать особым, если нужно)
        enemyBehavior = characterAction.selectEnemyBehavior(currentEnemy);
        behaviorIndex = -1;
        isStunned = false;

        player.setNewHealth(player.getMaxHealth());
        currentEnemy.setNewHealth(currentEnemy.getMaxHealth());

        frame.updatePlayerUI(player);
        frame.updateEnemyUI(currentEnemy);

        checkAndHandleLevelUp(player, frame);
    }

    
    
    
    
    
    /**
     * Игрок выбирает атаковать.
     *
     * @param human игрок
     * @param frame окно игры
     */
    public void playerAttack(Human human, FightFrame frame, ItemsDialog itemsDialog) {
        if (isStunned) {
            frame.setTurnLabelText("Вы оглушены. Пропуск хода.");
            isStunned = false;
            return;
        }

        human.setAttack(1); // Атака

        chooseNextEnemyAction();
        currentEnemy.setAttack(enemyBehavior[behaviorIndex]);

        performTurn(human, currentEnemy, frame);

        //checkAndHandleLevelUp(human, frame);

        frame.updatePlayerUI(human);
        frame.updateEnemyUI(currentEnemy);
        checkForRoundEnd(human, currentEnemy, frame,  itemsDialog);
    }
    
    
    /**
     * Игрок выбирает защищаться.
     *
     * @param human игрок
     * @param frame окно игры
     */
    public void playerDefend(Human human, FightFrame frame, ItemsDialog itemsDialog) {
        if (isStunned) {
            frame.setTurnLabelText("Вы оглушены. Пропуск хода.");
            isStunned = false;
            return;
        }

        human.setAttack(0); // Защита

        chooseNextEnemyAction();
        currentEnemy.setAttack(enemyBehavior[behaviorIndex]);

        performTurn(human, currentEnemy, frame);

        //checkAndHandleLevelUp(human, frame);

        frame.updatePlayerUI(human);
        frame.updateEnemyUI(currentEnemy);
        checkForRoundEnd(human, currentEnemy, frame, itemsDialog);
    }
    
    
    
    private void performTurn(Player p1, Player p2, FightFrame frame) {
        int p1Action = p1.getAttack();
        int p2Action = p2.getAttack();

        String turnType = Integer.toString(p1Action) + Integer.toString(p2Action);

        switch (turnType) {
            case "10": // Игрок атакует, враг защищается
                handlePlayerAttackWhileEnemyDefends(p1, p2, frame);
                break;
            case "11": // Оба атакуют
                handleBothAttack(p1, p2, frame);
                break;
            case "00": // Оба защищаются
                handleBothDefend(p1, p2, frame);
                break;
            case "01": // Защита → Атака
                handlePlayerDefendWhileEnemyAttacks(p1, p2, frame);
                break;
            case "-10": // Оглушённый игрок, враг защищается
                handleStunnedPlayerWhileEnemyDefends(p1, p2, frame);
                break;
            case "-11": // Оглушённый игрок, враг атакует
                handleStunnedPlayerWhileEnemyAttacks(p1, p2, frame);
                break;
            default:
                frame.setTurnLabelText("Неизвестное действие");
                break;
        }
    }
    
    /**
     * Обрабатывает ситуацию: игрок атакует, враг защищается.
     */
    private void handlePlayerAttackWhileEnemyDefends(Player p1, Player p2, FightFrame frame) {
        double v = Math.random();

        if (p2 instanceof ShaoKahn && v < 0.15) {
            p2.setHealth(-(int)(p1.getDamage() * 0.5));
            frame.setTurnLabelText("Блок игрока пробит!");
        } else {
            p2.setHealth(-p1.getDamage());
            frame.setTurnLabelText(p2.getName() + " контратакует");
        }
    }
    
    
    /**
     * Оба атакуют — наносится урон только первому
     */
    private void handleBothAttack(Player p1, Player p2, FightFrame frame) {
        p2.setHealth(-p1.getDamage());
        p1.setHealth(-p2.getDamage());
        frame.setTurnLabelText(p1.getName() + " и " + p2.getName() + " атакуют одновременно");
    }
    
    /**
     * Оба защищаются — шанс оглушения
     */
    private void handleBothDefend(Player p1, Player p2, FightFrame frame) {
        double chanceToStun = Math.random();
        if (chanceToStun <= 0.5) {
            isStunned = true;
            frame.setTurnLabelText("Оба защищаются. Вы оглушены.");
        } else {
            frame.setTurnLabelText("Оба защищаются");
        }
    }
    
    /**
     * Игрок защищается, враг атакует — игрок получает 50% урона
     */
    private void handlePlayerDefendWhileEnemyAttacks(Player p1, Player p2, FightFrame frame) {
        p1.setHealth(-(int)(p2.getDamage() * 0.5));
        frame.setTurnLabelText(p2.getName() + " атакует");
    }

    /**
     * Игрок оглушён, враг защищается — ничего не происходит
     */
    private void handleStunnedPlayerWhileEnemyDefends(Player p1, Player p2, FightFrame frame) {
        frame.setTurnLabelText(p1.getName() + " был оглушён");
    }
    
    
    /**
     * Игрок оглушён, враг атакует — игрок получает полный урон
     */
    private void handleStunnedPlayerWhileEnemyAttacks(Player p1, Player p2, FightFrame frame) {
        p1.setHealth(-p2.getDamage());
        frame.setTurnLabelText(p1.getName() + " был оглушён, " + p2.getName() + " атакует");
    }

    /**
     * Получает следующее действие из поведения врага
     */
    private void chooseNextEnemyAction() {
        if (behaviorIndex < enemyBehavior.length - 1) {
            behaviorIndex++;
        } else {
            behaviorIndex = 0;
        }
    }
    
    
    /**
     * Проверяет, повысился ли уровень игрока, и предлагает выбрать усиление
     */
//    private void checkAndHandleLevelUp(Human human, FightFrame frame) {
//        for (int i = 0; i < characterAction.getExperienceForNextLevel().length; i++) {
//            if (human.getExperience() >= characterAction.getExperienceForNextLevel()[i]) {
//                if (human.isLevelUpChoiceEnabled()) {
//                    showLevelUpChoice(human, frame);
//                    human.setLevel();
//
//                }
//                break;
//            }
//        }
//    }
    
    
    /**
    * Проверяет, повысился ли уровень игрока.
    * Если да — показывает окно выбора усиления.
    */
   public void checkAndHandleLevelUp(Human human, FightFrame frame) {
       int currentExp = human.getExperience();
       int currentLevel = human.getLevel();
       int[] expTable = characterAction.getExperienceForNextLevel();

       // Проходим по таблице порогов опыта
       for (int i = 0; i < expTable.length; i++) {
           if (currentExp >= expTable[i] && currentLevel <= i) {
               if (!human.isLevelUpChoiceEnabled()) {
                   // Сбрасываем флаг для нового уровня
                   human.setLevelUpChoiceEnabled(true);
               }

               if (human.isLevelUpChoiceEnabled()) {
                   showLevelUpChoice(human, frame);
                   human.setLevel(i + 1); 
                   human.setNextExperience(expTable[i + 1]); 
                   human.setLevelUpChoiceEnabled(false); 
                   break;
               }
           }
       }
   }
    
    /**
     * Диалоговое окно с выбором усиления при повышении уровня
     */
    private void showLevelUpChoice(Human human, FightFrame frame) {
        String[] options = {"Увеличить урон", "Увеличить здоровье"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "Выберите, что улучшить:",
                "Повышение уровня",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            increasePlayerDamage(human);
        } else if (choice == 1) {
            increasePlayerHealth(human);
        }
        
        human.restoreFullHealth();

        // Усиливаем всех обычных врагов пропорционально уровню игрока
        characterAction.adjustEnemyStatsToPlayer(human, characterAction.getEnemies());
        currentEnemy.restoreFullHealth();

        // Блокируем повторный выбор на этом уровне
        human.setLevelUpChoiceEnabled(false);

        // Обновляем интерфейс
        frame.updatePlayerUI(human);
        frame.updateEnemyUI(currentEnemy);
    }
    
    
    private void increasePlayerDamage(Human human) {
        human.setDamage((int) (human.getDamage() * 1.1)); // +10%
    }

    private void increasePlayerHealth(Human human) {
        human.setMaxHealth((int) (human.getMaxHealth() * 1.2)); // +20%
    }
    
    
    /**
     * Проверяет, закончился ли раунд
     */
//    private void checkForRoundEnd(Human human, Player enemy, FightFrame frame, ItemsDialog itemsDialog) { // Добавил
//        
//        if (human.getHealth() <= 0 || enemy.getHealth() <= 0) {
//            endCombatRound(human, enemy, frame, itemsDialog); // Добавил
//        }
//    }
//    
    
    
    
    
    private void checkForRoundEnd(Human human, Player enemy, FightFrame frame, ItemsDialog itemsDialog) {
        // Сначала проверяем крест возрождения
        if (!checkForAutoRevive(human, itemsDialog, frame)) {
            endCombatRound(human, enemy, frame, itemsDialog);
            return;
        }

        // После возможного восстановления проверяем конец раунда
        if (enemy.getHealth() <= 0) {
            endCombatRound(human, enemy, frame, itemsDialog);
        }
    }
    
    
    
    
    
    
    /**
     * Завершает раунд и начисляет очки, опыт, выпадение предметов
     */
    private void endCombatRound(Human human, Player enemy, FightFrame frame, ItemsDialog itemsDialog) { // добавил
    // Обновляем текст в интерфейсе
    if (human.getHealth() > 0) {
        frame.setTurnLabelText("Вы победили!");
        

        characterAction.grantExperienceAndPoints(human, characterAction.getEnemies());


        characterAction.dropItemsOnVictory(human, itemsDialog); // добавил


        showResultDialog(frame, "win");


        //startNewRound(frame);
        frame.onEnemyDefeated();
    } else {
        frame.setTurnLabelText("Вы проиграли " + enemy.getName());
        showResultDialog(frame, "lose");
        frame.onEnemyDefeated();
    }
}
    
    
    
        private void showResultDialog(FightFrame parentFrame, String resultType) {
        WinOrLoseDialog dialog = new WinOrLoseDialog(parentFrame, true, resultType);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }
        
        
        
        
    /**
 * Проверяет, можно ли использовать крест возрождения автоматически.
 * Если можно — восстанавливает часть здоровья, иначе — возвращает false
 *
 * @param player игрок
 * @param itemsDialog мешок предметов
 * @param frame GUI для обновления
 * @return true, если игрок жив, иначе false
 */
public boolean checkForAutoRevive(Human player, ItemsDialog itemsDialog, FightFrame frame) {
    if (player.getHealth() <= 0) {
        int crossCount = itemsDialog.getItemCount("Крест возрождения");

        if (crossCount > 0) {
            // Используем крест
            useReviveCross(player, itemsDialog, frame);

            // Обновляем уровень и параметры врага, если был повышение уровня
            checkAndHandleLevelUp(player, frame);

            // Сообщаем о восстановлении
            frame.setTurnLabelText("Вы были восстановлены крестом!");

            // Возвращаем true → бой продолжается
            return true;
        } else {
            frame.setTurnLabelText("У вас нет креста возрождения");
            return false;
        }
    }

    return true; // бой продолжается
}



    
}
