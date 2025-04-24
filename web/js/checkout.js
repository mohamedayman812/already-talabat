// Checkout page specific functionality

// Function to format currency
function formatCurrency(amount) {
  return "$" + amount.toFixed(2)
}

document.addEventListener("DOMContentLoaded", () => {
  // Load cart summary
  loadCartSummary()

  // Setup checkout steps
  setupCheckoutSteps()

  // Setup address form
  setupAddressForm()

  // Setup payment form
  setupPaymentForm()

  // Load review order
  loadReviewOrder()
})

// Function to load cart summary
function loadCartSummary() {
  const summaryItemsContainer = document.getElementById("summary-items")
  if (!summaryItemsContainer) return

  const cart = JSON.parse(localStorage.getItem("cart")) || []

  if (cart.length === 0) {
    window.location.href = "cart.html"
    return
  }

  let summaryHTML = ""
  let subtotal = 0

  cart.forEach((item) => {
    const itemTotal = item.price * (item.quantity || 1)
    subtotal += itemTotal

    summaryHTML += `
      <div class="cart-item">
        <div class="cart-item-info">
          <div class="cart-item-name">${item.name}</div>
          <div class="cart-item-quantity">Qty: ${item.quantity || 1}</div>
        </div>
        <div class="cart-item-price">${formatCurrency(itemTotal)}</div>
      </div>
    `
  })

  summaryItemsContainer.innerHTML = summaryHTML

  // Calculate totals
  const deliveryFee = 2.99
  const tax = subtotal * 0.09 // 9% tax rate
  const total = subtotal + deliveryFee + tax

  // Update totals
  document.getElementById("checkout-subtotal").textContent = formatCurrency(subtotal)
  document.getElementById("checkout-delivery-fee").textContent = formatCurrency(deliveryFee)
  document.getElementById("checkout-tax").textContent = formatCurrency(tax)
  document.getElementById("checkout-total").textContent = formatCurrency(total)

  // Also update review items
  const reviewItemsContainer = document.getElementById("review-items")
  if (reviewItemsContainer) {
    reviewItemsContainer.innerHTML = summaryHTML
  }
}

// Function to setup checkout steps
function setupCheckoutSteps() {
  // Step navigation
  const steps = document.querySelectorAll(".step")
  const stepContents = document.querySelectorAll(".checkout-step-content")

  // Delivery to Payment
  const deliveryNextBtn = document.getElementById("delivery-next-btn")
  if (deliveryNextBtn) {
    deliveryNextBtn.addEventListener("click", () => {
      // Validate delivery form (in a real app)

      // Hide delivery step
      document.getElementById("delivery-step").classList.remove("active")

      // Show payment step
      document.getElementById("payment-step").classList.add("active")

      // Update steps
      steps[0].classList.remove("active")
      steps[1].classList.add("active")
    })
  }

  // Payment to Review
  const paymentNextBtn = document.getElementById("payment-next-btn")
  if (paymentNextBtn) {
    paymentNextBtn.addEventListener("click", () => {
      // Validate payment form (in a real app)

      // Hide payment step
      document.getElementById("payment-step").classList.remove("active")

      // Show review step
      document.getElementById("review-step").classList.add("active")

      // Update steps
      steps[1].classList.remove("active")
      steps[2].classList.add("active")
    })
  }

  // Payment back to Delivery
  const paymentBackBtn = document.getElementById("payment-back-btn")
  if (paymentBackBtn) {
    paymentBackBtn.addEventListener("click", () => {
      // Hide payment step
      document.getElementById("payment-step").classList.remove("active")

      // Show delivery step
      document.getElementById("delivery-step").classList.add("active")

      // Update steps
      steps[1].classList.remove("active")
      steps[0].classList.add("active")
    })
  }

  // Review back to Payment
  const reviewBackBtn = document.getElementById("review-back-btn")
  if (reviewBackBtn) {
    reviewBackBtn.addEventListener("click", () => {
      // Hide review step
      document.getElementById("review-step").classList.remove("active")

      // Show payment step
      document.getElementById("payment-step").classList.add("active")

      // Update steps
      steps[2].classList.remove("active")
      steps[1].classList.add("active")
    })
  }

  // Place order
  const placeOrderBtn = document.getElementById("place-order-btn")
  if (placeOrderBtn) {
    placeOrderBtn.addEventListener("click", () => {
      // In a real app, this would submit the order to the server

      // Clear cart
      localStorage.setItem("cart", JSON.stringify([]))

      // Redirect to confirmation page
      alert("Order placed successfully! You will be redirected to the confirmation page.")
      window.location.href = "index.html"
    })
  }
}

// Function to setup address form
function setupAddressForm() {
  // Add address button
  const addAddressBtn = document.getElementById("add-address-btn")
  const newAddressForm = document.getElementById("new-address-form")
  const cancelAddressBtn = document.getElementById("cancel-address-btn")

  if (addAddressBtn && newAddressForm) {
    addAddressBtn.addEventListener("click", () => {
      newAddressForm.style.display = "block"
      addAddressBtn.style.display = "none"
    })
  }

  if (cancelAddressBtn && newAddressForm && addAddressBtn) {
    cancelAddressBtn.addEventListener("click", () => {
      newAddressForm.style.display = "none"
      addAddressBtn.style.display = "block"
    })
  }

  // Address form submission
  const addressForm = document.getElementById("address-form")
  if (addressForm) {
    addressForm.addEventListener("submit", (e) => {
      e.preventDefault()

      // In a real app, this would save the address to the user's profile

      // Hide form
      newAddressForm.style.display = "none"
      addAddressBtn.style.display = "block"

      // Show success message
      alert("Address added successfully!")
    })
  }

  // Address card selection
  const addressCards = document.querySelectorAll(".address-card")
  addressCards.forEach((card) => {
    card.addEventListener("click", function () {
      // Remove selected class from all cards
      addressCards.forEach((c) => c.classList.remove("selected"))

      // Add selected class to clicked card
      this.classList.add("selected")
    })
  })
}

// Function to setup payment form
function setupPaymentForm() {
  // Payment method selection
  const paymentMethods = document.querySelectorAll(".payment-method input")
  const creditCardForm = document.getElementById("credit-card-form")

  paymentMethods.forEach((method) => {
    method.addEventListener("change", function () {
      if (this.id === "credit-card" && creditCardForm) {
        creditCardForm.style.display = "block"
      } else if (creditCardForm) {
        creditCardForm.style.display = "none"
      }
    })
  })
}

// Function to load review order
function loadReviewOrder() {
  // This would be populated with actual data in a real app
  // For now, we'll use the static HTML in the template
}
