import pkg from 'xmlrpc';
import fs from 'fs'; 

const { createServer } = pkg;

export class serve{
    constructor(){
        this.server = createServer({ host: '0.0.0.0', port: 9090 });
    }

    addItem(){
        this.server.on('addItem', function(err, params, callback) {
            const nome = params[0];
            this.saveJson(nome);
            console.log('Item adicionado:', nome);
            callback(null, `Item ${nome} adicionado com sucesso.`);
        });

    }

    saveJson(item){
        const listItens = this.searchJson();
        listItens.push(item);
        fs.writeFileSync("listItens.json" , JSON.stringify(listItens), 'utf8');
    }

    searchJson(){
        const listItens = fs.readFile("listItens.json", 'utf8');
        return JSON.parse(listItens);
    }
}