// Import or declare missing variables
import { formatCurrency } from "./utils.js" // Assuming formatCurrency is in utils.js
import { updateCartCount } from "./app.js" // Assuming updateCartCount is in app.js

// Mock menuItemsData (replace with actual data loading)
const menuItemsData = {
  starters: [
    {
      id: 101,
      name: "Starter 1",
      price: 5.99,
      description: "Delicious starter",
      image: "/placeholder.svg?height=180&width=320",
    },
    {
      id: 102,
      name: "Starter 2",
      price: 6.99,
      description: "Another great starter",
      image: "/placeholder.svg?height=180&width=320",
    },
  ],
  main: [
    {
      id: 201,
      name: "Main 1",
      price: 12.99,
      description: "Hearty main course",
      image: "/placeholder.svg?height=180&width=320",
    },
    {
      id: 202,
      name: "Main 2",
      price: 14.99,
      description: "Another satisfying main",
      image: "/placeholder.svg?height=180&width=320",
    },
    {
      id: 203,
      name: "Main 3",
      price: 13.99,
      description: "A flavorful main dish",
      image: "/placeholder.svg?height=180&width=320",
    },
  ],
  desserts: [
    {
      id: 301,
      name: "Dessert 1",
      price: 7.99,
      description: "Sweet ending",
      image: "/placeholder.svg?height=180&width=320",
    },
    {
      id: 302,
      name: "Dessert 2",
      price: 8.99,
      description: "Indulgent dessert",
      image: "/placeholder.svg?height=180&width=320",
    },
  ],
}

// Cart page specific functionality

document.addEventListener("DOMContentLoaded", () => {
  // Load cart items
  loadCartItems()

  // Setup cart functionality
  setupCartFunctionality()

  // Load suggested items
  loadSuggestedItems()
})

// Function to load cart items
function loadCartItems() {
  const cartItemsContainer = document.getElementById("cart-items")
  if (!cartItemsContainer) return

  const cart = JSON.parse(localStorage.getItem("cart")) || []

  if (cart.length === 0) {
    // Show empty cart message
    document.getElementById("empty-cart-message").style.display = "block"

    // Disable checkout button
    const checkoutBtn = document.getElementById("checkout-btn")
    if (checkoutBtn) {
      checkoutBtn.disabled = true
    }

    // Reset totals
    document.getElementById("cart-subtotal").textContent = formatCurrency(0)
    document.getElementById("delivery-fee").textContent = formatCurrency(0)
    document.getElementById("tax").textContent = formatCurrency(0)
    document.getElementById("cart-total").textContent = formatCurrency(0)

    return
  }

  // Hide empty cart message
  const emptyCartMessage = document.getElementById("empty-cart-message")
  if (emptyCartMessage) {
    emptyCartMessage.style.display = "none"
  }

  let cartHTML = ""
  let subtotal = 0

  cart.forEach((item) => {
    const itemTotal = item.price * (item.quantity || 1)
    subtotal += itemTotal

    cartHTML += `
      <div class="cart-item" data-id="${item.id}">
        <div class="cart-item-image">
          <img src="/placeholder.svg?height=80&width=80" alt="${item.name}">
        </div>
        <div class="cart-item-details">
          <div class="cart-item-name">${item.name}</div>
          <div class="cart-item-options">
            ${item.options ? item.options.join(", ") : ""}
          </div>
          <div class="cart-item-actions">
            <div class="quantity-control">
              <button class="quantity-btn decrease-quantity" data-id="${item.id}">-</button>
              <span class="quantity">${item.quantity || 1}</span>
              <button class="quantity-btn increase-quantity" data-id="${item.id}">+</button>
            </div>
            <div class="cart-item-price">${formatCurrency(itemTotal)}</div>
            <button class="btn-icon remove-item" data-id="${item.id}">
              <i class="fas fa-trash"></i>
            </button>
          </div>
        </div>
      </div>
    `
  })

  cartItemsContainer.innerHTML = cartHTML

  // Calculate totals
  const deliveryFee = 2.99
  const tax = subtotal * 0.09 // 9% tax rate
  const total = subtotal + deliveryFee + tax

  // Update totals
  document.getElementById("cart-subtotal").textContent = formatCurrency(subtotal)
  document.getElementById("delivery-fee").textContent = formatCurrency(deliveryFee)
  document.getElementById("tax").textContent = formatCurrency(tax)
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

// Function to setup cart functionality
function setupCartFunctionality() {
  // Clear cart button
  const clearCartBtn = document.getElementById("clear-cart")
  if (clearCartBtn) {
    clearCartBtn.addEventListener("click", () => {
      if (confirm("Are you sure you want to clear your cart?")) {
        localStorage.setItem("cart", JSON.stringify([]))
        updateCartCount()
        loadCartItems()
      }
    })
  }

  // Quantity control and remove item
  document.addEventListener("click", (e) => {
    // Increase quantity
    if (e.target.classList.contains("increase-quantity")) {
      const itemId = Number.parseInt(e.target.getAttribute("data-id"))
      updateCartItemQuantity(itemId, 1)
    }
    // Decrease quantity
    else if (e.target.classList.contains("decrease-quantity")) {
      const itemId = Number.parseInt(e.target.getAttribute("data-id"))
      updateCartItemQuantity(itemId, -1)
    }
    // Remove item
    else if (e.target.classList.contains("remove-item") || e.target.closest(".remove-item")) {
      const button = e.target.classList.contains("remove-item") ? e.target : e.target.closest(".remove-item")
      const itemId = Number.parseInt(button.getAttribute("data-id"))
      removeCartItem(itemId)
    }
  })

  // Apply promo code
  const applyPromoBtn = document.getElementById("apply-promo")
  if (applyPromoBtn) {
    applyPromoBtn.addEventListener("click", () => {
      const promoCode = document.getElementById("promo-code").value

      if (!promoCode) {
        alert("Please enter a promo code")
        return
      }

      // Mock promo code validation
      if (promoCode.toUpperCase() === "WELCOME10") {
        alert("Promo code applied! 10% discount added.")
        // In a real app, this would recalculate the totals
      } else {
        alert("Invalid promo code")
      }
    })
  }
}

// Function to update cart item quantity
function updateCartItemQuantity(itemId, change) {
  // Get current cart
  const cart = JSON.parse(localStorage.getItem("cart")) || []

  // Find item index
  const itemIndex = cart.findIndex((item) => item.id === itemId)

  if (itemIndex === -1) return

  // Update quantity
  cart[itemIndex].quantity = (cart[itemIndex].quantity || 1) + change

  // Remove item if quantity is 0
  if (cart[itemIndex].quantity <= 0) {
    cart.splice(itemIndex, 1)
  }

  // Save cart to localStorage
  localStorage.setItem("cart", JSON.stringify(cart))

  // Update cart count
  updateCartCount()

  // Reload cart items
  loadCartItems()
}

// Function to remove cart item
function removeCartItem(itemId) {
  // Get current cart
  let cart = JSON.parse(localStorage.getItem("cart")) || []

  // Remove item
  cart = cart.filter((item) => item.id !== itemId)

  // Save cart to localStorage
  localStorage.setItem("cart", JSON.stringify(cart))

  // Update cart count
  updateCartCount()

  // Reload cart items
  loadCartItems()
}

// Function to load suggested items
function loadSuggestedItems() {
  const suggestedItemsContainer = document.getElementById("suggested-items")
  if (!suggestedItemsContainer) return

  // Get random items from different categories
  const suggestedItems = [
    ...getRandomItems(menuItemsData.starters, 1),
    ...getRandomItems(menuItemsData.main, 2),
    ...getRandomItems(menuItemsData.desserts, 1),
  ]

  let suggestedHTML = ""

  suggestedItems.forEach((item) => {
    suggestedHTML += `
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
          <button class="btn btn-primary btn-block add-to-cart" data-id="${item.id}">Add to Cart</button>
        </div>
      </div>
    `
  })

  suggestedItemsContainer.innerHTML = suggestedHTML

  // Add event listeners to add to cart buttons
  const addToCartButtons = suggestedItemsContainer.querySelectorAll(".add-to-cart")
  addToCartButtons.forEach((button) => {
    button.addEventListener("click", function () {
      const itemId = Number.parseInt(this.getAttribute("data-id"))
      const menuItem = findMenuItem(itemId)

      if (!menuItem) return

      // Add to cart
      addItemToCart(menuItem, 1)

      // Show confirmation
      showAddedToCartConfirmation(menuItem.name)
    })
  })
}

// Function to get random items from an array
function getRandomItems(array, count) {
  if (!array || array.length === 0) return []

  const shuffled = [...array].sort(() => 0.5 - Math.random())
  return shuffled.slice(0, count)
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
