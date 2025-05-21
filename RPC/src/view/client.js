import pkg from 'xmlrpc';

const { createClient } = pkg;


function addItem(){
  console.log("ok ok");
  const client = createClient({ host: '127.0.0.1', port: 9090, path: '/' });

  client.methodCall('create',["agua"], function (error, value) {
    if (error) console.error(error);
    else console.log('Resposta do servidor:', value);
  });
}

window.addItem = addItem;

/*
client.methodCall('create',["agua"], function (error, value) {
  if (error) console.error(error);
  else console.log('Resposta do servidor:', value);
});

client.methodCall('read',[],function(error,value){
  if (error) console.error(error);
  else console.log('Resposta do servidor:', value);
});

client.methodCall('update',["agua",2,3.00,true], function(error,value){
    if (error) console.error(error);
    else console.log('Resposta do servidor:', value);
});

client.methodCall('delete',["agua com gas"],function(error,value){
    if (error) console.error(error);
    else console.log('Resposta do servidor:', value);
});
*/