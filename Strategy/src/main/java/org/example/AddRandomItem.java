package org.example;

import java.util.Arrays;

@Strategies(
        idStr=NamesStr.AddRandomItem,
        type = IStrategy.class
)

public class AddRandomItem implements IStrategy{
    @ValidatingItem
    @Override
    public void execOperation(Item... products) {
        Item[] items= Arrays.copyOf(products,products.length+1);
        items[products.length]=new Item("RandomItem",(float)Math.random());
        for(Item item:items){
            System.out.println(item.name+"----->"+item.cost);
        }
        System.out.println();
    }
}
