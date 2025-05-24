/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ae.lab5.Location;

import java.util.Random;
import javax.swing.JOptionPane;

public class LocationManager {
    private int totalLocations; // сколько локаций выбрал игрок
    private int currentLocation = 0;
    private int enemiesPerLocation; // количество врагов на локации
    private boolean bossDefeated = false;

    public LocationManager(int totalLocations) {
        this.totalLocations = totalLocations;
        this.enemiesPerLocation = 3 + totalLocations; // например, от 3 до 8 врагов
    }

    /**
     * Начинает новую локацию.
     * @return true, если это не последняя локация
     */
    public boolean startNextLocation() {
        currentLocation++;
        if (currentLocation > totalLocations) return false;
        JOptionPane.showMessageDialog(null, "Вы входите в локацию №" + currentLocation + "\nКоличество врагов: " + getRandomEnemyCount());
        return true;
    }

    /**
     * Проверяет, нужно ли сейчас бить босса.
     * @return true, если это финал локации
     */
    public boolean isFinalBossRound() {
        return getCurrentEnemyNumber() == 0;
    }

    /**
     * Уменьшает счётчик врагов.
     */
    public void enemyDefeated() {
        enemiesPerLocation--;
    }

    /**
     * Возвращает случайное количество врагов на локации (от 3 до 6)
     */
    public int getRandomEnemyCount() {
        return 3 + new Random().nextInt(3); 
    }

    public int getTotalLocations() {
        return totalLocations;
    }

    public int getCurrentLocation() {
        return currentLocation;
    }

    public int getCurrentEnemyNumber() {
        return enemiesPerLocation;
    }

    public boolean isAllLocationsCompleted() {
        return currentLocation >= totalLocations;
    }
}