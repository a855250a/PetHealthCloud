console.log("script.js loaded");

let editPetId = null;

// =========================
// Authentication Guard
// =========================

const isDashboard = window.location.pathname.includes("dashboard.html");

if (isDashboard) {
    const token = localStorage.getItem("token");

    console.log("Auth Guard Token:", token);

    if (!token || token === "null" || token === "undefined") {
        console.log("No valid token, redirecting to login...");
        localStorage.removeItem("token");
        window.location.replace("/login.html");
    }
}

// =========================
// Login
// =========================

const loginButton = document.getElementById("loginButton");

const passwordInput = document.getElementById("password");

if (passwordInput) {
    passwordInput.addEventListener("keydown", function (event) {
        if (event.key === "Enter") {
            loginButton.click();
        }
    });
}

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
.then(async response => {

    const data = await response.json();

    if (!response.ok || !data.token) {
        throw new Error(data.message || "帳號或密碼錯誤");
    }

    return data;
})
.then(data => {

    console.log("Login Success");

    localStorage.setItem("token", data.token);

    window.location.href = "/dashboard.html";

})
.catch(error => {

    console.error("Login Error:", error);

    localStorage.removeItem("token");

    alert("登入失敗，請確認帳號與密碼");

});

    });

}

// =========================
// Guest Login
// =========================

const guestLoginButton = document.getElementById("guestLoginButton");

if (guestLoginButton) {

    guestLoginButton.addEventListener("click", function () {

        guestLoginButton.disabled = true;
        guestLoginButton.textContent = "登入中...";

        fetch("/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email: "demo@pethealthcloud.com",
                password: "Demo123456"
            })
        })
        .then(response => {

            if (!response.ok) {
                throw new Error("訪客登入失敗");
            }

            return response.json();

        })
        .then(data => {

            localStorage.setItem("token", data.token);

            window.location.href = "/dashboard.html";

        })
        .catch(error => {

            console.error("Guest Login Error:", error);

            alert("訪客登入暫時無法使用");

            guestLoginButton.disabled = false;
            guestLoginButton.textContent = "🐾 訪客體驗登入";

        });

    });

}

// =========================
// Dashboard
// =========================

const logoutButton = document.getElementById("logoutButton");

if (logoutButton) {
    logoutButton.addEventListener("click", function () {
        localStorage.removeItem("token");
        window.location.href = "/login.html";
    });
}

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
                    <p>年齡: ${pet.age}</p>
                    <p>體重: ${pet.weight} kg</p>
                    <p>疫苗紀錄: ${pet.vaccine ?? "-"}</p>
                    ${pet.photo ? `
                    <img
                        src="${pet.photo}"
                        class="pet-photo"
                        alt="${pet.name}">
                    ` : ""}
            
                    <div class="pet-actions">
            
                        <button
                            class="editPetButton"
                            data-id="${pet.id}">
                            編輯
                        </button>
            
                        <button
                            class="deletePetButton"
                            data-id="${pet.id}">
                            刪除
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

                        addPetButton.textContent = "更新寵物";

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
        const petPhoto = document.getElementById("petPhoto").files[0];

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

        if (petPhoto) {

            const formData = new FormData();
            formData.append("file", petPhoto);

            fetch("/upload", {
                method: "POST",
                body: formData
            })
                .then(response => response.text())
                .then(photoPath => {

                    savePet(photoPath);

                });

        } else {

            savePet("");

        }

        function savePet(photoPath) {

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
                    vaccine: petVaccine,
                    photo: photoPath
                })
            })
                .then(response => response.json())
                .then(data => {

                    console.log("Add Pet Response:", data);

                    editPetId = null;

                    document.getElementById("petName").value = "";
                    document.getElementById("petAge").value = "";
                    document.getElementById("petWeight").value = "";
                    document.getElementById("petVaccine").value = "";
                    document.getElementById("petPhoto").value = "";

                    addPetButton.textContent = "新增寵物";

                    loadPetsButton.click();

                });

        }

    });

}
