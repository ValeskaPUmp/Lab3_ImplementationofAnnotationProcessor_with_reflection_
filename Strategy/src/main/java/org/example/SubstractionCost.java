package org.example;
@Strategies(
        idStr=NamesStr.SubstractionCost,
        type = IStrategy.class
)

public class SubstractionCost implements IStrategy {
    @ValidatingItem
    @Override
    public void execOperation(Item... products) {
        for(Item item:products){
            System.out.println(item.name+"------->"+(item.cost-10));
        }
        System.out.println();
    }
}