import express from 'express';

import { RpcClient } from '../services/RpcClient.js';

import cors from 'cors';

const app = express();
const PORT = 3000;
const rpc = new RpcClient();

app.use(express.static('../view'));
app.use(cors());

app.get('/addItem', (req, res) => {
  const itemName = req.query.itemNameAdd;
  rpc.create(itemName);
  res.send('Chamando função addItem no servidor RPC '+itemName);
});

app.get('/deleteItem', (req, res) =>{
    const itemName = req.query.itemNameDelete;
    rpc.delete(itemName);
    res.send('Deletando item');
});

app.get('/updateItem', (req, res) =>{
  const itemName = req.query.itemNameUpdate;
  const itemQuantity = req.query.itemQuantity;
  const itemPrice = req.query.itemPrice;
  rpc.update(itemName, itemQuantity, itemPrice);
  res.send('Alterando item');
});

app.get('/getItems', (req, res) => {
    res.json(rpc.read());
});

app.listen(PORT, '0.0.0.0', () => {
  console.log(`Servidor disponível na rede: http://172.25.0.19:${PORT}`);
});