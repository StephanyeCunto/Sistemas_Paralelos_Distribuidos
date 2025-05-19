import { RpcServer } from '../services/RpcServer.js';

import { ItemDAO } from './ItemDAO.js';

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
        this.itemDAO.create(params);
        callback(null, `Item adicionado com sucesso.`);
    }

    handleDelete(err, params, callback){
        const item = this.itemDAO.findItemByName(params[0]);
        if(item) this.itemDAO.delete(item);
        const items = this.itemDAO.getItems();
        callback(null,"Item deletado");
    }

    handleUpdate(err, params, callback){
        const item = this.itemDAO.findItemByName(params[0]);
        if(item) this.itemDAO.update(item,params[1]);
        const items = this.itemDAO.getItems();
        callback(null,"Item auterado");
    }

    handleRead(err, params, callback){
        const items = this.itemDAO.getItems();
        callback(null,items);
    }

}

const Item = new ItemController();
console.log('Servidor rodando em http://0.0.0.0:9090');