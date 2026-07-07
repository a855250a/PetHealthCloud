console.log("script.js loaded");

let  editPetId = null;

// =========================
// Login
// =========================

const loginButton = document.getElementById("loginButton");

console.log(loginButton);

if (loginButton) {

    loginButton.addEventListener("click", function () {

        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        console.log("login button clicked");
        console.log(email);
        console.log(password);

        fetch("/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email: email,
                password: password
            })
        })
            .then(response => response.json())
            .then(data => {

                console.log("Login Response:", data);

                localStorage.setItem("token", data.token);

                console.log("Token Saved");

                window.location.href = "/dashboard.html";

            });

    });

}


// =========================
// Dashboard
// =========================

const loadPetsButton = document.getElementById("loadPetsButton");

if (loadPetsButton) {

    loadPetsButton.addEventListener("click", function () {

        console.log("Load My Pets Clicked");

        const token = localStorage.getItem("token");

        console.log("Token:", token);

        fetch("/pets", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            }
        })
            .then(response => response.json())
            .then(data => {

                console.log("Pets:", data);

                const petList = document.getElementById("petList");

                petList.innerHTML = "";

                data.forEach(function (pet) {

                    petList.innerHTML += `
                <div class="pet-item">
                    <p><strong>${pet.name}</strong></p>
                    <p>Age: ${pet.age}</p>
                    <p>Weight: ${pet.weight} kg</p>
                    <p>Vaccine: ${pet.vaccine ?? "-"}</p>
            
                    <div class="pet-actions">
            
                        <button
                            class="editPetButton"
                            data-id="${pet.id}">
                            Edit
                        </button>
            
                        <button
                            class="deletePetButton"
                            data-id="${pet.id}">
                            Delete
                        </button>
            
                    </div>
            
                    <hr>
                </div>
            `;

                });

                // 綁定 Delete Button
                const deleteButtons = document.querySelectorAll(".deletePetButton");

                deleteButtons.forEach(function (button) {

                    button.addEventListener("click", function () {

                        const petId = button.dataset.id;

                        console.log("Delete Pet:", petId);

                        const token = localStorage.getItem("token");

                        fetch("/pets/" + petId, {
                            method: "DELETE",
                            headers: {
                                "Authorization": "Bearer " + token
                            }
                        })
                            .then(response => {

                                console.log("Delete Status:", response.status);

                                if (response.ok) {

                                    loadPetsButton.click();

                                } else {

                                    alert("Delete Failed");

                                }

                            });

                    });

                });


// =========================
// 綁定 Edit Button
// =========================

                const editButtons = document.querySelectorAll(".editPetButton");

                editButtons.forEach(function (button) {

                    button.addEventListener("click", function () {

                        const petId = Number(button.dataset.id);

                        const pet = data.find(function (item) {
                            return item.id === petId;
                        });

                        console.log("Edit Pet:", pet);

                        editPetId = pet.id;

                        document.getElementById("petName").value = pet.name;
                        document.getElementById("petAge").value = pet.age;
                        document.getElementById("petWeight").value = pet.weight;
                        document.getElementById("petVaccine").value = pet.vaccine ?? "";

                        addPetButton.textContent = "Update Pet";

                    });

                });

            });

    });

}


// =========================
// Add Pet
// =========================

const addPetButton = document.getElementById("addPetButton");

if (addPetButton) {

    addPetButton.addEventListener("click", function () {

        const petName = document.getElementById("petName").value;
        const petAge = document.getElementById("petAge").value;
        const petWeight = document.getElementById("petWeight").value;
        const petVaccine = document.getElementById("petVaccine").value;

        console.log("Add Pet Clicked");
        console.log("Name:", petName);
        console.log("Age:", petAge);
        console.log("Weight:", petWeight);
        console.log("Vaccine:",petVaccine);

        const token = localStorage.getItem("token");

        const url = editPetId === null
            ? "/pets"
            : "/pets/" + editPetId;

        const method = editPetId === null
            ? "POST"
            : "PUT";

        fetch(url, {
            method: method,
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify({
                name: petName,
                age: Number(petAge),
                weight: Number(petWeight),
                vaccine:petVaccine
            })
        })
            .then(response => response.json())
            .then(data => {

                console.log("Add Pet Response:", data);

                //回復新增模式
                editPetId = null;

                document.getElementById("petName").value = "";
                document.getElementById("petAge").value = "";
                document.getElementById("petWeight").value = "";
                document.getElementById("petVaccine").value = "";

                addPetButton.textContent = "Add Pet";

                // 新增成功後自動重新載入
                loadPetsButton.click();

            });

    });

}