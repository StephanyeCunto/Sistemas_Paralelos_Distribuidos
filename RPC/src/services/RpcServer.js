import pkg from 'xmlrpc';

const { createServer } = pkg;

export class RpcServer{
    constructor(){
        this.server = createServer({ host: '0.0.0.0', port: 9090 });
    }

    on(method, handler) {
        this.server.on(method, handler);
    }

}