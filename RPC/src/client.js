import pkg from 'xmlrpc';

const { createClient } = pkg;

const client = createClient({ host: '127.0.0.1', port: 9090, path: '/' });

client.methodCall('create',  ["coisa aleatoria "], function (error, value) {
  if (error) console.error(error);
  else console.log('Resposta do servidor:', value);
});



client.methodCall('read',[],function(error,value){
  if (error) console.error(error);
  else console.log('Resposta do servidor:', value);
});

/*
client.methodCall('update',[], function(error,value){
    if (error) console.error(error);
    else console.log('Resposta do servidor:', value);
});
*/

client.methodCall('delete',["agua com gas"],function(error,value){
    if (error) console.error(error);
    else console.log('Resposta do servidor:', value);
});
