import pkg from 'xmlrpc';
import fs from 'fs'; 

const { createServer } = pkg;

const localJsonPath = "./data/listItens.json";

export class serve{
    constructor(){
        this.server = createServer({ host: '0.0.0.0', port: 9090 });
        this.server.on('create', this.handleCreate.bind(this));
        this.server.on('read', this.handleRead.bind(this));
        this.server.on('update', this.handleUpdate.bind(this));
        this.server.on('delete', this.handleDelete.bind(this));
    }

    handleDelete(err, params, callback){
        const item = this.searchItensName(params[0]);
        if(item) this.delete(item);
        const itens = this.getItens();
        callback(null,itens);
    }

    handleUpdate(err, params, callback){
        const item = this.searchItensName(params[0]);
        if(item) this.update(item,params[1]);
        const itens = this.getItens();
        callback(null,itens);
    }

    handleRead(err, params, callback){
        const itens = this.getItensName();
        callback(null,itens);
    }

    handleCreate(err,params, callback){
        this.create(params);
        callback(null, `Item adicionado com sucesso.`);
    }

    update(item,price){
        const itens = this.getItens().filter(itens => itens.Name !== item.Name); 
        itens.push(this.createItemPurchased(item, price));
        this.saveItens(itens);
    }

    delete(item){
        const itens = this.getItens().filter(itens => itens.Name !== item.Name);
        this.saveItens(itens);
    }

    searchItensName(name){
        const itens = this.getItens();
        for(let item of itens){
            if(name == item.Name) return item
        }
    }

    getItensName(){
        return this.getItens().map(itens => itens.Name);
    }

    getItens(){
        return JSON.parse(fs.readFileSync(localJsonPath, 'utf8'));
    }

    create(params){
        const listItens = this.getItens();
        listItens.push(this.createItem(params[0]));
        this.saveItens(listItens);
    }

    createItem(item) {
        return {Name: item};
    }

    createItemPurchased(item,price){
        return {Name: item.Name , Preco: price}
    }

    saveItens(itens){
        fs.writeFileSync(localJsonPath , JSON.stringify(itens, null, 2));
    }
}

const s = new serve();
console.log('Servidor rodando em http://0.0.0.0:9090');
