// Restaurant detail page specific functionality

// Helper function to get URL parameters
function getUrlParameter(name) {
  name = name.replace(/[[]/, "\\[").replace(/[\]]/, "\\]")
  var regex = new RegExp("[\\?&]" + name + "=([^&#]*)")
  var results = regex.exec(location.search)
  return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "))
}

// Dummy data for restaurants (replace with actual data source)
const restaurantsData = [
  {
    id: 1,
    name: "Delicious Bites",
    rating: 4.5,
    cuisine: "Italian",
    priceRange: "$$",
    deliveryTime: "30-40 min",
  },
  {
    id: 2,
    name: "Spicy Delights",
    rating: 4.2,
    cuisine: "Indian",
    priceRange: "$$$",
    deliveryTime: "35-45 min",
  },
]

// Dummy data for menu items (replace with actual data source)
const menuItemsData = {
  starters: [
    { id: 101, name: "Garlic Bread", price: 5.99, description: "Toasted bread with garlic butter", image: "" },
    { id: 102, name: "Bruschetta", price: 6.99, description: "Toasted bread with tomatoes and basil", image: "" },
  ],
  main: [
    { id: 201, name: "Pasta Carbonara", price: 12.99, description: "Pasta with eggs, cheese, and bacon", image: "" },
    { id: 202, name: "Chicken Tikka Masala", price: 14.99, description: "Chicken in a creamy tomato sauce", image: "" },
  ],
  sides: [
    { id: 301, name: "Fries", price: 3.99, description: "Crispy french fries", image: "" },
    { id: 302, name: "Salad", price: 4.99, description: "Fresh green salad", image: "" },
  ],
  desserts: [
    { id: 401, name: "Tiramisu", price: 7.99, description: "Coffee-flavored Italian dessert", image: "" },
    { id: 402, name: "Ice Cream", price: 4.99, description: "Vanilla ice cream", image: "" },
  ],
  drinks: [
    { id: 501, name: "Coke", price: 2.99, description: "Coca-Cola", image: "" },
    { id: 502, name: "Water", price: 1.99, description: "Bottled water", image: "" },
  ],
}

// Helper function to format currency
function formatCurrency(amount) {
  return "$" + amount.toFixed(2)
}

// Helper function to update cart count (replace with actual implementation)
function updateCartCount() {
  const cart = JSON.parse(localStorage.getItem("cart")) || []
  const cartCount = cart.reduce((sum, item) => sum + (item.quantity || 1), 0)
  const cartCountElement = document.getElementById("cart-count")
  if (cartCountElement) {
    cartCountElement.textContent = cartCount
  }
}

document.addEventListener("DOMContentLoaded", () => {
  // Get restaurant ID from URL
  const restaurantId = Number.parseInt(getUrlParameter("id")) || 1

  // Load restaurant details
  loadRestaurantDetails(restaurantId)

  // Load menu items
  loadMenuItems()

  // Setup menu navigation
  setupMenuNavigation()

  // Load cart items
  loadCartItems()

  // Setup add to cart functionality
  setupAddToCart()

  // Initial cart count update
  updateCartCount()
})

// Function to load restaurant details
function loadRestaurantDetails(restaurantId) {
  // Find restaurant by ID
  const restaurant = restaurantsData.find((r) => r.id === restaurantId) || restaurantsData[0]

  // Update restaurant header
  document.getElementById("restaurant-name").textContent = restaurant.name
  document.getElementById("restaurant-rating").textContent = restaurant.rating
  document.getElementById("restaurant-cuisine").textContent = restaurant.cuisine
  document.getElementById("restaurant-price").textContent = restaurant.priceRange
  document.getElementById("delivery-time").textContent = restaurant.deliveryTime

  // Update page title
  document.title = `${restaurant.name} - Already Talbt`
}

// Function to load menu items
function loadMenuItems() {
  // Get all menu category containers
  const menuCategories = {
    starters: document.querySelector("#starters .menu-items"),
    main: document.querySelector("#main-courses .menu-items"),
    sides: document.querySelector("#sides .menu-items"),
    desserts: document.querySelector("#desserts .menu-items"),
    drinks: document.querySelector("#drinks .menu-items"),
  }

  // Load items for each category
  for (const [category, container] of Object.entries(menuCategories)) {
    if (!container) continue

    const items = menuItemsData[category] || []
    let itemsHTML = ""

    items.forEach((item) => {
      itemsHTML += `
        <div class="menu-item" data-id="${item.id}">
          <div class="menu-item-image">
            <img src="${item.image || "/placeholder.svg?height=180&width=320"}" alt="${item.name}">
          </div>
          <div class="menu-item-content">
            <div class="menu-item-header">
              <div class="menu-item-name">${item.name}</div>
              <div class="menu-item-price">${formatCurrency(item.price)}</div>
            </div>
            <div class="menu-item-description">${item.description}</div>
            <div class="menu-item-actions">
              <div class="quantity-control">
                <button class="quantity-btn decrease-quantity" data-id="${item.id}">-</button>
                <span class="quantity">1</span>
                <button class="quantity-btn increase-quantity" data-id="${item.id}">+</button>
              </div>
              <button class="btn btn-primary add-to-cart" data-id="${item.id}">Add to Cart</button>
            </div>
          </div>
        </div>
      `
    })

    container.innerHTML = itemsHTML
  }
}

// Function to setup menu navigation
function setupMenuNavigation() {
  // Menu tabs
  const menuTabs = document.querySelectorAll(".menu-tab")
  menuTabs.forEach((tab) => {
    tab.addEventListener("click", function () {
      const category = this.getAttribute("data-category")

      // Update active tab
      menuTabs.forEach((t) => t.classList.remove("active"))
      this.classList.add("active")

      // Scroll to category section if not "all"
      if (category !== "all") {
        const categorySection = document.getElementById(category)
        if (categorySection) {
          categorySection.scrollIntoView({ behavior: "smooth" })
        }
      }
    })
  })

  // Sidebar category links
  const categoryLinks = document.querySelectorAll(".menu-categories a")
  categoryLinks.forEach((link) => {
    link.addEventListener("click", function (e) {
      e.preventDefault()

      // Update active link
      categoryLinks.forEach((l) => l.classList.remove("active"))
      this.classList.add("active")

      // Get target section ID
      const targetId = this.getAttribute("href").substring(1)
      const targetSection = document.getElementById(targetId)

      if (targetSection) {
        targetSection.scrollIntoView({ behavior: "smooth" })
      }
    })
  })
}

// Function to load cart items
function loadCartItems() {
  const cartItemsContainer = document.getElementById("cart-items-sidebar")
  if (!cartItemsContainer) return

  const cart = JSON.parse(localStorage.getItem("cart")) || []

  if (cart.length === 0) {
    cartItemsContainer.innerHTML = `
      <div class="empty-cart">
        <p>Your cart is empty</p>
        <p>Add items to get started</p>
      </div>
    `

    // Disable checkout button
    const checkoutBtn = document.getElementById("checkout-btn")
    if (checkoutBtn) {
      checkoutBtn.disabled = true
    }

    // Reset totals
    document.getElementById("cart-subtotal").textContent = formatCurrency(0)
    document.getElementById("delivery-fee").textContent = formatCurrency(0)
    document.getElementById("cart-total").textContent = formatCurrency(0)

    return
  }

  let cartHTML = ""
  let subtotal = 0

  cart.forEach((item) => {
    const itemTotal = item.price * (item.quantity || 1)
    subtotal += itemTotal

    cartHTML += `
      <div class="cart-item" data-id="${item.id}">
        <div class="cart-item-info">
          <div class="cart-item-name">${item.name}</div>
          <div class="cart-item-quantity">Qty: ${item.quantity || 1}</div>
        </div>
        <div class="cart-item-price">${formatCurrency(itemTotal)}</div>
      </div>
    `
  })

  cartItemsContainer.innerHTML = cartHTML

  // Calculate totals
  const deliveryFee = subtotal > 0 ? 2.99 : 0
  const total = subtotal + deliveryFee

  // Update totals
  document.getElementById("cart-subtotal").textContent = formatCurrency(subtotal)
  document.getElementById("delivery-fee").textContent = formatCurrency(deliveryFee)
  document.getElementById("cart-total").textContent = formatCurrency(total)

  // Enable checkout button
  const checkoutBtn = document.getElementById("checkout-btn")
  if (checkoutBtn) {
    checkoutBtn.disabled = false
    checkoutBtn.addEventListener("click", () => {
      window.location.href = "checkout.html"
    })
  }
}

// Function to setup add to cart functionality
function setupAddToCart() {
  // Quantity control
  document.addEventListener("click", (e) => {
    if (e.target.classList.contains("increase-quantity")) {
      const quantityElement = e.target.previousElementSibling
      const quantity = Number.parseInt(quantityElement.textContent)
      quantityElement.textContent = quantity + 1
    } else if (e.target.classList.contains("decrease-quantity")) {
      const quantityElement = e.target.nextElementSibling
      const quantity = Number.parseInt(quantityElement.textContent)
      if (quantity > 1) {
        quantityElement.textContent = quantity - 1
      }
    }
  })

  // Add to cart buttons
  const addToCartButtons = document.querySelectorAll(".add-to-cart")
  addToCartButtons.forEach((button) => {
    button.addEventListener("click", function () {
      const itemId = Number.parseInt(this.getAttribute("data-id"))
      const menuItem = findMenuItem(itemId)

      if (!menuItem) return

      // Get quantity
      const menuItemElement = this.closest(".menu-item")
      const quantityElement = menuItemElement.querySelector(".quantity")
      const quantity = Number.parseInt(quantityElement.textContent)

      // Add to cart
      addItemToCart(menuItem, quantity)

      // Reset quantity
      quantityElement.textContent = "1"

      // Show confirmation
      showAddedToCartConfirmation(menuItem.name)
    })
  })
}

// Function to find menu item by ID
function findMenuItem(itemId) {
  for (const category in menuItemsData) {
    const item = menuItemsData[category].find((item) => item.id === itemId)
    if (item) return item
  }
  return null
}

// Function to add item to cart
function addItemToCart(item, quantity) {
  // Get current cart
  const cart = JSON.parse(localStorage.getItem("cart")) || []

  // Check if item already exists in cart
  const existingItemIndex = cart.findIndex((cartItem) => cartItem.id === item.id)

  if (existingItemIndex !== -1) {
    // Update quantity if item exists
    cart[existingItemIndex].quantity = (cart[existingItemIndex].quantity || 1) + quantity
  } else {
    // Add new item to cart
    cart.push({
      id: item.id,
      name: item.name,
      price: item.price,
      quantity: quantity,
    })
  }

  // Save cart to localStorage
  localStorage.setItem("cart", JSON.stringify(cart))

  // Update cart count
  updateCartCount()

  // Reload cart items
  loadCartItems()
}

// Function to show added to cart confirmation
function showAddedToCartConfirmation(itemName) {
  // Create confirmation element
  const confirmation = document.createElement("div")
  confirmation.className = "cart-confirmation"
  confirmation.innerHTML = `
    <div class="cart-confirmation-content">
      <i class="fas fa-check-circle"></i>
      <p>${itemName} added to cart</p>
    </div>
  `

  // Add to body
  document.body.appendChild(confirmation)

  // Remove after 3 seconds
  setTimeout(() => {
    confirmation.classList.add("fade-out")
    setTimeout(() => {
      document.body.removeChild(confirmation)
    }, 300)
  }, 2000)
}
