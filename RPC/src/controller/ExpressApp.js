import express from 'express';

import { RpcClient } from '../services/RpcClient.js';

import cors from 'cors';

const app = express();
const PORT = 3000;
const rpc = new RpcClient();

app.use(express.static('../view'));
app.use(cors());

app.get('/addItem',async (req, res) => {
  const itemName = req.query.itemNameAdd;
  const resAdd = await rpc.create(itemName);
  res.send(resAdd);
});

app.get('/deleteItem', async (req, res) =>{
    const itemName = req.query.itemNameDelete;
    const resDelete = await rpc.delete(itemName);
    res.send(resDelete);
});

app.get('/updateItem', async (req, res) =>{
  const itemName = req.query.itemNameUpdate;
  const itemQuantity = req.query.itemQuantity;
  const itemPrice = req.query.itemPrice;
  const resUpdate = await rpc.update(itemName, itemQuantity, itemPrice);
  res.send(resUpdate);
});

app.get('/getItems', async (req, res) => {
    const items = await rpc.read();
    res.json(items);
});


app.listen(PORT, '0.0.0.0', () => {
  console.log(`Servidor disponível na rede: http://172.25.0.19:${PORT}`);
});