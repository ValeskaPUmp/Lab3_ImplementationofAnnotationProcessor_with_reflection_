package org.example;

public class Item {
    public String name;
    public float cost;
    public Item(String name, float cost){
        this.cost=cost;
        this.name=name;
    }

    @Override
    public String toString() {
        return name+"------>"+cost;
    }
}
