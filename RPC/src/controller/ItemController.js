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
            return callback(null, `Item adicionado com sucesso.`);
        }
        return callback(null, `Nenhum item foi adicionado. A lista de parâmetros está vazia.`);
    }

    handleDelete(err, params, callback){
        const itemWasDeleted = this.itemDAO.deleteByName(params[0]);
        if(itemWasDeleted) return callback(null,`Item deletado.`);
        return callback(null, `Item não encontrado na lista.`);
    }

    handleUpdate(err, params, callback) {
        const item = this.itemDAO.findItemByName(params[0]);
        if (item) {
            if (params[1] != null) if (typeof params[1] !== "number" || params[1] <= 0) return callback(null, `Quantidade inválida.`);
            if (params[2] != null) if (typeof params[2] !== "number" || params[2] <= 0) return callback(null, `Preço inválido.`);
            if (params[3] != null) if (typeof params[3] !== "boolean") return callback(null, `Disponibilidade inválida (esperado true ou false).`);

            this.itemDAO.update(item, params[1], params[2], params[3]);
            return callback(null, `Item alterado com sucesso.`);
        }

        return callback(null, `Item não encontrado na lista.`);
    }


    handleRead(err, params, callback){
        const items = this.itemDAO.getAll();
        callback(null,items);
    }

}

const Item = new ItemController();