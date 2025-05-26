import pkg from 'xmlrpc';

import { Item } from '../dto/Item.js';

const { createClient } = pkg;

export class RpcClient{
  constructor(){
    this.hostName = `192.168.1.9`; 
  }
  
  async create(itemName, quantity = null, price = null, isPurchased = null){
    const item = new Item(itemName, quantity, price, isPurchased);
    const client = createClient({ host: this.hostName, port: 9090, path: '/' });

    return new Promise((resolve, reject) => {
      client.methodCall('create',item.getItem(), function (error, value) {
      if (error) reject(error);
      else resolve(value);
    });
    });
  }

  async read() {
    const client = createClient({ host: this.hostName, port: 9090, path: '/' });

    return new Promise((resolve, reject) => {
      client.methodCall('read', [], (error, value) => {
        if (error) reject(error);
        else resolve(value);
      });
    });
  }


  async delete(itemName, quantity = null, price = null, isPurchased = null){
    const client = createClient({ host: this.hostName, port: 9090, path: '/' });
    const item = new Item(itemName,quantity, price, isPurchased);

    return new Promise((resolve, reject) => {
      client.methodCall('delete',item.getItem(),function(error,value){
        if (error) reject(error);
        else resolve(value);
      });
    });
  }

  async update(itemName, quantity = null, price = null, isPurchased = null){
    const client = createClient({ host: this.hostName, port: 9090, path: '/' });
    const item = new Item(itemName, quantity, price, isPurchased);

    return new Promise((resolve, reject) => {
      client.methodCall('update',item.getItem(), function(error,value){
          if (error) reject(error);
          else resolve(value);
      });
  });
  }
}

const client = new RpcClient();