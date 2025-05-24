
package mephi.b22901.ae.lab5;

public abstract class Player {
    protected int level;
    protected int health;
    protected int maxHealth;
    protected int damage;
    protected int attack;
    
    
    // --- Дебафф ослабления ---
    protected int weakenTurns = 0;         // Сколько ходов ослабление длится
    protected int buffTurns = 0;           // Сколько ходов бонус-урон
    protected double buffDamageRatio = 0.0;// Например, 0.15 для +15%

    public Player(int level, int health, int damage, int attack) {
        this.level = level;
        this.health = health;
        this.maxHealth = health;
        this.damage = damage;
        this.attack = attack;
    }

    public int getLevel() {
        return level;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getDamage() {
        return damage;
    }

    public int getAttack() {
        return attack;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setHealth(int health) {
        this.health += health;
    }

    public void setNewHealth(int health) {
        this.health = health;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth += maxHealth;
    }

    public void setDamage(int damage) {
        this.damage += damage;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }
    
    public void setLevel(){
        this.level++;
    }
    
    public void restoreFullHealth() {
        this.health = this.maxHealth;
    }
    
    
    
    public void applyWeakenDebuff(int turns) {
        this.weakenTurns = turns;
    }
    public boolean hasWeakenDebuff() {
        return weakenTurns > 0;
    }
    public void decrementWeakenTurn() {
        if (weakenTurns > 0) 
            weakenTurns--;
    }

    public void applyBuffDamage(double ratio, int turns) {
        this.buffDamageRatio = ratio;
        this.buffTurns = turns;
    }
    public double getBuffDamage() {
        return buffTurns > 0 ? buffDamageRatio : 0.0;
    }
    public void decrementBuffTurn() {
        if (buffTurns > 0) buffTurns--;
        if (buffTurns == 0) buffDamageRatio = 0.0;
    }

    // Считать свой урон с учетом дебаффа/баффа
    public int getModifiedDamage() {
        int base = this.damage;
        if (hasWeakenDebuff()) {
            base = (int) (base * 0.5); // ослабление снижает наш урон на 50%
        }
        if (getBuffDamage() > 0) {
            base = (int) (base * (1.0 + getBuffDamage()));
        }
        return base;
    }

    // Считать урон, который ВЫ ПОЛУЧАЕТЕ (если на вас ослабление, получаете на 25% больше)
    public int receiveModifiedDamage(int incomingDamage) {
        if (hasWeakenDebuff()) {
            return (int) (incomingDamage * 1.25);
        }
        return incomingDamage;
    }


    public abstract String getName();
}