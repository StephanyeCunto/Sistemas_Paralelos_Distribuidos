import express from 'express';

import { RpcClient } from '../services/RpcClient.js'

const app = express();
const PORT = 3000;
const rpc = new RpcClient();

app.use(express.static('../view'));

app.get('/addItem', (req, res) => {
  const itemName = req.query.itemName;
  rpc.update(itemName,null,3);
  res.send('Chamando função addItem no servidor RPC '+itemName);
});

app.listen(PORT, '0.0.0.0', () => {
  console.log(`Servidor disponível na rede: http://192.168.1.204:${PORT}`);
});