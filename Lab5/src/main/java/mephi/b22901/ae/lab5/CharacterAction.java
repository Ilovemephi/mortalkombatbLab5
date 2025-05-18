
package mephi.b22901.ae.lab5;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;


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

    public void AddPoints(Human human, Player[] enemyes) {
        switch (human.getLevel()) {
            case 0:
                human.setExperience(20);
                human.setPoints(25 + human.getHealth() / 4);
                break;
            case 1:
                human.setExperience(25);
                human.setPoints(30 + human.getHealth() / 4);
                break;
            case 2:
                human.setExperience(30);
                human.setPoints(35 + human.getHealth() / 4);
                break;
            case 3:
                human.setExperience(40);
                human.setPoints(45 + human.getHealth() / 4);
                break;
            case 4:
                human.setExperience(50);
                human.setPoints(55 + human.getHealth() / 4);
                break;
        }
        for (int i = 0; i < 5; i++) {
            if (experienceForNextLevel[i] == human.getExperience()) {
                human.setLevel();
                human.setNextExperience(experienceForNextLevel[i + 1]);
                NewHealthHuman(human);
                for (int j = 0; j < 4; j++) {
                    NewHealthEnemy(enemyes[j], human);
                }
            }
        }
    }

    public void AddPointsBoss(Human human, Player[] enemyes) {
        switch (human.getLevel()) {
            case 2:
                human.setExperience(30);
                human.setPoints(45 + human.getHealth() / 2);
                break;
            case 4:
                human.setExperience(50);
                human.setPoints(65 + human.getHealth() / 2);
                break;
        }
        for (int i = 0; i < 5; i++) {
            if (experienceForNextLevel[i] == human.getExperience()) {
                human.setLevel();
                human.setNextExperience(experienceForNextLevel[i + 1]);
                NewHealthHuman(human);
                for (int j = 0; j < 4; j++) {
                    NewHealthEnemy(enemyes[j], human);
                }
            }
        }
    }

    public void AddItems(int k1, int k2, int k3, Items[] items) {
        double i = Math.random();
        if (i < k1 * 0.01) {
            items[0].setCount(1);
        }
        if (i >= k1 * 0.01 & i < (k1 + k2) * 0.01) {
            items[1].setCount(1);
        }
        if (i >= (k1 + k2) * 0.01 & i < (k1 + k2 + k3) * 0.01) {
            items[2].setCount(1);
        }
    }

    public void NewHealthHuman(Human human) {
        int hp = 0;
        int damage = 0;
        switch (human.getLevel()) {
            case 1:
                hp = 25;
                damage = 3;
                break;
            case 2:
                hp = 30;
                damage = 3;
                break;
            case 3:
                hp = 30;
                damage = 4;
                break;
            case 4:
                hp = 40;
                damage = 6;
                break;
        }
        human.setMaxHealth(hp);
        human.setDamage(damage);
    }

    public void NewHealthEnemy(Player enemy, Human human) {
        int hp = 0;
        int damage = 0;
        switch (human.getLevel()) {
            case 1:
                hp = 32;
                damage = 25;
                break;
            case 2:
                hp = 30;
                damage = 20;
                break;
            case 3:
                hp = 23;
                damage = 24;
                break;
            case 4:
                hp = 25;
                damage = 26;
                break;
        }
        enemy.setMaxHealth((int) enemy.getMaxHealth() * hp / 100);
        enemy.setDamage((int) enemy.getDamage() * damage / 100);
        enemy.setLevel();
    }

    public void UseItem(Player human, Items[] items, String name, JDialog dialog, JDialog dialog1) {
        switch (name) {
            case "jRadioButton1":
                if (items[0].getCount() > 0) {
                    human.setHealth((int) (human.getMaxHealth() * 0.25));
                    items[0].setCount(-1);
                } else {
                    dialog.setVisible(true);
                    dialog.setBounds(300, 200, 400, 300);
                }
                break;
            case "jRadioButton2":
                if (items[1].getCount() > 0) {
                    human.setHealth((int) (human.getMaxHealth() * 0.5));
                    items[1].setCount(-1);
                } else {
                    dialog.setVisible(true);
                    dialog.setBounds(300, 200, 400, 300);
                }
                break;
            case "jRadioButton3":
                dialog.setVisible(true);
                dialog.setBounds(300, 200, 400, 300);
                break;
        }
        
        if(dialog.isVisible()==false){
            dialog1.dispose();
        }
    }
}
