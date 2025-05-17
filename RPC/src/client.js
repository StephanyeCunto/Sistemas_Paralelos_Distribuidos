import pkg from 'xmlrpc';

const { createClient } = pkg;

const client = createClient({ host: '127.0.0.1', port: 9090, path: '/' });

let name = "agua";

client.methodCall('addItem', [name], function (error, value) {
  if (error) console.error(error);
  else console.log('Resposta do servidor:', value);
});
