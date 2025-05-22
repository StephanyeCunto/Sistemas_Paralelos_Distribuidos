import pkg from 'xmlrpc';

const { createServer } = pkg;

export class RpcServer{
    constructor(){
        try {
            this.server = createServer({ host: '127.0.0.1', port: 9090 });
        } catch (err) {
            console.error("Erro ao iniciar o servidor RPC:", err);
        }
    }

    on(method, handler) {
        try{
            this.server.on(method, handler);
        } catch (e) {
            console.error(`Erro no handler do método ${method}:`, e);
            callback({ faultCode: -32500, faultString: 'Erro interno do servidor RPC' });
        }
    }  
}