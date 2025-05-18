
package mephi.b22901.ae.lab5;


public class SonyaBlade extends Player{
    
    public SonyaBlade(int level, int health, int damage, int attack) {
        super(level, health, damage, attack);
    }

    
    @Override
    public String getName(){
        return "Sonya Blade";
    }
}
