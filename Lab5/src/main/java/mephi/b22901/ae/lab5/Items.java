/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.ae.lab5;

/**
 *
 * @author Мария
 */
public class Items {
    
    private String name;
    private int count;
    
    public Items(String name, int count){
        this.name=name;
        this.count=count;
    }
    
    public void setName(String name){
        this.name=name;
    }
    public void setCount(int count){
        this.count+=count;
    }
    
    public String getName(){
        return this.name;
    }
    public int getCount(){
        return this.count;
    }
}
