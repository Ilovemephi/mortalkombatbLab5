
package mephi.b22901.ae.lab5;

import java.awt.Color;
import java.util.Random;
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
   private Items[] items = new Items[3];

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
   private Player[] enemies = new Player[5];

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
                          
                                 
}
    
    /**
    * Инициализирует предметы игрока с нулевым количеством.
    */
   public void initializeItems() {
       items[0] = new Items("Малое зелье лечения", 0);
       items[1] = new Items("Большое зелье лечения", 0);
       items[2] = new Items("Крест возрождения", 0);
   }




    public Player[] getEnemies() {
        return this.enemies;
    }
    
    /**
    * Возвращает массив предметов игрока.
    *
    * @return массив с тремя типами предметов: малое зелье, большое зелье, крест
    */
   public Items[] getItems() {
       return this.items;
   }
    
    /**
    * Возвращает массив, содержащий количество опыта, необходимое для повышения уровня.
    *
    * @return массив порогов опыта
    */
   public int[] getExperienceForNextLevel() {
       return experienceForNextLevel;
   }
    

    /**
    * Выбирает случайного обычного противника и обновляет данные в интерфейсе.
    * Не усиливает врага, так как он уже был настроен ранее.
    *
    * @return              выбранный противник
    */
   public Player chooseEnemy() {
       // Генерируем случайный индекс: 0–3
       int randomIndex = new Random().nextInt(4);

       // Получаем случайного противника
       currentEnemy = enemies[randomIndex];

       // Обновляем интерфейс через метод updateEnemyUI в FightFrame
       return currentEnemy;
   }

    public Player chooseBoss(Human human) {
    
        currentEnemy = enemies[4];
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
    * Начисляет игроку опыт и очки за победу над обычным врагом.
    * Повышает уровень игрока, если он достиг порога.
    *
    * @param human игрок, которому начисляются опыт и очки
    * @param enemies список доступных противников для масштабирования сложности
    */
    public void grantExperienceAndPoints(Human human, Player[] enemies) {
        int level = human.getLevel();

        // Получаем базовые значения опыта и очков
        int[] levelToExperience = {20, 25, 30, 40, 50, 60};
        int[] levelToBasePoints = {25, 30, 35, 45, 55, 70};

        level = Math.min(level, levelToExperience.length - 1);

        int baseExp = levelToExperience[level];
        int basePoints = levelToBasePoints[level];

        // Расчёт финальных очков с учётом здоровья
        int healthFactor = Math.max(0, human.getHealth());
        int finalPoints = basePoints + (healthFactor / 4);

        // Начисляем опыт и очки
        human.setExperience(baseExp);
        human.setPoints(finalPoints);

   
    }
   
   private void increasePlayerStatsOnLevelUp(Player player) {
        player.setMaxHealth((int) (player.getMaxHealth() * 0.1));
        player.setDamage((int) (player.getDamage() * 0.05));      
    }
   
   
   public void adjustEnemyStatsToPlayer(Human human, Player[] enemies) {
        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i] != null && !(enemies[i] instanceof ShaoKahn)) {
                enemies[i].setMaxHealth((int) (enemies[i].getMaxHealth()  * 0.9)); 
                enemies[i].setDamage((int) (enemies[i].getDamage() * 0.9)); 
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
 * @param itemsDialog Диалоговое окно выбора предметов 
 */

   public void dropItemsOnVictory(Human human, ItemsDialog itemsDialog) {
        double chance = Math.random() * 100;

        if (chance < 5) {
            itemsDialog.addItem("Крест возрождения", 1);
        } else if (chance < 20) {
            itemsDialog.addItem("Большое зелье лечения", 1);
        } else if (chance < 45) {
            itemsDialog.addItem("Малое зелье лечения", 1);
        }
    }
    
    
   
   
}
