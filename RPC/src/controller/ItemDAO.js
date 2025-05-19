import fs from 'fs'; 

import { Item } from "../model/Item.js";

const LOCAL_JSON_PATH = "../data/listItens.json";

export class ItemDAO{
    create(params){
        const itemNew = new Item(this.toUpperFirstLowerRest(params));
        const itemInList = this.findItemByName(itemNew.name);
        if(itemInList) this.updateQuantity(itemInList,1);
        else this.addItem(itemNew);
    }

    toUpperFirstLowerRest(str) {
        str = str.trim();
        return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
    }

    updateQuantity(item,quantity){
        item.quantity = item.quantity + quantity;
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

    findItemByName(name){
        const items = this.getAll();
        const foundItem = items.find(item => item.name === name);
        if(foundItem) {
            return foundItem;
        }
        return null;
    }

    deleteByName(item){
        item = this.toUpperFirstLowerRest(item);
        const items = this.getAll().filter(items => items.name !== item);
        this.saveItems(items);
    }

/*
    update(item,price){
        const items = this.getAll();
        const index = items.findIndex(items => items.Name === item.Name);
        if(index > -1) items[index] = { Name: item.Name, Preco: price };
        this.saveItems(items);
    }
    
    delete(item){
        const items = this.getAll().filter(items => items.Name !== item.Name);
        this.saveItems(items);
    }
    
    findItemByName(name){
        const items = this.getAll();
        return items.find(item => item.Name === name);
    }
    
    getAll(){
        return JSON.parse(fs.readFileSync(LOCAL_JSON_PATH, 'utf8'));
    }
    
    createItemPurchased(item,price){
        return {Name: item.Name , Preco: price}
    }
    
    saveItems(items){
        fs.writeFileSync(LOCAL_JSON_PATH , JSON.stringify(items, null, 2));
        console.log(this.getAll());
    }

    */
}