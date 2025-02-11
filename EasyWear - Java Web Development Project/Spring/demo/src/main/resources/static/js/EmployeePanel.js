'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
var subscriptions = {};
var stompClient = null;
var subscription=null;
var username = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function subscribeToDestination(destination, callback) {
    if (isSubscribed(destination)) {
        subscription = stompClient.subscribe(destination, callback);
        subscriptions[destination] = subscription;
        console.log("the subscirition equals to!!!!!!!!!!!!!!!!!!!!!!! = " ,subscriptions[destination] );
        console.log(`Subscribed to ${destination}`);
    } else {
        console.log(`Already subscribed to ${destination}`);
    }
}
function isSubscribed(destination) {
    if(subscriptions[destination]==undefined){
        console.log("the subscirition equals to = " ,subscriptions[destination] );
        return true;
    }
    else 
    console.log("the subscirition equals to = " ,subscriptions[destination] );
        return false;
}
function unsubscribeFromDestination(destination) {
    if (isSubscribed(destination)) {
        subscriptions[destination].unsubscribe();
        delete subscriptions[destination];
        console.log(`Unsubscribed from ${destination}`);
    } else {
        console.log(`Not subscribed to ${destination}`);
    }
}

function connect(event) {
    username = document.querySelector('#name').value.trim();

    if(username) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.debug = function(str) {
            console.log("STOMP: " + str);
        };

        stompClient.connect({}, onConnected, function(error) {
            console.error("Connection error:", error);
            alert("Could not connect to the server. Make sure the channel is available.");
        });

    }
    event.preventDefault();
}
var userBranches = {}
var targetBranch = {}

function onConnected() {
    targetBranch[username] = document.querySelector('#targetBranch').value.trim();
    console.log("function on connect !!");
    console.log(username);    
    try {
        stompClient.send("/app/chat.getUserBranch", {}, JSON.stringify({sender: username}));
        console.log("Sent request for user branch");
    } catch (error) {
        console.error("Error sending getUserBranch request:", error);
    }   
    stompClient.send("/app/chat.addUser", {}, JSON.stringify({sender: username, type: 'JOIN'}));

    const branchSubscription = stompClient.subscribe('/topic/branch', function(response) {
        try {
            const chatMessage = JSON.parse(response.body);
            const branchData = chatMessage.senderBranch;
            console.log("chat messege is " + chatMessage);
            if (branchData) {
                if (!Object.values(userBranches).includes(branchData)) 
                    {userBranches[username] = branchData;}
                else 
                    {branchSubscription.unsubscribe();}
                console.log("Branch received for user " + username + ": " + userBranches[username]);
                const destination = `/topic/${userBranches[username]}`;
                branchSubscription.unsubscribe();
                subscribeToDestination(destination, function(response) {
                    console.log("Message received from /topic/:", response.body);
                    onMessageReceived(response);
                });
            }
            else{
                branchSubscription.unsubscribe();
            }
        } catch (error) {
            console.error("Error in branch subscription:", error);
            alert("Channel is already occupied. Please try again later.");
        }
    });
    connectingElement.classList.add('hidden');
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();
     
    if (!userBranches[username]) {
        alert("you now in queue , please wait.");
        return;
    }
    if (messageContent && targetBranch && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageContent,
            senderBranch: userBranches[username], 
            targetBranch: targetBranch[username],
            type: 'CHAT'
        };
        stompClient.send("/app/chat.sendToBranch", {}, JSON.stringify(chatMessage));
        // stompClient.send("/app/chat.sendToBranch", {}, JSON.stringify(chatOwnMessage));
        // onMessageReceived(chatMessage);
        messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    console.log("messege receive !!!!!!!!!! "+ message);
    var messageElement = document.createElement('li');
    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.senderBranch + ' joined!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}
 usernameForm.addEventListener('submit', connect, true)
 messageForm.addEventListener('submit', sendMessage, true)
 document.getElementById('exitButton').addEventListener('click', function() {
    if (stompClient && userBranches[username]) {
        try {
            stompClient.send("/app/chat.endChat", {}, JSON.stringify({
                sender: username,
                senderBranch: userBranches[username],
                targetBranch: targetBranch[username],
                type: 'LEAVE'
            }));
            
            const destination = `/topic/${userBranches[username]}`;
            if (subscriptions[destination]) {
                subscriptions[destination].unsubscribe();
                delete subscriptions[destination];
                delete userBranches[username];
                console.log(`Unsubscribed from ${destination}`);
            }
        } catch (error) {
            console.error("Error during chat exit:", error);
        }
    }
    document.getElementById('chat-page').classList.add('hidden');
    document.getElementById('username-page').classList.remove('hidden');
    document.getElementById('messageArea').innerHTML = '';
});    
    
    // Function to show the specific tab by its ID
    function showTab(tabId) {
        const tabs = document.querySelectorAll('.tab-content');
        tabs.forEach(tab => {
            tab.style.display = 'none'; // Hide all tabs
        }); 
        const activeTab = document.getElementById(tabId);
        if (activeTab) {
            activeTab.style.display = 'block'; // Show the active tab
        }
    }
    window.onload = () => {
        const tabs = document.querySelectorAll('.tab-content');
        tabs.forEach(tab => {
            tab.style.display = 'none'; // Initially hide all tabs
        });
        // Clear Screen
        showTab('none');
    };

    // Logout function
    function logout() {
        fetch('/api/v1/logout', {
            method: 'POST'
        })
        .then(response => response.json())
        .then(data => {
            alert('Logged out successfully');
            window.location.href = '/login.html';  // Redirect to the login page
        })
        .catch(error => console.error('Logout error:', error));
    }

        
        // Inventory Functions /////////////////////////////////////////////
        // View All Inventory
        function viewInventory() {
            fetch('/api/v1/inventory')
                .then(response => response.json())
                .then(data => {
                    const inventoryTableBody = document.getElementById('inventoryTable').getElementsByTagName('tbody')[0];
                    inventoryTableBody.innerHTML = ''; // Clear any existing rows in the table body

                    data.forEach(item => {
                        // Create a new row for each inventory item
                        const row = document.createElement('tr');

                        // Create and append cells for each property of the inventory item
                        const transactionIdCell = document.createElement('td');
                        transactionIdCell.textContent = item.inventoryId;
                        row.appendChild(transactionIdCell);

                        const productNameCell = document.createElement('td');
                        productNameCell.textContent = item.productName;
                        row.appendChild(productNameCell);

                        const quantityCell = document.createElement('td');
                        quantityCell.textContent = item.quantity;
                        row.appendChild(quantityCell);

                        const priceCell = document.createElement('td');
                        priceCell.textContent = item.price;
                        row.appendChild(priceCell);

                        const categoryCell = document.createElement('td');
                        categoryCell.textContent = item.productCategory;
                        row.appendChild(categoryCell);

                        const branchCell = document.createElement('td');
                        branchCell.textContent = item.branch;
                        row.appendChild(branchCell);

                        // Append the newly created row to the table body
                        inventoryTableBody.appendChild(row);
                    });
                })
                .catch(error => console.error('Error fetching inventory:', error));
            }

        // Get Inventory by Inventory Branch logic
        document.getElementById('getInventoryByBranchForm').addEventListener('submit', function(event) {
            event.preventDefault(); // Prevent form from submitting the traditional way

            // Get the branch input value from the form
            const branch = document.getElementById('branchInput').value;

            // Fetch inventory items by the provided branch
            fetch(`/api/v1/inventory/branch/${branch}`)
                .then(response => response.json())
                .then(data => {
                    const inventoryTableBody = document.getElementById('inventoryTable').getElementsByTagName('tbody')[0];
                    inventoryTableBody.innerHTML = ''; // Clear any existing rows in the table body

                    // Check if data is returned
                    if (data.length === 0) {
                        alert('No inventory found for the given branch.');
                        return;
                    }

                    // Populate the table with the fetched inventory data
                    data.forEach(item => {
                        const row = document.createElement('tr');

                        const transactionIdCell = document.createElement('td');
                        transactionIdCell.textContent = item.inventoryId;
                        row.appendChild(transactionIdCell);

                        const productNameCell = document.createElement('td');
                        productNameCell.textContent = item.productName;
                        row.appendChild(productNameCell);

                        const quantityCell = document.createElement('td');
                        quantityCell.textContent = item.quantity;
                        row.appendChild(quantityCell);

                        const priceCell = document.createElement('td');
                        priceCell.textContent = item.price;
                        row.appendChild(priceCell);

                        const categoryCell = document.createElement('td');
                        categoryCell.textContent = item.productCategory;
                        row.appendChild(categoryCell);

                        const branchCell = document.createElement('td');
                        branchCell.textContent = item.branch;
                        row.appendChild(branchCell);

                        // Append the row to the table body
                        inventoryTableBody.appendChild(row);
                    });
                })
                .catch(error => console.error('Error fetching inventory by branch:', error));
        });

        // Get Inventory by Inventory Product Name logic
        document.getElementById('getInventoryByProductForm').addEventListener('submit', function(event) {
            event.preventDefault(); // Prevent form from submitting the traditional way

            // Get the Product Name input value from the form
            const productName = document.getElementById('ProductNameInput').value;

            // Fetch inventory items by the provided Product Name
            fetch(`/api/v1/inventory/productName/${productName}`)
                .then(response => response.json())
                .then(data => {
                    const inventoryTableBody = document.getElementById('inventoryTable').getElementsByTagName('tbody')[0];
                    inventoryTableBody.innerHTML = ''; // Clear any existing rows in the table body

                    // Check if data is returned
                    if (data.length === 0) {
                        alert('No inventory found for the given Product Name.');
                        return;
                    }

                    // Populate the table with the fetched inventory data
                    data.forEach(item => {
                        const row = document.createElement('tr');

                        const transactionIdCell = document.createElement('td');
                        transactionIdCell.textContent = item.inventoryId;
                        row.appendChild(transactionIdCell);

                        const productNameCell = document.createElement('td');
                        productNameCell.textContent = item.productName;
                        row.appendChild(productNameCell);

                        const quantityCell = document.createElement('td');
                        quantityCell.textContent = item.quantity;
                        row.appendChild(quantityCell);

                        const priceCell = document.createElement('td');
                        priceCell.textContent = item.price;
                        row.appendChild(priceCell);

                        const categoryCell = document.createElement('td');
                        categoryCell.textContent = item.productCategory;
                        row.appendChild(categoryCell);

                        const branchCell = document.createElement('td');
                        branchCell.textContent = item.branch;
                        row.appendChild(branchCell);

                        // Append the row to the table body
                        inventoryTableBody.appendChild(row);
                    });
                })
                .catch(error => console.error('Error fetching inventory by Product Name:', error));
        });

        // Get Inventory by Inventory Product Category logic
        document.getElementById('getInventoryByProductCategoryForm').addEventListener('submit', function(event) {
            event.preventDefault(); // Prevent form from submitting the traditional way

            // Check if the submit event is being triggered
            console.log("Form submitted");

            // Get the Product Category input value from the form
            const productCategory = document.getElementById('ProductCategoryInput').value;

            // Log the category to verify the input value is being retrieved correctly
            console.log(`Product Category: ${productCategory}`);

            // Fetch inventory items by the provided Product Category
            fetch(`/api/v1/inventory/productCategory/${productCategory}`)
                .then(response => response.json())
                .then(data => {
                    const inventoryTableBody = document.getElementById('inventoryTable').getElementsByTagName('tbody')[0];
                    inventoryTableBody.innerHTML = ''; // Clear any existing rows in the table body

                    // Check if data is returned
                    if (data.length === 0) {
                        alert('No inventory found for the given Product Category.');
                        return;
                    }

                    // Populate the table with the fetched inventory data
                    data.forEach(item => {
                        const row = document.createElement('tr');

                        const transactionIdCell = document.createElement('td');
                        transactionIdCell.textContent = item.inventoryId;
                        row.appendChild(transactionIdCell);

                        const productNameCell = document.createElement('td');
                        productNameCell.textContent = item.productName;
                        row.appendChild(productNameCell);

                        const quantityCell = document.createElement('td');
                        quantityCell.textContent = item.quantity;
                        row.appendChild(quantityCell);

                        const priceCell = document.createElement('td');
                        priceCell.textContent = item.price;
                        row.appendChild(priceCell);

                        const categoryCell = document.createElement('td');
                        categoryCell.textContent = item.productCategory;
                        row.appendChild(categoryCell);

                        const branchCell = document.createElement('td');
                        branchCell.textContent = item.branch;
                        row.appendChild(branchCell);

                        // Append the row to the table body
                        inventoryTableBody.appendChild(row);
                    });
                })
                .catch(error => console.error('Error fetching inventory by Product Category:', error));
        });

        // Add Inventory    
        document.getElementById('addInventoryForm').addEventListener('submit', function(event) {
            event.preventDefault();

            console.log("Form submitted"); // Log to see if the form submission is triggering
            // Get values from form fields
            const productName = document.getElementById('productName').value;
            const quantity = document.getElementById('inventoryQuantity').value;
            const price = document.getElementById('inventoryPrice').value;
            const productCategory = document.getElementById('productCategory').value;
            const branch = document.getElementById('branch').value;

            // Create item object to send in POST req
            const item = { productName, quantity, price, productCategory, branch};

            // Send POST request to create new inventory item
            fetch('/api/v1/inventory', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(item)
            })
            .then(response => response.json())
            .then(data => {
                alert('Inventory item added successfully');
                document.getElementById('addInventoryForm').reset();
                viewInventory();  // Refresh inventory list after adding
            })
            .catch(error => console.error('Error adding inventory:', error));
        });


        // Delete Inventory logic
        document.getElementById('deleteInventoryForm').addEventListener('submit', function(event) {
            event.preventDefault();
            const inventoryId = document.getElementById('inventoryId').value;

            fetch(`/api/v1/inventory/${inventoryId}`, {
                method: 'DELETE',
                headers: { 'Content-Type': 'application/json' },
            })
            .then(response => {
                if (response.ok) {
                    alert('inventory deleted successfully');
                    document.getElementById('deleteInventoryForm').reset();
                    viewInventory(); // Reload the employee list after deletion
                } else {
                    alert('Error deleting employee');
                }
            })
            .catch(error => console.error('Error deleting Inventory:', error));
        });


        // Customers Functions /////////////////////////////////////////////
        // View Customers
        function viewCustomers() {
            fetch('/api/v1/customers')
                .then(response => response.json())
                .then(data => {
                    const customersTableBody = document.getElementById('customersTable').getElementsByTagName('tbody')[0];
                    customersTableBody.innerHTML = ''; // Clear any existing rows

                    data.forEach(customer => {
                        const row = document.createElement('tr');

                        const idCell = document.createElement('td');
                        idCell.textContent = customer.customerId;
                        row.appendChild(idCell);

                        const fullNameCell = document.createElement('td');
                        fullNameCell.textContent = customer.fullName;
                        row.appendChild(fullNameCell);

                        const idNumberCell = document.createElement('td');
                        idNumberCell.textContent = customer.idNumber;
                        row.appendChild(idNumberCell);

                        const phoneCell = document.createElement('td');
                        phoneCell.textContent = customer.phone;
                        row.appendChild(phoneCell);

                        const customerTypeCell = document.createElement('td');
                        customerTypeCell.textContent = customer.customerType;
                        row.appendChild(customerTypeCell);

                        customersTableBody.appendChild(row);
                    });
                })
                .catch(error => console.error('Error fetching customers:', error));
        }

        // Add Customer
        document.getElementById('addCustomerForm').addEventListener('submit', function(event) {
            event.preventDefault();

            console.log("Form submitted");

            const fullName = document.getElementById('customerFullName').value;
            const idNumber = document.getElementById('customerIdNumber').value;
            const phone = document.getElementById('customerPhone').value;
            const customerType = document.getElementById('customerType').value;

            const customer = { fullName, idNumber, phone, customerType };

            fetch('/api/v1/customers', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(customer)
            })
            .then(response => response.json())
            .then(data => {
                alert('Customer added successfully');
                document.getElementById('addCustomerForm').reset();
                viewCustomers(); // Refresh customers list
            })
            .catch(error => console.error('Error adding customer:', error));
        });


        // Delete Customer logic
        document.getElementById('deleteCustomerForm').addEventListener('submit', function(event) {
            event.preventDefault();
            const customerId = document.getElementById('customerIdToDelete').value; // Assuming this is the input field to enter customer ID to delete

            fetch(`/api/v1/customers/${customerId}`, {
                method: 'DELETE',
                headers: { 'Content-Type': 'application/json' },
            })
            .then(response => {
                if (response.ok) {
                    alert('Customer deleted successfully');
                    document.getElementById('deleteCustomerForm').reset();
                    viewCustomers(); // Reload the customer list after deletion
                } else {
                    alert('Error deleting customer');
                }
            })
            .catch(error => console.error('Error deleting customer:', error));
        });


        // Sales Functions /////////////////////////////////////////////
        // View Sales
        function viewSales() {
            fetch('/api/v1/sales')
                .then(response => response.json())
                .then(data => {
                    const salesTableBody = document.getElementById('salesTable').getElementsByTagName('tbody')[0];
                    salesTableBody.innerHTML = ''; // Clear any existing rows

                    data.forEach(sale => {
                        const row = document.createElement('tr');

                        const transactionIdCell = document.createElement('td');
                        transactionIdCell.textContent = sale.id;
                        row.appendChild(transactionIdCell);

                        const customerNameCell = document.createElement('td');
                        customerNameCell.textContent = sale.customerName;
                        row.appendChild(customerNameCell);

                        const customerTypeCell = document.createElement('td');
                        customerTypeCell.textContent = sale.customerType;
                        row.appendChild(customerTypeCell);

                        const productNameCell = document.createElement('td');
                        productNameCell.textContent = sale.productName;
                        row.appendChild(productNameCell);

                        const productCategoryCell = document.createElement('td');
                        productCategoryCell.textContent = sale.productCategory;
                        row.appendChild(productCategoryCell);

                        const quantityCell = document.createElement('td');
                        quantityCell.textContent = sale.quantity;
                        row.appendChild(quantityCell);

                        const priceCell = document.createElement('td');
                        priceCell.textContent = sale.price;
                        row.appendChild(priceCell);

                        const branchCell = document.createElement('td');
                        branchCell.textContent = sale.branch;
                        row.appendChild(branchCell);

                        salesTableBody.appendChild(row);
                    });
                })
                .catch(error => console.error('Error fetching sales:', error));
        }

        // Add Sale
        document.getElementById('addSalesForm').addEventListener('submit', function(event) {
            event.preventDefault();

            const customerName = document.getElementById('SalesCustomerName').value;
            const customerType = document.getElementById('SalesCustomerType').value;
            const productName = document.getElementById('SalesProductName').value;
            const productCategory = document.getElementById('SalesProductCategory').value;
            const quantity = document.getElementById('SalesQuantity').value;
            const price = document.getElementById('SalesPrice').value;
            const branch = document.getElementById('SalesBranch').value;

            const sale = { customerName, customerType, productName, productCategory, quantity, price, branch };

            fetch('/api/v1/sales', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(sale)
            })
            .then(response => response.json())
            .then(data => {
                alert('Sale added successfully');
                document.getElementById('addSalesForm').reset();
                viewSales(); // Refresh sales list
            })
            .catch(error => console.error('Error adding sale:', error));
        });


        // Handle the Buy Product form submission
        document.getElementById('buyProductForm').addEventListener('submit', function(event) {
            console.log('Form submitted!');
            event.preventDefault();

            // Get the values from the form fields
            const customerId = document.getElementById('BuycustomerId').value;
            const productName = document.getElementById('BuyproductName').value;
            const quantity = document.getElementById('Buyquantity').value;

            console.log('Customer ID:', customerId);
            console.log('Product Name:', productName);
            console.log('Quantity:', quantity);
            // Construct the query string with customerId, productName, and quantity
            const queryString = `?customerId=${customerId}&productName=${encodeURIComponent(productName)}&quantity=${quantity}`;

            console.log('Sending product name:', productName);
            console.log('Query string:', queryString);
            console.log('Sending request to URL: /api/v1/sales/buy' + queryString);
            // Send a POST request to the server with the query parameters
            fetch(`/api/v1/sales/buy${queryString}`, {
                method: 'POST', // We still use POST for creating a sale, but send parameters via GET.
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error making purchase');
                }
                return response.json();
            })
            .then(data => {
                // Handle successful purchase
                alert('Purchase successful!');
                document.getElementById('buyProductForm').reset();
                viewSales(); 
            })
            .catch(error => {
                viewSales(); 
                // Handle error
                // console.error('Error making purchase:', error);
                // alert('Error processing the purchase');
            });
        });

        // Get Sales by Branch logic
        document.getElementById('getSalesByBranchForm').addEventListener('submit', function(event) {
            event.preventDefault(); // Prevent form from submitting the traditional way

            // Get the branch input value from the form
            const branch = document.getElementById('SalesbranchInput').value;

            // Fetch sales items by the provided branch
            fetch(`/api/v1/sales/branch/${branch}`)
                .then(response => response.json())
                .then(data => {
                    const salesTableBody = document.getElementById('salesTable').getElementsByTagName('tbody')[0];
                    salesTableBody.innerHTML = ''; // Clear any existing rows in the table body
                    console.log("Fetched sales data:", data);

                    // Check if data is returned
                    if (data.length === 0) {
                        alert('No sales found for the given branch.');
                        return;
                    }

                    // Populate the table with the fetched sales data
                    data.forEach(item => {
                        const row = document.createElement('tr');

                        // Transaction ID
                        const transactionIdCell = document.createElement('td');
                        transactionIdCell.textContent = item.id;  
                        row.appendChild(transactionIdCell);

                        // Customer Name
                        const customerNameCell = document.createElement('td');
                        customerNameCell.textContent = item.customerName;  
                        row.appendChild(customerNameCell);

                        // Customer Type
                        const customerTypeCell = document.createElement('td');
                        customerTypeCell.textContent = item.customerType;  
                        row.appendChild(customerTypeCell);

                        // Product Name
                        const productNameCell = document.createElement('td');
                        productNameCell.textContent = item.productName; 
                        row.appendChild(productNameCell);

                        // Product Category
                        const productCategoryCell = document.createElement('td');
                        productCategoryCell.textContent = item.productCategory;  
                        row.appendChild(productCategoryCell);

                        // Quantity
                        const quantityCell = document.createElement('td');
                        quantityCell.textContent = item.quantity;  
                        row.appendChild(quantityCell);

                        // Price (check for proper field mapping)
                        const priceCell = document.createElement('td');
                        if (item.price !== undefined && item.price !== null) {
                            priceCell.textContent = item.price;  
                        } else {
                            priceCell.textContent = 'N/A';  // Handle case where price is missing
                        }
                        row.appendChild(priceCell);

                        // Branch (check for proper field mapping)
                        const branchCell = document.createElement('td');
                        if (item.branch !== undefined && item.branch !== null) {
                            branchCell.textContent = item.branch;  // Correct field
                        } else {
                            branchCell.textContent = 'N/A';  // Handle case where branch is missing
                        }
                        row.appendChild(branchCell);

                        // Append the row to the table body
                        salesTableBody.appendChild(row);
                    });
                })
                .catch(error => console.error('Error fetching sales by branch:', error));
        });


        // Get Sales by Product Name logic
        document.getElementById('getSalesByProductForm').addEventListener('submit', function(event) {
            event.preventDefault(); // Prevent form from submitting the traditional way

            // Get the Product Name input value from the form
            const productName = document.getElementById('SalesProductNameInput').value;

            // Fetch sales items by the provided Product Name
            fetch(`/api/v1/sales/productName/${productName}`)
                .then(response => response.json())
                .then(data => {
                    const salesTableBody = document.getElementById('salesTable').getElementsByTagName('tbody')[0];
                    salesTableBody.innerHTML = ''; // Clear any existing rows in the table body

                    // Check if data is returned
                    if (data.length === 0) {
                        alert('No sales found for the given branch.');
                        return;
                    }

                    // Populate the table with the fetched sales data
                    data.forEach(item => {
                        const row = document.createElement('tr');

                        // Transaction ID
                        const transactionIdCell = document.createElement('td');
                        transactionIdCell.textContent = item.id;  
                        row.appendChild(transactionIdCell);

                        // Customer Name
                        const customerNameCell = document.createElement('td');
                        customerNameCell.textContent = item.customerName;  
                        row.appendChild(customerNameCell);

                        // Customer Type
                        const customerTypeCell = document.createElement('td');
                        customerTypeCell.textContent = item.customerType;  
                        row.appendChild(customerTypeCell);

                        // Product Name
                        const productNameCell = document.createElement('td');
                        productNameCell.textContent = item.productName; 
                        row.appendChild(productNameCell);

                        // Product Category
                        const productCategoryCell = document.createElement('td');
                        productCategoryCell.textContent = item.productCategory;  
                        row.appendChild(productCategoryCell);

                        // Quantity
                        const quantityCell = document.createElement('td');
                        quantityCell.textContent = item.quantity;  
                        row.appendChild(quantityCell);

                        // Price (check for proper field mapping)
                        const priceCell = document.createElement('td');
                        if (item.price !== undefined && item.price !== null) {
                            priceCell.textContent = item.price;  
                        } else {
                            priceCell.textContent = 'N/A';  // Handle case where price is missing
                        }
                        row.appendChild(priceCell);

                        // Branch (check for proper field mapping)
                        const branchCell = document.createElement('td');
                        if (item.branch !== undefined && item.branch !== null) {
                            branchCell.textContent = item.branch;  // Correct field
                        } else {
                            branchCell.textContent = 'N/A';  // Handle case where branch is missing
                        }
                        row.appendChild(branchCell);

                        // Append the row to the table body
                        salesTableBody.appendChild(row);
                    });
                })
                .catch(error => console.error('Error fetching sales by product name:', error));
        });


        // Get Sales by Product Category logic
        document.getElementById('getSalesByProductCategoryForm').addEventListener('submit', function(event) {
            event.preventDefault(); // Prevent form from submitting the traditional way

            // Get the Product Category input value from the form
            const productCategory = document.getElementById('SalesProductCategoryInput').value;

            // Fetch sales items by the provided Product Category
            fetch(`/api/v1/sales/productCategory/${productCategory}`)
                .then(response => response.json())
                .then(data => {
                    const salesTableBody = document.getElementById('salesTable').getElementsByTagName('tbody')[0];
                    salesTableBody.innerHTML = ''; // Clear any existing rows in the table body

                    // Check if data is returned
                    if (data.length === 0) {
                        alert('No sales found for the given branch.');
                        return;
                    }

                    // Populate the table with the fetched sales data
                    data.forEach(item => {
                        const row = document.createElement('tr');

                        // Transaction ID
                        const transactionIdCell = document.createElement('td');
                        transactionIdCell.textContent = item.id;  
                        row.appendChild(transactionIdCell);

                        // Customer Name
                        const customerNameCell = document.createElement('td');
                        customerNameCell.textContent = item.customerName;  
                        row.appendChild(customerNameCell);

                        // Customer Type
                        const customerTypeCell = document.createElement('td');
                        customerTypeCell.textContent = item.customerType;  
                        row.appendChild(customerTypeCell);

                        // Product Name
                        const productNameCell = document.createElement('td');
                        productNameCell.textContent = item.productName; 
                        row.appendChild(productNameCell);

                        // Product Category
                        const productCategoryCell = document.createElement('td');
                        productCategoryCell.textContent = item.productCategory;  
                        row.appendChild(productCategoryCell);

                        // Quantity
                        const quantityCell = document.createElement('td');
                        quantityCell.textContent = item.quantity;  
                        row.appendChild(quantityCell);

                        // Price (check for proper field mapping)
                        const priceCell = document.createElement('td');
                        if (item.price !== undefined && item.price !== null) {
                            priceCell.textContent = item.price;  
                        } else {
                            priceCell.textContent = 'N/A';  // Handle case where price is missing
                        }
                        row.appendChild(priceCell);

                        // Branch (check for proper field mapping)
                        const branchCell = document.createElement('td');
                        if (item.branch !== undefined && item.branch !== null) {
                            branchCell.textContent = item.branch;  // Correct field
                        } else {
                            branchCell.textContent = 'N/A';  // Handle case where branch is missing
                        }
                        row.appendChild(branchCell);

                        // Append the row to the table body
                        salesTableBody.appendChild(row);
                    });
                })
                .catch(error => console.error('Error fetching sales by product category:', error));
        });




        // Delete Sale logic
        document.getElementById('deleteSalesForm').addEventListener('submit', function(event) {
            event.preventDefault();
            const saleId = document.getElementById('saleIdToDelete').value; // Assuming this is the input field to enter sale ID to delete

            fetch(`/api/v1/sales/${id}`, {
                method: 'DELETE',
                headers: { 'Content-Type': 'application/json' },
            })
            .then(response => {
                if (response.ok) {
                    alert('Sale deleted successfully');
                    document.getElementById('deleteSalesForm').reset();
                    viewSales(); // Reload the sales list after deletion
                } else {
                    alert('Error deleting sale');
                }
            })
            .catch(error => console.error('Error deleting sale:', error));
        });
