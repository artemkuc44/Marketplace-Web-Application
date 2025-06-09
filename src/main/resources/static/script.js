// async function fetchProducts() {
//     const response = await fetch('/products');
//     const products = await response.json();
//     let productTable = document.getElementById("productTable");
//     productTable.innerHTML = "<tr><th>ID</th><th>Name</th><th>Price</th></tr>";
//
//     products.forEach(product => {
//         let row = productTable.insertRow();
//         row.insertCell(0).innerText = product.id;
//         row.insertCell(1).innerText = product.name;
//         row.insertCell(2).innerText = product.price;
//     });
// }
//
// async function addProduct(event) {
//     event.preventDefault(); // Prevents page reload on form submission
//
//     let name = document.getElementById("productName").value;
//     let price = document.getElementById("productPrice").value;
//
//     const response = await fetch('/products', {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json'
//         },
//         body: JSON.stringify({ name, price })
//     });
//
//     if (response.ok) {
//         fetchProducts(); // Refresh table after adding product
//     } else {
//         alert("Failed to add product!");
//     }
// }
//
// async function addUser(event){
//     event.preventDefault();
//
//     let username = document.getElementById("UserName").value;
//     let password = document.getElementById("UserPassword").value;
//
//     // Corrected username check request
//     const responseObject = await fetch(`/users/check-username?username=${username}`);
//     const userExists = await responseObject.json(); // Convert response to boolean
//
//     if (username === "") {
//         alert("Username cannot be empty.");
//         return;
//     }
//
//     if (userExists) {
//         alert("This username has already been taken");
//         return;
//     }
//
//     const responsePost = await fetch('/users', {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json'
//         },
//         body: JSON.stringify({ username, password })
//     });
//
//     if (responsePost.ok) {
//         alert("User added successfully!");
//     } else {
//         alert("Error: " + await responsePost.text());
//     }
// }
