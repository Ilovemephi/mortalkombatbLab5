
package mephi.b22901.ae.lab5;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import mephi.b22901.ae.lab5.GUI.ItemsDialog;


public class CharacterAction {

    /**
    * Количество опыта, необходимое для перехода на следующий уровень.
    */
   private final int experienceForNextLevel[] = {40, 90, 180, 260, 410, 1000};

   /**
    * Шаблоны поведения противников.
    * 1 — атака, 0 — защита
    */
   private final int[][] enemyBehaviorPatterns = {
       {1, 0},           //  0: Attack → Defend
       {1, 1, 0},        //  1: Attack → Attack → Defend
       {0, 1, 0},        //  2: Defend → Attack → Defend
       {1, 1, 1, 1}      //  3: All attacks
   };

   /**
    * Список доступных противников (типы).
    */
   private Player[] enemies = new Player[6];

   /**
    * Текущий противник в бою.
    */
   private Player currentEnemy = null;
   
   
    /**
     * Инициализируем врагов которых создает фабрика 
     * Последние два это босы
     */
    public void initializeEnemies() {
    enemies[0] = EnemyFactory.createEnemy(EnemyType.BARAKA, 1);     

    enemies[1] = EnemyFactory.createEnemy(EnemyType.SUBZERO, 1);    

    enemies[2] = EnemyFactory.createEnemy(EnemyType.LIUKANG, 1);   

    enemies[3] = EnemyFactory.createEnemy(EnemyType.SONYABLADE, 1); 

    enemies[4] = EnemyFactory.createEnemy(EnemyType.SHAOKAHN, 3); 
                          
    enemies[5] = EnemyFactory.createEnemy(EnemyType.SHAOKAHN, 5);  
                                 
}




    public Player[] getEnemyes() {
        return this.enemies;
    }
    

    public Player ChooseEnemy(JLabel label, JLabel label2, JLabel text, JLabel label3) {
        int i = (int) (Math.random() * 4);
        ImageIcon icon1 = null;
        switch (i) {
            case 0:
                currentEnemy = enemies[0];
                icon1 = new ImageIcon("C:\\Users\\Мария\\Desktop\\Baraka.jpg");
                label2.setText("Baraka (танк)");
                break;
            case 1:
                currentEnemy = enemies[1];
                icon1 = new ImageIcon("C:\\Users\\Мария\\Desktop\\Sub-Zero.jpg");
                label2.setText("Sub-Zero (маг)");
                break;
            case 2:
                currentEnemy = enemies[2];
                icon1 = new ImageIcon("C:\\Users\\Мария\\Desktop\\Liu Kang.jpg");
                label2.setText("Liu Kang (боец)");
                break;
            case 3:
                currentEnemy = enemies[3];
                icon1 = new ImageIcon("C:\\Users\\Мария\\Desktop\\Sonya Blade.jpg");
                label2.setText("Sonya Blade (солдат)");
                break;
        }
        label.setIcon(icon1);
        text.setText(Integer.toString(currentEnemy.getDamage()));
        label3.setText(Integer.toString(currentEnemy.getHealth()) + "/" + Integer.toString(currentEnemy.getMaxHealth()));
        return currentEnemy;
    }

    public Player ChooseBoss(JLabel label, JLabel label2, JLabel text, JLabel label3, int i) {
        ImageIcon icon1 = null;
        icon1 = new ImageIcon("C:\\Users\\Мария\\Desktop\\Shao Kahn.png");
        label2.setText("Shao Kahn (босс)");
        switch (i) {
            case 2:
                currentEnemy = enemies[4];
                break;
            case 4:
                currentEnemy = enemies[5];
                break;
        }
        label.setIcon(icon1);
        text.setText(Integer.toString(currentEnemy.getDamage()));
        label3.setText(Integer.toString(currentEnemy.getHealth()) + "/" + Integer.toString(currentEnemy.getMaxHealth()));
        return currentEnemy;
    }

    
    /** 
     * Генерируем поведение противника
     * @param pattern1Chance процент вероятности первого типа поведения 
     * @param pattern2Chance процент вероятности второго типа поведения
     * @param pattern3Chance процент вероятности третьего типа поведения
     * @param pattern4Chance процент вероятности четвертого типа поведения
     * @param randomValue случайное число в диапазоне [0.0, 1.0)
     * @return выбранный шаблон поведения как массив {1, 0} и т.п.
     */
    public int[] generateEnemyBehavior(int pattern1Chance, int pattern2Chance, int pattern3Chance, int pattern4Chance, double randomValue) {
        int totalChance = pattern1Chance + pattern2Chance + pattern3Chance + pattern4Chance;
        if (totalChance != 100) {
            throw new IllegalArgumentException("Сумма вероятностей должна быть равна 100%");
        }

        double cumulative = 0; // накполенная сумма вероятностей 

        if (randomValue < (cumulative += pattern1Chance * 0.01)) {
            return enemyBehaviorPatterns[0];
        } else if (randomValue < (cumulative += pattern2Chance * 0.01)) {
            return enemyBehaviorPatterns[1];
        } else if (randomValue < (cumulative += pattern3Chance * 0.01)) {
            return enemyBehaviorPatterns[2];
        } else {
            return enemyBehaviorPatterns[3];
        }
    }
    
    

    /**
    * Выбирает шаблон поведения противника на основе его типа.
    * Используется для определения последовательности действий врага (атака/защита).
    *
    * @param enemy текущий противник
    * @return массив с последовательностью действий (1 — атака, 0 — защита)
    */
   public int[] selectEnemyBehavior(Player enemy) {

       if ("Baraka".equals(enemy.getClass().getSimpleName())) {
           return generateEnemyBehavior(30, 60, 10, 0, Math.random());


       } else if ("SubZero".equals(enemy.getClass().getSimpleName())) {
           return generateEnemyBehavior(50, 0, 50, 0, Math.random());

       } else if ("LiuKang".equals(enemy.getClass().getSimpleName())) {
           return generateEnemyBehavior(25, 70, 5, 0, Math.random());

       } else if ("SonyaBlade".equals(enemy.getClass().getSimpleName())) {
           return generateEnemyBehavior(40, 50, 10, 0, Math.random());
       }

       return generateEnemyBehavior(30, 60, 10, 0, Math.random());
   }

    /**
    * Обновляет состояние индикатора здоровья (прогресс-бар) на основе текущего здоровья персонажа.
    *
    * @param player   игрок или противник, чьё здоровье отслеживается
    * @param progressBar индикатор здоровья (JProgressBar)
    */
   public void updateHealthBar(Player player, JProgressBar progressBar) {
       int maxHealth = player.getMaxHealth();
       int currentHealth = player.getHealth();

 
       int healthPercentage = Math.max(0, Math.min(100, (currentHealth * 100) / maxHealth));
       progressBar.setValue(healthPercentage);
   }

    /**
    * Начисляет игроку опыт и очки за победу над обычным врагом.
    * Если накоплено достаточно опыта — повышает уровень игрока и усиливает его параметры.
    *
    * @param human игрок, которому начисляются опыт и очки
    * @param enemies список доступных противников для масштабирования их характеристик
    */
   public void grantExperienceAndPoints(Human human, Player[] enemies) {
       // Таблицы зависимости уровня на опыт и очки
       int[] levelToExperience = {20, 25, 30, 40, 50, 60}; // опыт по уровням 0-5
       int[] levelToBasePoints = {25, 30, 35, 45, 55, 70}; // очки по уровням 0-5

       int level = Math.min(human.getLevel(), levelToExperience.length - 1);

       int experienceGained = levelToExperience[level];
       int basePoints = levelToBasePoints[level];

       // Расчёт финальных очков с учётом здоровья
       int healthFactor = Math.max(0, human.getHealth());
       int finalPoints = basePoints + (healthFactor / 4);

       // Начисляем опыт и очки
       human.setExperience(experienceGained);
       human.setPoints(finalPoints);

       // Проверяем повышение уровня
       for (int i = 0; i < experienceForNextLevel.length; i++) {
           if (human.getExperience() == experienceForNextLevel[i]) {
               human.setLevel();
               human.setNextExperience(experienceForNextLevel[i + 1]);

               increasePlayerStatsOnLevelUp(human);
               adjustEnemyStatsToPlayer(human, enemies);
               break;
           }
       }
   }
   
   private void increasePlayerStatsOnLevelUp(Player player) {
        player.setMaxHealth((int) (player.getMaxHealth() * 1.2));
        player.setDamage((int) (player.getDamage() * 1.1));      
    }
   
   
   private void adjustEnemyStatsToPlayer(Human human, Player[] enemies) {
        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i] != null && !(enemies[i] instanceof ShaoKahn)) {
                enemies[i].setMaxHealth((int) (enemies[i].getMaxHealth() * (1 + (human.getLevel() - 1) * 0.2))); //Здоровье на 20 процентов больше
                enemies[i].setDamage((int) (enemies[i].getDamage() * (1 + (human.getLevel() - 1) * 0.1))); // Урон на 10 процентов больше
            }
        }
    }
   

    /**
    * Начисляет игроку опыт и очки за победу над боссом.
    * Если накоплено достаточно опыта — повышает уровень игрока и усиливает его параметры.
    *
    * @param human игрок, которому начисляются опыт и очки
    * @param enemies список доступных противников для масштабирования их характеристик
    */
   public void grantExperienceAndPointsFromBoss(Human human, Player[] enemies) {
       int experienceGained;
       int basePoints;

       int level = human.getLevel();
       if (level == 2) {
           experienceGained = 30;
           basePoints = 45;
       } else if (level == 4) {
           experienceGained = 50;
           basePoints = 65;
       } else {
           experienceGained = 40;
           basePoints = 50;
       }

       // Расчёт финальных очков с учётом здоровья
       int healthFactor = Math.max(0, human.getHealth());
       int finalPoints = basePoints + (healthFactor / 2); // Учитываем здоровье в 2 раза больше

     
       human.setExperience(experienceGained);
       human.setPoints(finalPoints);

      
       for (int i = 0; i < experienceForNextLevel.length; i++) {
           if (human.getExperience() == experienceForNextLevel[i]) {
               human.setLevel();
               human.setNextExperience(experienceForNextLevel[i + 1]);

               increasePlayerStatsOnLevelUp(human);
               adjustEnemyStatsToPlayer(human, enemies);
               break;
           }
       }
   }

    /**
 * Вызывается после победы над врагом.
 * С вероятностью добавляет игроку один из следующих предметов:
 * - Малое зелье лечения (25%)
 * - Большое зелье лечения (15%)
 * - Крест возрождения (5%)
 *
 * @param playerItems массив предметов игрока, в который может быть добавлен новый предмет
 */
public void dropItemsOnVictory(Items[] playerItems) {
    double chance = Math.random() * 100;

    if (chance < 5 && playerItems[2].getCount() < 99) {
        playerItems[2].setCount(1); // Крест воскрешения
    } else if (chance < 20 && playerItems[1].getCount() < 99) {
        playerItems[1].setCount(1); // Большое зелье лечения
    } else if (chance < 45 && playerItems[0].getCount() < 99) {
        playerItems[0].setCount(1); // Малое зелье лечения
    }
}

    /**
    * Усиливает параметры игрока при повышении уровня.
    * Здоровье и урон зависят от текущего уровня игрока.
    *
    * @param human игрок, которому нужно повысить параметры
    */
   public void increasePlayerStats(Human human) {
       int level = human.getLevel();

       int[] healthBonusTable = {0, 25, 30, 30, 40}; 
       int[] damageBonusTable = {0, 3, 3, 4, 6};     

       int maxLevel = Math.min(level, healthBonusTable.length - 1);

       int bonusHealth = healthBonusTable[maxLevel];
       int bonusDamage = damageBonusTable[level];

       human.setMaxHealth(bonusHealth);
       human.setDamage(bonusDamage);
   }

    /**
    * Усиливает параметры противника в зависимости от уровня игрока.
    * Здоровье и урон увеличиваются как процент от исходных значений.
    *
    * @param human игрок, по уровню которого корректируется сложность
    * @param enemy противник, которого нужно усилить
    */
   public void adjustEnemyStats(Human human, Player enemy) {
       int level = human.getLevel();

       int[] healthBonusPercentTable = {0, 32, 30, 23, 25}; 
       int[] damageBonusPercentTable = {0, 25, 20, 24, 26}; 

    
       if (level < 1 || level >= healthBonusPercentTable.length) {
           level = Math.min(level, healthBonusPercentTable.length - 1);
       }


       int healthBonusPercent = healthBonusPercentTable[level];
       int damageBonusPercent = damageBonusPercentTable[level];

       int additionalHealth = (int) (enemy.getMaxHealth() * healthBonusPercent / 100.0);
       int additionalDamage = (int) (enemy.getDamage() * damageBonusPercent / 100.0);

       enemy.setMaxHealth(additionalHealth);
       enemy.setDamage(additionalDamage);
       enemy.setLevel(human.getLevel());
   }
   

    /**
    * Обрабатывает использование выбранного предмета из мешка.
    * Поддерживает: малое зелье, большое зелье, крест возрождения.
    *
    * @param player игрок, применяющий предмет
    * @param itemsDialog окно мешка предметов
    */
   public void useSelectedItemFromBag(Player player, ItemsDialog itemsDialog) {
       int selectedIndex = itemsDialog.getSelectedIndex();

       if (selectedIndex == -1) {
           JOptionPane.showMessageDialog(null, "Пожалуйста, выберите предмет.");
           return;
       }

       String[] allItems = ItemsDialog.allItems;
       String selectedItemName = allItems[selectedIndex];

       
       if ("Малое зелье лечения".equals(selectedItemName)) {
           useSmallPotion(player, itemsDialog);
       } else if ("Большое зелье лечения".equals(selectedItemName)) {
           useBigPotion(player, itemsDialog);
       } else if ("Крест возрождения".equals(selectedItemName)) {
           useReviveCross(player, itemsDialog);
       }
   }
   
   private void useSmallPotion(Player player, ItemsDialog dialog) {
        int count = dialog.getItemCount("Малое зелье лечения");
        if (count > 0) {
            player.setHealth((int) (player.getMaxHealth() * 0.25));
            dialog.addItem("Малое зелье лечения", -1); 
            dialog.dispose(); // закрываем окно
        } else {
            JOptionPane.showMessageDialog(dialog, "Нет доступных предметов");
        }
    }
   
   private void useBigPotion(Player player, ItemsDialog dialog) {
        int count = dialog.getItemCount("Большое зелье лечения");
        if (count > 0) {
            player.setHealth((int) (player.getMaxHealth() * 0.5));
            dialog.addItem("Большое зелье лечения", -1);
            dialog.dispose();
        } else {
            JOptionPane.showMessageDialog(dialog, "Нет доступных предметов");
        }
    }
   
   private void useReviveCross(Player player, ItemsDialog dialog) {
        int count = dialog.getItemCount("Крест возрождения");
        if (count > 0 && player.getHealth() <= 0) {
            player.setHealth((int) (player.getMaxHealth() * 0.05)); 
            dialog.addItem("Крест возрождения", -1);
            dialog.dispose();
        } else if (count > 0) {
            JOptionPane.showMessageDialog(dialog, "Крест можно использовать только если здоровье <= 0");
        } else {
            JOptionPane.showMessageDialog(dialog, "Нет доступных предметов");
        }
    }
   
}
