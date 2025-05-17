import pkg from 'xmlrpc';
import fs from 'fs'; 

const { createServer } = pkg;

const localJsonPath = "./data/listItens.json";

export class serve{
    constructor(){
        this.server = createServer({ host: '0.0.0.0', port: 9090 });
        this.server.on('create', this.handleCreate.bind(this));
        this.server.on('read', this.handleRead.bind(this));
    }

    handleRead(err, params, callback){
        const itens = this.read().Itens;
        const nameItens = itens.map(itens => itens.Name);
        callback(null,nameItens);
    }

    handleCreate(err,params, callback){
        this.create(params);
        callback(null, `Item adicionado com sucesso.`);
    }

    read(){
        return JSON.parse(fs.readFileSync(localJsonPath, 'utf8'));
    }

    create(params){
        const item = this.createItem(params[0]);
        const listItens = this.read();
        listItens.Itens.push(item);
        fs.writeFileSync(localJsonPath , JSON.stringify(listItens, null, 2));
    }

    createItem(item) {
        const itemObjeto = {Name: item};
        return itemObjeto;
    }

}

const s = new serve();
console.log('Servidor rodando em http://0.0.0.0:9090');
