
package mephi.b22901.ae.lab5;

public abstract class Player {
    protected int level;
    protected int health;
    protected int maxHealth;
    protected int damage;
    protected int attack;

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


    public abstract String getName();
}