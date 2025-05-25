import pkg from 'xmlrpc';

import { Item } from '../dto/Item.js';

const { createClient } = pkg;

export class RpcClient{
  constructor(){
    this.hostName = `192.168.1.9`; 
  }
  
  create(itemName, quantity = null, price = null, isPurchased = null){
    const item = new Item(itemName, quantity, price, isPurchased);
    const client = createClient({ host: this.hostName, port: 9090, path: '/' });

    client.methodCall('create',item.getItem(), function (error, value) {
      if (error) console.error(error);

      return value;
    });
  }

  read(){    
    const client = createClient({ host: this.hostName, port: 9090, path: '/' });

    client.methodCall('read',[],function(error,value){
      if (error) console.error(error);
      return value;
    });
  }

  delete(itemName, quantity = null, price = null, isPurchased = null){
    const client = createClient({ host: this.hostName, port: 9090, path: '/' });
    const item = new Item(itemName,quantity, price, isPurchased);

    client.methodCall('delete',item.getItem(),function(error,value){
        if (error) console.error(error);
        return value;

    });
  }

  update(itemName, quantity = null, price = null, isPurchased = null){
    const client = createClient({ host: this.hostName, port: 9090, path: '/' });
    const item = new Item(itemName, quantity, price, isPurchased);

    client.methodCall('update',item.getItem(), function(error,value){
      if (error) console.error(error);
      return value;
    });
  }
}

const client = new RpcClient();