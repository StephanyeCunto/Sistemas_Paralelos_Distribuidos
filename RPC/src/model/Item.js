export class Item{
    constructor(name,quantity = 1,price = null){
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.isPurchased = false;
    }
}