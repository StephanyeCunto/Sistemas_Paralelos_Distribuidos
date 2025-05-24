export class Item{
    constructor(name,quantity = 1,price = null, isPurchased = null){
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.isPurchased = isPurchased;
    }

    getItem(){
        return [this.name, this.quantity, this.price, this.isPurchased];
    }
}