package org.example;
@Strategies(
        idStr = NamesStr.SubstractionPercentFromCost,
        type = IStrategy.class
)
public class SubstractionPercentFromCost implements IStrategy{
    @ValidatingItem
    @Override
    public void execOperation(Item... products) {
        for(Item item:products){
            System.out.println(item.name+"------->"+(item.cost*0.8f));
        }
        System.out.println();
    }
}