import { RpcServer } from '../services/RpcServer.js';

import { ItemDAO } from '../dao/ItemDAO.js';

export class ItemController{
    constructor(){
        this.server = new RpcServer();
        this.itemDAO = new ItemDAO();
        this.registerHandlers();
    }

    registerHandlers() {
        this.server.on('create', this.handleCreate.bind(this));
        this.server.on('read', this.handleRead.bind(this));
        this.server.on('update', this.handleUpdate.bind(this));
        this.server.on('delete', this.handleDelete.bind(this));
    }

    handleCreate(err,params, callback){
        if(params[0].length != 0){
            this.itemDAO.create(params[0]);
            callback(null, `Item adicionado com sucesso.`);
        }
        else callback(null, `Nenhum item foi adicionado. A lista de parâmetros está vazia.`);
    }

    handleDelete(err, params, callback){
        const itemWasDeleted = this.itemDAO.deleteByName(params[0]);
        if(itemWasDeleted) callback(null,"Item deletado.");
        else callback(null, "Item não encontrado na lista.");
    }

    handleUpdate(err, params, callback){
        if(params[3] == 'true') params[3] = true;
        else if(params[3] == 'false') params[3] = false;

        let item;
        if(params[1] != null && params[1] <= 0) callback(null, `Quantidade inválida.`);
        else if(params[2] !== null && params[2] <= 0) callback(null, `Preço inválida`);
        else if(params[3].length != 0  && typeof params[3] !== "boolean") callback(null, `Disponibilidade inválida (esperado true ou false).`);
        else if(item = this.itemDAO.findItemByName(params[0])){
            this.itemDAO.update(item,params[1],params[2],params[3]);
            callback(null, `Item alterado com sucesso.`);
        }
        else callback(null, `Item não encontrado na lista.`);
    }

    handleRead(err, params, callback){
        const items = this.itemDAO.getAll();
        callback(null,items);
    }

}

const Item = new ItemController();
console.log('Servidor rodando em http://127.0.0.1:9090');