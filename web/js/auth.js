// Authentication page functionality

document.addEventListener("DOMContentLoaded", () => {
  // Tab switching
  const authTabs = document.querySelectorAll(".auth-tab")
  const authForms = document.querySelectorAll(".auth-form")

  authTabs.forEach((tab) => {
    tab.addEventListener("click", function () {
      const tabId = this.getAttribute("data-tab")

      // Update active tab
      authTabs.forEach((t) => t.classList.remove("active"))
      this.classList.add("active")

      // Show corresponding form
      authForms.forEach((form) => {
        form.classList.remove("active")
        if (form.id === tabId + "-form") {
          form.classList.add("active")
        }
      })
    })
  })

  // Login form submission
  const loginForm = document.getElementById("login-form-element")
  if (loginForm) {
    loginForm.addEventListener("submit", (e) => {
      e.preventDefault()

      const email = document.getElementById("login-email").value
      const password = document.getElementById("login-password").value
      const userType = document.querySelector('input[name="user-type"]:checked').value

      // Simple validation
      if (!email || !password) {
        alert("Please fill in all fields")
        return
      }

      // Mock login - in a real app, this would be an API call
      console.log("Logging in with:", { email, userType })

      // Store login state
      localStorage.setItem("isLoggedIn", "true")
      localStorage.setItem("userType", userType)
      localStorage.setItem("userEmail", email)

      // Redirect based on user type
      if (userType === "vendor") {
        window.location.href = "vendor-dashboard.html"
      } else if (userType === "delivery") {
        window.location.href = "delivery-dashboard.html"
      } else {
        window.location.href = "index.html"
      }
    })
  }

  // Registration form submission
  const registerForm = document.getElementById("register-form-element")
  if (registerForm) {
    registerForm.addEventListener("submit", (e) => {
      e.preventDefault()

      const name = document.getElementById("register-name").value
      const email = document.getElementById("register-email").value
      const phone = document.getElementById("register-phone").value
      const password = document.getElementById("register-password").value
      const confirmPassword = document.getElementById("register-confirm-password").value
      const userType = document.querySelector('input[name="reg-user-type"]:checked').value
      const termsAccepted = document.getElementById("terms").checked

      // Simple validation
      if (!name || !email || !phone || !password || !confirmPassword) {
        alert("Please fill in all fields")
        return
      }

      if (password !== confirmPassword) {
        alert("Passwords do not match")
        return
      }

      if (!termsAccepted) {
        alert("Please accept the terms and conditions")
        return
      }

      // Mock registration - in a real app, this would be an API call
      console.log("Registering with:", { name, email, phone, userType })

      // Store login state
      localStorage.setItem("isLoggedIn", "true")
      localStorage.setItem("userType", userType)
      localStorage.setItem("userName", name)
      localStorage.setItem("userEmail", email)

      // Redirect based on user type
      if (userType === "vendor") {
        window.location.href = "vendor-dashboard.html"
      } else if (userType === "delivery") {
        window.location.href = "delivery-dashboard.html"
      } else {
        window.location.href = "index.html"
      }
    })
  }
})
