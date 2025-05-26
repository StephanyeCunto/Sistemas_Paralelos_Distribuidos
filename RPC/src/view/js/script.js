const API_BASE_URL = 'http://192.168.1.9:3000';

function loadItems() {
    const list = document.getElementById('itemList');
    list.innerHTML = '<li class="loading">Carregando itens...</li>';

    fetch(`${API_BASE_URL}/getItems`).then(response => {
        if (!response.ok) throw new Error('Servidor não encontrado');
        return response.json();
    }).then(data => {
        if (data && data.length > 0) itens = data;
        exibirItens();
    }).catch(error => {
        console.log('Usando dados locais:', error.message);
        exibirItens();
    });
}

function exibirItens() {
    const list = document.getElementById('itemList');
    list.innerHTML = '';

    if (itens.length === 0) {
        list.innerHTML = '<li>Nenhum item na lista.</li>';
        return;
    }

    itens.forEach(item => {
        const li = document.createElement('li');
        li.textContent = `${item.name || item.itemName} - Quantidade: ${item.quantity || 'N/A'}, Preço: R$${(item.price || 0).toFixed(2)}`;
        list.appendChild(li);
    });
}

function enviarDados(url, dados) {
    return fetch(`${API_BASE_URL}${url}`, {
        method: 'GET',
    }).then(response => {
        if (!response.ok) throw new Error('Servidor não disponível');
        return response.text();
    }).catch(error => {
        console.log('Processando localmente:', error.message);
        return 'Processado localmente';
    });
}

document.getElementById('addForm').addEventListener('submit', function (e) {
    e.preventDefault();
    const itemName = document.getElementById('itemNameAdd').value;

    enviarDados(`/addItem?itemNameAdd=${encodeURIComponent(itemName)}`)
        .then(response => {
            console.log('Item adicionado:', response);

            itens.push({
                name: itemName,
                quantity: 1,
                price: 0.00
            });

            exibirItens();
            document.getElementById('itemNameAdd').value = '';
        });
});

document.getElementById('deleteForm').addEventListener('submit', function (e) {
    e.preventDefault();
    const itemName = document.getElementById('itemNameDelete').value;

    enviarDados(`/deleteItem?itemNameDelete=${encodeURIComponent(itemName)}`)
        .then(response => {
            console.log('Item deletado:', response);

            itens = itens.filter(item =>
                (item.name || item.itemName).toLowerCase() !== itemName.toLowerCase()
            );

            exibirItens();
            document.getElementById('itemNameDelete').value = '';
        });
});

document.getElementById('updateForm').addEventListener('submit', function (e) {
    e.preventDefault();
    const itemName = document.getElementById('itemNameUpdate').value;
    const quantity = document.getElementById('itemQuantity').value;
    const price = document.getElementById('itemPrice').value;

    enviarDados(`/updateItem?itemNameUpdate=${encodeURIComponent(itemName)}&itemQuantity=${quantity}&itemPrice=${price}`)
        .then(response => {
            console.log('Item alterado:', response);

            const itemIndex = itens.findIndex(item =>
                (item.name || item.itemName).toLowerCase() === itemName.toLowerCase()
            );

            if (itemIndex !== -1) {
                itens[itemIndex].quantity = parseInt(quantity);
                itens[itemIndex].price = parseFloat(price);
            } else {
                itens.push({
                    name: itemName,
                    quantity: parseInt(quantity),
                    price: parseFloat(price)
                });
            }

            exibirItens();
            document.getElementById('updateForm').reset();
        });
});

document.addEventListener('DOMContentLoaded', loadItems);