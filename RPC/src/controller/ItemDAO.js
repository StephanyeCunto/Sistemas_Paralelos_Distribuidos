import fs from 'fs'; 

const LOCAL_JSON_PATH = "../data/listItens.json";

export class ItemDAO{
    update(item,price){
        const items = this.getItems();
        const index = items.findIndex(items => items.Name === item.Name);
        if(index > -1) items[index] = { Name: item.Name, Preco: price };
        this.saveItems(items);
    }
    
    delete(item){
        const items = this.getItems().filter(items => items.Name !== item.Name);
        this.saveItems(items);
    }
    
    findItemByName(name){
        const items = this.getItems();
        return items.find(item => item.Name === name);
    }
    
    getItems(){
        return JSON.parse(fs.readFileSync(LOCAL_JSON_PATH, 'utf8'));
    }
    
    create(params){
        const listItems = this.getItems();
        listItems.push(this.createItem(params[0]));
        this.saveItems(listItems);
    }
    
    createItem(item) {
        return {Name: item};
    }
    
    createItemPurchased(item,price){
        return {Name: item.Name , Preco: price}
    }
    
    saveItems(items){
        fs.writeFileSync(LOCAL_JSON_PATH , JSON.stringify(items, null, 2));
    }
}