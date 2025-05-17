/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mortalkombatbversion;

/**
 *
 * @author Мария
 */
public class Baraka extends Player{
    
    public Baraka(int level, int health, int  damage, int attack){
        super (level, health, damage, attack);
    }
    
    @Override
    public String getName(){
        return "Baraka";
    }
    
}
