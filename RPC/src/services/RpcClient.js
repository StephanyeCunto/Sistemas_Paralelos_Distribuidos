import pkg from 'xmlrpc';

const { createClient } = pkg;

export class RpcClient{
  constructor(){
    this.hostName = `192.168.122.1`; 
  }
  
  create(itemName){
    const item = [itemName];
    const client = createClient({ host: this.hostName, port: 9090, path: '/' });

    client.methodCall('create',item, function (error, value) {
      if (error) console.error(error);
      else console.log('Resposta do servidor:', value);
    });
  }

  read(){    
    const client = createClient({ host: this.hostName, port: 9090, path: '/' });

    client.methodCall('read',[],function(error,value){
      if (error) console.error(error);
      else console.log('Resposta do servidor:', value);
    });
  }

  delete(itemName){
    const client = createClient({ host: this.hostName, port: 9090, path: '/' });

    const item = [itemName];
    client.methodCall('delete',item,function(error,value){
        if (error) console.error(error);
        else console.log('Resposta do servidor:', value);
    });
  }

  update(itemName,quantity = null,price = null,isPurchased = null){
    const client = createClient({ host: this.hostName, port: 9090, path: '/' });

    const item = [itemName,quantity,price,isPurchased];
    client.methodCall('update',item, function(error,value){
      if (error) console.error(error);
      else console.log('Resposta do servidor:', value);
    });
  }
}