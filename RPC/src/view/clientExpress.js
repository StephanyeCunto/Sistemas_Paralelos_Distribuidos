import express from 'express';

import { clientRPC } from '../services/RpcClient.js'

const app = express();
const PORT = 3000;
const rpc = new clientRPC();

app.use(express.static('public'));

app.get('/addItem', (req, res) => {
  rpc.addItem();
  res.send('Chamando função addItem no servidor RPC');
});

app.listen(PORT, '0.0.0.0', () => {
  console.log(`Servidor disponível na rede: http://192.168.1.8:${PORT}`);
});