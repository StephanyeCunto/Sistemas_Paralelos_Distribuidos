import fs from 'fs'; 

import { Item } from "../model/Item.js";

const LOCAL_JSON_PATH = "../data/listItens.json";

export class ItemDAO{
    create(params){
        const itemNew = new Item(this.toUpperFirstLowerRest(params));
        const itemInList = this.findItemByName(itemNew.name);
        if(itemInList) this.update(itemInList,itemInList.quantity+1);
        else this.addItem(itemNew);
    }

    toUpperFirstLowerRest(str = str.trim()) {
        str = str.trim();
        return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
    }

    update(item,quantity = null,price = null,isPurchased = null){
        if(quantity != null && quantity > 0) item.quantity = quantity;
        if(price != null && price > 0) item.price = price;
        if(isPurchased != null && isPurchased.length > 0) item.isPurchased = isPurchased;
        this.deleteByName(item.name);
        this.addItem(item);
    }

    addItem(item){
        const listItems = this.getAll();
        listItems.push(item);
        this.saveItems(listItems);
    }

    getAll(){
        return JSON.parse(fs.readFileSync(LOCAL_JSON_PATH, 'utf8'));
    }

    saveItems(items){
        fs.writeFileSync(LOCAL_JSON_PATH , JSON.stringify(items, null, 2));
    }

    findItemByName(itemName){
        const items = this.getAll();
        const itemFind = this.toUpperFirstLowerRest(itemName);
        const foundItem = items.find(item => item.name === itemFind);
        if(foundItem) return foundItem;
        return null;    
    }

    deleteByName(itemName){
        itemName = this.toUpperFirstLowerRest(itemName);

        const originalItems= this.getAll();
        const items = originalItems.filter(item => item.name !== itemName);
        this.saveItems(items);
        return(items.length < originalItems.length);
    }
}