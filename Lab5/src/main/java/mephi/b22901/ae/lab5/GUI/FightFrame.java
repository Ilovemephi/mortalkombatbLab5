/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package mephi.b22901.ae.lab5.GUI;

import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import mephi.b22901.ae.lab5.Player;
import mephi.b22901.ae.lab5.*;
import mephi.b22901.ae.lab5.battle.BattleManager;
/**
 *
 * @author artyom_egorkin
 */
public class FightFrame extends javax.swing.JFrame {
    
   private Human human; // основной игрок   
    private Player currentEnemy;
    private CharacterAction characterAction;
    private BattleManager battleManager;
    ItemsDialog items = new ItemsDialog(this, true);
    
    
    private int totalLocations;
    private int currentLocation;
    private int enemiesRemaining;
    private boolean fightingBoss;


    

    
    public FightFrame(int locationCount) {  
        
        this.totalLocations = locationCount;
        this.currentLocation = 1;
        
        
        
        // Сначала создаём игрока
        this.human = new Human(0, 80, 16, 1); 
        this.human.resetToDefault();
        // Затем создаём CharacterAction и инициализируем противников
        this.characterAction = new CharacterAction();
        this.characterAction.initializeEnemies(); // инициализация всех врагов
        this.characterAction.initializeItems();

        // Создаем BattleManager уже с готовым игроком и CharacterAction
        this.battleManager = new BattleManager(human, characterAction);

        initComponents();

//        // Выбираем первого врага и обновляем интерфейс
//        battleManager.startNewRound(this);
        
        startNextLocation();
    }
    

    
    public void updatePlayerUI(Human player) {
        humanBar.setMaximum(player.getMaxHealth());
        humanBar.setValue(player.getHealth());
        humanHealthLabel.setText(player.getHealth() + "/" + player.getMaxHealth());
        humanLevelLabel.setText("Уровень: " + player.getLevel());
        humanDamageLabel.setText("Урон: " + player.getDamage());
         humanPointLabel.setText("Очки: " + ((Human) player).getPoints());  
        // Опыт
        int currentExp = player.getExperience();
        int nextLevelExp = getNextLevelExperience(player.getLevel());
        expLabel.setText("Опыт: " + currentExp + " / " + nextLevelExp);
    }
    
    public void updateEnemyUI(Player enemy) {
        if (enemy != null) {
            opponentBar.setMaximum(enemy.getMaxHealth());
            opponentBar.setValue(enemy.getHealth());
            opHealthLabel.setText(enemy.getHealth() + "/" + enemy.getMaxHealth());
            opLevelLabel.setText("Уровень: " + enemy.getLevel());
            opDamageLabel.setText("Урон: " + enemy.getDamage());
            opNameLabel.setText(enemy.getName());
        }
    }
    
    /**
    * Вспомогательный метод для получения порога следующего уровня
    */
   private int getNextLevelExperience(int currentLevel) {
       if (currentLevel >= characterAction.getExperienceForNextLevel().length - 1) {
           return 99999; // если это последний уровень
       }
       return characterAction.getExperienceForNextLevel()[currentLevel];
   }
    
    
    public JLabel getEnemyName() {
        return opNameLabel;
    }   

    

    public JLabel getEnemyDamage() {
        return opDamageLabel;
    }

    public JProgressBar getEnemyHealth() {
        return opponentBar;
    }
    
    public void setTurnLabelText(String text) {
    turnLabel.setText(text);
}
    
    
    private void startNextLocation() {
        if (currentLocation <= totalLocations) {
            JOptionPane.showMessageDialog(this, "Локация " + currentLocation + " из " + totalLocations);

            fightingBoss = false;
            enemiesRemaining = getRandomEnemyCount(human.getLevel());

            startEnemyBattle();
        } else {
            JOptionPane.showMessageDialog(this, "Поздравляем! Вы прошли все локации!");
            
            int finalScore = human.getPoints();
            // Здесь можно будет сохранить очки
            this.dispose();
        }
    }
    
    private int getRandomEnemyCount(int level) {
        return new Random().nextInt(2) + level; // Например: level до level+1 врагов
    }
    
    private void startEnemyBattle() {
        fightingBoss = false;
        battleManager.startNewRound(this);
    }
    
    private void startBossBattle() {
        fightingBoss = true;
        Player boss = characterAction.chooseBoss(human);
        battleManager.startSpecificEnemyRound(this, boss);  // этот метод создадим в следующем шаге
    }
    
    public void onEnemyDefeated() {
        if (fightingBoss) {
            currentLocation++;
            startNextLocation(); // переход на следующую локацию
        } else {
            enemiesRemaining--;
            if (enemiesRemaining > 0) {
                startEnemyBattle(); // ещё враги остались
            } else {
                startBossBattle(); // все враги побеждены — вызываем босса
            }
        }
    }



    



    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        attackButton = new javax.swing.JButton();
        defenceButton = new javax.swing.JButton();
        itemsButton = new javax.swing.JButton();
        opponentPanel = new javax.swing.JPanel();
        opponentBar = new javax.swing.JProgressBar();
        opDamageLabel = new javax.swing.JLabel();
        opLevelLabel = new javax.swing.JLabel();
        opNameLabel = new javax.swing.JLabel();
        opHealthLabel = new javax.swing.JLabel();
        humanPanel = new javax.swing.JPanel();
        humanBar = new javax.swing.JProgressBar();
        humanDamageLabel = new javax.swing.JLabel();
        humanLevelLabel = new javax.swing.JLabel();
        humanNameLabel = new javax.swing.JLabel();
        humanHealthLabel = new javax.swing.JLabel();
        turnLabel = new javax.swing.JLabel();
        expLabel = new javax.swing.JLabel();
        humanPointLabel = new javax.swing.JLabel();
        weakenButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        attackButton.setText("Атковать");
        attackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attackButtonActionPerformed(evt);
            }
        });

        defenceButton.setText("Защищаться");
        defenceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defenceButtonActionPerformed(evt);
            }
        });

        itemsButton.setText("Предметы");
        itemsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemsButtonActionPerformed(evt);
            }
        });

        opDamageLabel.setText("Damage:");

        opLevelLabel.setText("Level: ");

        opNameLabel.setText("jLabel1");

        opHealthLabel.setText("jLabel1");

        javax.swing.GroupLayout opponentPanelLayout = new javax.swing.GroupLayout(opponentPanel);
        opponentPanel.setLayout(opponentPanelLayout);
        opponentPanelLayout.setHorizontalGroup(
            opponentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(opponentPanelLayout.createSequentialGroup()
                .addGroup(opponentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(opponentPanelLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(opponentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(opLevelLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(opDamageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(opponentBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(opHealthLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(opponentPanelLayout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(opNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        opponentPanelLayout.setVerticalGroup(
            opponentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(opponentPanelLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(opHealthLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(opponentBar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(opDamageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(opLevelLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(opNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        humanDamageLabel.setText("Damage:");

        humanLevelLabel.setText("Level:");

        humanNameLabel.setText("Kitana");

        humanHealthLabel.setText("jLabel1");

        javax.swing.GroupLayout humanPanelLayout = new javax.swing.GroupLayout(humanPanel);
        humanPanel.setLayout(humanPanelLayout);
        humanPanelLayout.setHorizontalGroup(
            humanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(humanPanelLayout.createSequentialGroup()
                .addGroup(humanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(humanPanelLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(humanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(humanLevelLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(humanDamageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(humanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(humanHealthLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(humanBar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(humanPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(humanNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        humanPanelLayout.setVerticalGroup(
            humanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(humanPanelLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(humanHealthLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(humanBar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(humanDamageLabel)
                .addGap(18, 18, 18)
                .addComponent(humanLevelLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(humanNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );

        turnLabel.setText("turn: ");

        expLabel.setText("jLabel1");

        humanPointLabel.setText("jLabel1");

        weakenButton.setText("Ослабить");
        weakenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weakenButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(opponentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(expLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                            .addComponent(humanPointLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(itemsButton)
                                .addGap(26, 26, 26)
                                .addComponent(weakenButton, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(turnLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(defenceButton)
                        .addGap(18, 18, 18)
                        .addComponent(attackButton))
                    .addComponent(humanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(opponentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(humanPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(expLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(humanPointLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                        .addComponent(turnLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(attackButton)
                    .addComponent(defenceButton)
                    .addComponent(itemsButton)
                    .addComponent(weakenButton))
                .addGap(32, 32, 32))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void defenceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_defenceButtonActionPerformed
        battleManager.playerDefend(human, this, items);
    }//GEN-LAST:event_defenceButtonActionPerformed

    private void itemsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemsButtonActionPerformed
        
        items.setItemUsedListener(itemName -> {
            battleManager.useSelectedItemFromBag(human, items, this);
        });
        items.setVisible(true);
    }//GEN-LAST:event_itemsButtonActionPerformed

    private void attackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attackButtonActionPerformed
        battleManager.playerAttack(human, this, items);
    }//GEN-LAST:event_attackButtonActionPerformed

    private void weakenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weakenButtonActionPerformed
        battleManager.playerWeaken(human, this, items);

    }//GEN-LAST:event_weakenButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton attackButton;
    private javax.swing.JButton defenceButton;
    private javax.swing.JLabel expLabel;
    private javax.swing.JProgressBar humanBar;
    private javax.swing.JLabel humanDamageLabel;
    private javax.swing.JLabel humanHealthLabel;
    private javax.swing.JLabel humanLevelLabel;
    private javax.swing.JLabel humanNameLabel;
    private javax.swing.JPanel humanPanel;
    private javax.swing.JLabel humanPointLabel;
    private javax.swing.JButton itemsButton;
    private javax.swing.JLabel opDamageLabel;
    private javax.swing.JLabel opHealthLabel;
    private javax.swing.JLabel opLevelLabel;
    private javax.swing.JLabel opNameLabel;
    private javax.swing.JProgressBar opponentBar;
    private javax.swing.JPanel opponentPanel;
    private javax.swing.JLabel turnLabel;
    private javax.swing.JButton weakenButton;
    // End of variables declaration//GEN-END:variables

    
}
