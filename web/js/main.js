// Main JavaScript file for shared functionality across pages

document.addEventListener("DOMContentLoaded", () => {
  // Mobile menu toggle
  const mobileMenuToggle = document.getElementById("mobile-menu-toggle")
  const mobileMenu = document.getElementById("mobile-menu")

  if (mobileMenuToggle && mobileMenu) {
    mobileMenuToggle.addEventListener("click", () => {
      mobileMenu.classList.toggle("active")
    })
  }

  // User authentication state check (mock)
  const isLoggedIn = localStorage.getItem("isLoggedIn") === "true"
  const loginBtn = document.getElementById("login-btn")
  const userProfile = document.getElementById("user-profile")

  if (loginBtn && userProfile) {
    if (isLoggedIn) {
      loginBtn.style.display = "none"
      userProfile.style.display = "block"
    } else {
      loginBtn.style.display = "block"
      userProfile.style.display = "none"
    }
  }

  // Logout functionality
  const logoutBtn = document.getElementById("logout-btn")
  if (logoutBtn) {
    logoutBtn.addEventListener("click", (e) => {
      e.preventDefault()
      localStorage.setItem("isLoggedIn", "false")
      window.location.href = "index.html"
    })
  }

  // Cart count update
  updateCartCount()
})

// Function to update cart count
function updateCartCount() {
  const cartCountElements = document.querySelectorAll("#cart-count")
  const cart = JSON.parse(localStorage.getItem("cart")) || []
  const itemCount = cart.reduce((total, item) => total + (item.quantity || 1), 0)

  cartCountElements.forEach((element) => {
    element.textContent = itemCount
  })
}

// Function to format currency
function formatCurrency(amount) {
  return "$" + Number.parseFloat(amount).toFixed(2)
}

// Function to get URL parameters
function getUrlParameter(name) {
  name = name.replace(/[[]/, "\\[").replace(/[\]]/, "\\]")
  const regex = new RegExp("[\\?&]" + name + "=([^&#]*)")
  const results = regex.exec(location.search)
  return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "))
}

// Mock data for restaurants
const restaurantsData = [
  {
    id: 1,
    name: "Burger Palace",
    image: "img/restaurant-1.jpg",
    cuisine: "American, Burgers",
    rating: 4.7,
    deliveryTime: "20-35 min",
    priceRange: "$$",
  },
  {
    id: 2,
    name: "Pizza Heaven",
    image: "img/restaurant-2.jpg",
    cuisine: "Italian, Pizza",
    rating: 4.5,
    deliveryTime: "25-40 min",
    priceRange: "$$",
  },
  {
    id: 3,
    name: "Sushi World",
    image: "img/restaurant-3.jpg",
    cuisine: "Japanese, Sushi",
    rating: 4.8,
    deliveryTime: "30-45 min",
    priceRange: "$$$",
  },
  {
    id: 4,
    name: "Taco Fiesta",
    image: "img/restaurant-4.jpg",
    cuisine: "Mexican, Tacos",
    rating: 4.3,
    deliveryTime: "15-30 min",
    priceRange: "$",
  },
  {
    id: 5,
    name: "Curry House",
    image: "img/restaurant-5.jpg",
    cuisine: "Indian, Curry",
    rating: 4.6,
    deliveryTime: "30-45 min",
    priceRange: "$$",
  },
  {
    id: 6,
    name: "Noodle Bar",
    image: "img/restaurant-6.jpg",
    cuisine: "Chinese, Noodles",
    rating: 4.4,
    deliveryTime: "20-35 min",
    priceRange: "$$",
  },
]

// Mock data for menu items
const menuItemsData = {
  starters: [
    {
      id: 101,
      name: "Garlic Bread",
      description: "Freshly baked bread with garlic butter and herbs",
      price: 4.99,
      image: "img/menu-item-1.jpg",
    },
    {
      id: 102,
      name: "Mozzarella Sticks",
      description: "Breaded mozzarella sticks with marinara sauce",
      price: 6.99,
      image: "img/menu-item-2.jpg",
    },
    {
      id: 103,
      name: "Chicken Wings",
      description: "Spicy chicken wings with blue cheese dip",
      price: 8.99,
      image: "img/menu-item-3.jpg",
    },
  ],
  main: [
    {
      id: 201,
      name: "Classic Burger",
      description: "Beef patty with lettuce, tomato, and special sauce",
      price: 12.99,
      image: "img/menu-item-4.jpg",
    },
    {
      id: 202,
      name: "Margherita Pizza",
      description: "Tomato sauce, mozzarella, and fresh basil",
      price: 14.99,
      image: "img/menu-item-5.jpg",
    },
    {
      id: 203,
      name: "Grilled Salmon",
      description: "Fresh salmon with lemon butter sauce and vegetables",
      price: 18.99,
      image: "img/menu-item-6.jpg",
    },
  ],
  sides: [
    {
      id: 301,
      name: "French Fries",
      description: "Crispy golden fries with sea salt",
      price: 3.99,
      image: "img/menu-item-7.jpg",
    },
    {
      id: 302,
      name: "Onion Rings",
      description: "Crispy battered onion rings",
      price: 4.99,
      image: "img/menu-item-8.jpg",
    },
  ],
  desserts: [
    {
      id: 401,
      name: "Chocolate Cake",
      description: "Rich chocolate cake with chocolate ganache",
      price: 6.99,
      image: "img/menu-item-9.jpg",
    },
    {
      id: 402,
      name: "Cheesecake",
      description: "New York style cheesecake with berry compote",
      price: 7.99,
      image: "img/menu-item-10.jpg",
    },
  ],
  drinks: [
    {
      id: 501,
      name: "Soft Drink",
      description: "Choice of Coke, Sprite, or Fanta",
      price: 2.49,
      image: "img/menu-item-11.jpg",
    },
    {
      id: 502,
      name: "Iced Tea",
      description: "Freshly brewed iced tea with lemon",
      price: 2.99,
      image: "img/menu-item-12.jpg",
    },
  ],
}
