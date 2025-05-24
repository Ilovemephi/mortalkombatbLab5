
package mephi.b22901.ae.lab5.battle;

import javax.swing.JOptionPane;
import mephi.b22901.ae.lab5.ActionCodes;
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
    
    private int bossTotalDamageTaken = 0;

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
        
        if (enemy instanceof ShaoKahn){
            bossTotalDamageTaken = 0;

        }

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
    
    
    
    
    
    public void playerWeaken(Human human, FightFrame frame, ItemsDialog itemsDialog) {
        if (isStunned) {
            frame.setTurnLabelText("Вы оглушены. Пропуск хода.");
            isStunned = false;
            return;
        }

        human.setAttack(ActionCodes.WEAKEN); // 3 — ослабление

        chooseNextEnemyAction();

        currentEnemy.setAttack(enemyBehavior[behaviorIndex]);

        performTurn(human, currentEnemy, frame);

        frame.updatePlayerUI(human);
        frame.updateEnemyUI(currentEnemy);

        checkForRoundEnd(human, currentEnemy, frame, itemsDialog);
    }
    
    
    
    
    private void performTurn(Player p1, Player p2, FightFrame frame) {
        int p1Action = p1.getAttack();
        int p2Action = p2.getAttack();
        
        if (p2 instanceof ShaoKahn && p2Action == 2) {
            handleBossRegeneration(p1, (ShaoKahn)p2, p1Action, frame);
            return;
        }


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
            case "30": // Игрок ослабляет, враг защищается
                handleWeakenWhileEnemyDefends((Human) p1, p2, frame);
                break;
            case "31": // Игрок ослабляет, враг атакует
                handleWeakenFailOnAttackingEnemy((Human) p1, p2, frame);
                break;
            case "03": // Враг (маг) ослабляет игрока, игрок защищается
                handleEnemyWeakenWhilePlayerDefends(p1, (Human) p2, frame);
                break;
            case "13": // Враг (маг) ослабляет игрока, игрок атакует
                handleEnemyWeakenFailOnAttackingPlayer(p1, (Human) p2, frame);
                break;
            default:
                frame.setTurnLabelText("Неизвестное действие");
                break;
        }
        
        p1.decrementWeakenTurn();
        p1.decrementBuffTurn();
        p2.decrementWeakenTurn();
        p2.decrementBuffTurn();
    }
    
    
    private void handleBossRegeneration(Player player, ShaoKahn boss, int playerAction, FightFrame frame) {
    if (playerAction == 0) { // Защита
        int heal = (int)(bossTotalDamageTaken * 0.5);
        boss.setHealth(heal);
        frame.setTurnLabelText("Босс восстанавливает " + heal + " здоровья!");
        frame.updateEnemyUI(boss);
    } else { // Атака
        int attackValue = player.getModifiedDamage() * 2;                // Используем дебаффы/баффы игрока и удваиваем результат
        int finalDamage = boss.receiveModifiedDamage(attackValue);        // Учтём уязвимость босса к урону (если активен дебафф)
        boss.setHealth(boss.getHealth() - finalDamage);
        bossTotalDamageTaken += finalDamage;
        frame.setTurnLabelText("Вы прервали регенерацию босса! Босс получил двойной урон: " + finalDamage);
        frame.updateEnemyUI(boss);
    }

}
    
    /**
     * Обрабатывает ситуацию: игрок атакует, враг защищается.
     */
    private void handlePlayerAttackWhileEnemyDefends(Player p1, Player p2, FightFrame frame) {
        double v = Math.random();
        if (p2 instanceof ShaoKahn && v < 0.15) {
            // атака в блок босса - только 50% урона
            int rawDamage = (int)(p1.getModifiedDamage() * 0.5);          // урон с учетом дебаффов (потом только 50% от этого)
            int finalDamage = p2.receiveModifiedDamage(rawDamage);        // если на боссе дебафф - ещё +25%
            p2.setHealth(-finalDamage);
            bossTotalDamageTaken += finalDamage;
            frame.setTurnLabelText("Блок игрока пробит!");
        } else {
            int rawDamage = p1.getModifiedDamage();                       // урон с учётом всех эффектов
            int finalDamage = p2.receiveModifiedDamage(rawDamage);        // учитываем возможную уязвимость к урону
            p2.setHealth(-finalDamage);
            frame.setTurnLabelText(p2.getName() + " контратакует");
        }
    }
    
    
    /**
    * Оба атакуют — наносится урон обоим
    */
   private void handleBothAttack(Player p1, Player p2, FightFrame frame) {
       // Урон от первого к второму (с учётом всех эффектов)
       int damageToEnemyRaw = p1.getModifiedDamage();
       int damageToEnemyFinal = p2.receiveModifiedDamage(damageToEnemyRaw);

       // Урон от второго к первому (с учётом всех эффектов)
       int damageToPlayerRaw = p2.getModifiedDamage();
       int damageToPlayerFinal = p1.receiveModifiedDamage(damageToPlayerRaw);

       p2.setHealth(-damageToEnemyFinal);
       p1.setHealth(-damageToPlayerFinal);

       frame.setTurnLabelText(p1.getName() + " и " + p2.getName() + " атакуют одновременно");

       if (p2 instanceof ShaoKahn) {
           bossTotalDamageTaken += damageToEnemyFinal;
       }
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
    * Игрок защищается, враг атакует — игрок получает 50% урона (с учётом баффов/дебаффов)
    */
   private void handlePlayerDefendWhileEnemyAttacks(Player p1, Player p2, FightFrame frame) {
       int attackValue = (int)(p2.getModifiedDamage() * 0.5);           // урон врага с учётом ослаблений, потом делим пополам
       int finalDamage = p1.receiveModifiedDamage(attackValue);         // если на игроке дебафф — урон дополнительно +25%
       p1.setHealth(-finalDamage);
       frame.setTurnLabelText(p2.getName() + " атакует");
   }

    /**
     * Игрок оглушён, враг защищается — ничего не происходит
     */
    private void handleStunnedPlayerWhileEnemyDefends(Player p1, Player p2, FightFrame frame) {
        frame.setTurnLabelText(p1.getName() + " был оглушён");
    }
    
    
    /**
    * Игрок оглушён, враг атакует — игрок получает полный урон (с учётом всех эффектов)
    */
   private void handleStunnedPlayerWhileEnemyAttacks(Player p1, Player p2, FightFrame frame) {
       int attackValue = p2.getModifiedDamage();            // урон с учетом эффектов врага
       int finalDamage = p1.receiveModifiedDamage(attackValue); // урон с учетом уязвимостей игрока
       p1.setHealth(-finalDamage);
       frame.setTurnLabelText(p1.getName() + " был оглушён, " + p2.getName() + " атакует");
   }
    
    
    
    // 1. Игрок ослабляет, враг защищается
    private void handleWeakenWhileEnemyDefends(Human player, Player enemy, FightFrame frame) {
        double chance = Math.random();
        int nTurns = Math.max(1, player.getLevel());
        if (chance < 0.75) {
            // Оба получают дебафф на n ходов
            enemy.applyWeakenDebuff(nTurns);   // теперь по врагу урон +25%
            player.applyWeakenDebuff(nTurns);  // у игрока урон -50%
            frame.setTurnLabelText("Успех! " + enemy.getName() + " ослаблен (" + nTurns + " ходов), но вы сами временно атакуете слабее.");
        } else {
            frame.setTurnLabelText("Промах: противник успешно защищается, никто не ослаблен.");
        }
    }
    
    
    
    // 2. Игрок ослабляет, враг атакует (ослабление не удалось, игрок получает бафф урона на 1 ход)
    private void handleWeakenFailOnAttackingEnemy(Human player, Player enemy, FightFrame frame) {
        player.applyBuffDamage(0.15, 1); // +15% к урону (1 ход)
        frame.setTurnLabelText(enemy.getName() + " перебивает вашу попытку ослабить! Ваша следующая атака усилена на 15%.");
    }
    
    
    
    // 3. Враг ослабляет игрока, игрок защищается
    private void handleEnemyWeakenWhilePlayerDefends(Player enemy, Human player, FightFrame frame) {
        double chance = Math.random();
        int nTurns = Math.max(1, enemy.getLevel());
        if (chance < 0.75) {
            player.applyWeakenDebuff(nTurns);  // теперь по игроку урон +25%
            enemy.applyWeakenDebuff(nTurns);   // у врага урон -50%
            frame.setTurnLabelText("Вас ослабили на " + nTurns + " ходов! И противник теперь атакует слабее.");
        } else {
            frame.setTurnLabelText("Соперник не смог вас ослабить — вы хорошо защищаетесь.");
        }
    }

    
    // 4. Враг ослабляет, игрок атакует (ослабление не удалось, враг получает бафф урона на 1 ход)
    private void handleEnemyWeakenFailOnAttackingPlayer(Player enemy, Human player, FightFrame frame) {
        enemy.applyBuffDamage(0.15, 1); // +15% к урону (1 ход)
        frame.setTurnLabelText("Вы сорвали ослабление врага, он разозлился! Его следующая атака усилена на 15%.");
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
