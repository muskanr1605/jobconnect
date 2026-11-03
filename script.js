document.addEventListener("DOMContentLoaded", function () {

    // ========================================
    // 1. MOBILE NAVIGATION
    // ========================================

    const menuButton = document.getElementById("menuButton");
    const navLinks = document.querySelector(".nav-links");

    if (menuButton && navLinks) {

        menuButton.addEventListener("click", function () {

            navLinks.classList.toggle("show");

            const isExpanded = navLinks.classList.contains("show");

            menuButton.setAttribute("aria-expanded", isExpanded);
            menuButton.textContent = isExpanded ? "✕" : "☰";

        });

        navLinks.querySelectorAll("a").forEach(function (link) {

            link.addEventListener("click", function () {

                navLinks.classList.remove("show");
                menuButton.setAttribute("aria-expanded", "false");
                menuButton.textContent = "☰";

            });

        });

    }


    // ========================================
    // 2. CURRENT YEAR IN FOOTER
    // ========================================

    const currentYear = document.getElementById("currentYear");

    if (currentYear) {
        currentYear.textContent = new Date().getFullYear();
    }


    // ========================================
    // 3. LOGIN WITH SPRING BOOT + MYSQL
    // ========================================

    const apiLoginForm = document.getElementById("loginForm");

    if (apiLoginForm) {

        apiLoginForm.addEventListener("submit", async function (event) {

            event.preventDefault();

            const email = document.getElementById("email").value.trim();

            const password = document.getElementById("password").value;

            const message = document.getElementById("loginMessage");

            const button = apiLoginForm.querySelector(
                'button[type="submit"]'
            );

            if (!isValidEmail(email)) {

                message.textContent = "Please enter a valid email address.";
                message.style.color = "red";

                return;

            }

            if (!password.trim()) {

                message.textContent = "Please enter your password.";
                message.style.color = "red";

                return;

            }

            message.textContent = "Logging in...";
            message.style.color = "#2563eb";

            button.disabled = true;

            try {

                const response = await fetch(
                    "https://jobconnect-65l2.onrender.com/api/users/login",
                    {
                        method: "POST",

                        headers: {
                            "Content-Type": "application/json"
                        },

                        body: JSON.stringify({
                            email: email,
                            password: password
                        })
                    }
                );

                const data = await response.json();

                if (!response.ok) {

                    throw new Error(
                        data.message || "Invalid email or password."
                    );

                }

                sessionStorage.setItem(
                    "jobConnectUser",
                    JSON.stringify(data)
                );

                message.textContent = "Login successful!";
                message.style.color = "green";

                if (data.role === "SEEKER") {

                    window.location.href = "seeker-dashboard.html";

                }

                else if (data.role === "RECRUITER") {

                    window.location.href = "recruiter-dashboard.html";

                }

                else {

                    sessionStorage.removeItem("jobConnectUser");

                    throw new Error("Invalid account role.");

                }

            }

            catch (error) {

                message.textContent = error.message;
                message.style.color = "red";

            }

            finally {

                button.disabled = false;

            }

        });

    }


    // ========================================
    // 4. JOB SEARCH AND FILTER
    // ========================================

    const jobSearchForm = document.getElementById("jobSearchForm");

    const jobKeyword = document.getElementById("jobKeyword");

    const jobLocation = document.getElementById("jobLocation");

    const jobCards = document.querySelectorAll("#jobList .job-card");

    if (jobCards.length > 0) {

        const params = new URLSearchParams(window.location.search);

        if (jobKeyword && params.has("keyword")) {
            jobKeyword.value = params.get("keyword");
        }

        if (jobLocation && params.has("location")) {
            jobLocation.value = params.get("location");
        }

        function filterJobs() {

            const keyword = jobKeyword
                ? jobKeyword.value.trim().toLowerCase()
                : "";

            const location = jobLocation
                ? jobLocation.value.trim().toLowerCase()
                : "";

            let visibleCount = 0;

            jobCards.forEach(function (card) {

                const cardText = card.textContent.toLowerCase();

                const matchesKeyword =
                    keyword === "" || cardText.includes(keyword);

                const matchesLocation =
                    location === "" || cardText.includes(location);

                if (matchesKeyword && matchesLocation) {

                    card.style.display = "";
                    visibleCount++;

                }

                else {

                    card.style.display = "none";

                }

            });

            let noResults = document.getElementById("noResults");

            if (!noResults) {

                noResults = document.createElement("p");

                noResults.id = "noResults";

                noResults.textContent =
                    "No jobs found. Try different keywords or location.";

                noResults.style.textAlign = "center";
                noResults.style.padding = "30px";
                noResults.style.color = "#64748b";
                noResults.style.gridColumn = "1 / -1";

                document.getElementById("jobList").appendChild(noResults);

            }

            noResults.style.display =
                visibleCount === 0 ? "block" : "none";

        }

        if (jobSearchForm) {

            jobSearchForm.addEventListener("submit", function (event) {

                event.preventDefault();
                filterJobs();

            });

        }

        if (jobKeyword) {
            jobKeyword.addEventListener("input", filterJobs);
        }

        if (jobLocation) {
            jobLocation.addEventListener("input", filterJobs);
        }

        filterJobs();

    }


    // ========================================
    // 5. REGISTER WITH SPRING BOOT + MYSQL
    // ========================================

    const apiRegisterForm = document.getElementById("registerForm");

    if (apiRegisterForm) {

        apiRegisterForm.addEventListener("submit", async function (event) {

            event.preventDefault();

            const name = document.getElementById("name").value.trim();

            const email = document.getElementById("registerEmail").value.trim();

            const phone = document.getElementById("phone").value.trim();

            const password = document.getElementById("registerPassword").value;

            const role = document.getElementById("role").value;

            const message = document.getElementById("registerMessage");

            const button = apiRegisterForm.querySelector(
                'button[type="submit"]'
            );

            if (!name || !email || !phone || !password || !role) {

                message.textContent = "Please fill in all fields.";
                message.style.color = "red";

                return;

            }

            if (!isValidEmail(email)) {

                message.textContent = "Please enter a valid email address.";
                message.style.color = "red";

                return;

            }

            if (!/^[0-9]{10}$/.test(phone)) {

                message.textContent = "Please enter a valid 10-digit phone number.";
                message.style.color = "red";

                return;

            }

            if (password.length < 8) {

                message.textContent = "Password must contain at least 8 characters.";
                message.style.color = "red";

                return;

            }

            if (role !== "SEEKER" && role !== "RECRUITER") {

                message.textContent = "Please select a valid account type.";
                message.style.color = "red";

                return;

            }

            button.disabled = true;

            message.textContent = "Creating your account...";
            message.style.color = "#2563eb";

            try {

                const response = await fetch(
                    "https://jobconnect-65l2.onrender.com/api/users/register",
                    {
                        method: "POST",

                        headers: {
                            "Content-Type": "application/json"
                        },

                        body: JSON.stringify({

                            name: name,
                            email: email,
                            phone: phone,
                            password: password,
                            role: role

                        })
                    }
                );

                const data = await response.json();

                if (!response.ok) {

                    throw new Error(
                        data.message || "Registration failed. Please try again."
                    );

                }

                message.textContent =
                    "Account created successfully! Redirecting to login...";

                message.style.color = "green";

                apiRegisterForm.reset();

                setTimeout(function () {

                    window.location.href = "login.html";

                }, 1500);

            }

            catch (error) {

                message.textContent = error.message;
                message.style.color = "red";

            }

            finally {

                button.disabled = false;

            }

        });

    }


    // ========================================
    // 6. POST JOB FORM VALIDATION
    // ========================================

    const postJobForm = document.querySelector(
        'form[action="/jobportal/jobs"]'
    );

    if (postJobForm) {

        postJobForm.addEventListener("submit", function (event) {

            const salaryMin = postJobForm.querySelector(
                '[name="salary_min"]'
            );

            const salaryMax = postJobForm.querySelector(
                '[name="salary_max"]'
            );

            if (salaryMin && salaryMax) {

                const min = Number(salaryMin.value);
                const max = Number(salaryMax.value);

                if (
                    salaryMin.value.trim() !== "" &&
                    salaryMax.value.trim() !== "" &&
                    (min < 0 || max < 0 || max < min)
                ) {

                    event.preventDefault();

                    alert("Please enter a valid salary range.");

                    return;

                }

            }

            const title = postJobForm.querySelector(
                '[name="job_title"]'
            );

            const company = postJobForm.querySelector(
                '[name="company_name"]'
            );

            const description = postJobForm.querySelector(
                '[name="description"]'
            );

            const location = postJobForm.querySelector(
                '[name="location"]'
            );

            if (
                (title && !title.value.trim()) ||
                (company && !company.value.trim()) ||
                (description && !description.value.trim()) ||
                (location && !location.value.trim())
            ) {

                event.preventDefault();

                alert("Please fill in all required job details.");

            }

        });

    }


    // ========================================
    // 7. APPLICATION STATUS UPDATE
    // ========================================

    const updateButtons = document.querySelectorAll(
        ".update-status-btn"
    );

    updateButtons.forEach(function (button) {

        button.addEventListener("click", function () {

            const row = button.closest("tr");

            if (!row) return;

            const statusSelect = row.querySelector("select");

            const statusCell = row.querySelector(".status-cell");

            if (!statusSelect || !statusCell) return;

            const selectedStatus = statusSelect.value;

            if (!selectedStatus) {

                alert("Please select an application status.");

                return;

            }

            statusCell.textContent = selectedStatus;

            statusCell.className =
                "status-cell status-" +
                selectedStatus.toLowerCase();

            alert("Application status updated successfully!");

        });

    });


    // ========================================
    // 8. EMAIL VALIDATION FUNCTION
    // ========================================

    function isValidEmail(email) {

        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        return emailPattern.test(email);

    }


    // ========================================
    // 9. PASSWORD SHOW / HIDE
    // ========================================

    const passwordToggles = document.querySelectorAll(
        ".toggle-password"
    );

    passwordToggles.forEach(function (toggle) {

        toggle.addEventListener("click", function () {

            const passwordInput = toggle.parentElement.querySelector(
                'input[type="password"], input[type="text"]'
            );

            if (!passwordInput) return;

            if (passwordInput.type === "password") {

                passwordInput.type = "text";
                toggle.textContent = "Hide";

            }

            else {

                passwordInput.type = "password";
                toggle.textContent = "Show";

            }

        });

    });

});