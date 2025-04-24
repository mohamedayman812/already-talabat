// Home page specific functionality

// Import the restaurants data (assuming it's in a separate file)
import restaurantsData from "./restaurantsData.js"

document.addEventListener("DOMContentLoaded", () => {
  // Load popular restaurants
  loadPopularRestaurants()
})

// Function to load popular restaurants
function loadPopularRestaurants() {
  const restaurantList = document.getElementById("restaurant-list")
  if (!restaurantList) return

  // Get a subset of restaurants for the home page
  const popularRestaurants = restaurantsData.slice(0, 3)

  let restaurantHTML = ""

  popularRestaurants.forEach((restaurant) => {
    restaurantHTML += `
      <div class="restaurant-card">
        <img src="${restaurant.image || "/placeholder.svg?height=180&width=320"}" alt="${restaurant.name}">
        <div class="restaurant-card-content">
          <h3>${restaurant.name}</h3>
          <div class="restaurant-meta">
            <div class="rating">
              <i class="fas fa-star"></i>
              <span>${restaurant.rating}</span>
            </div>
            <div class="cuisine">${restaurant.cuisine}</div>
            <div class="delivery-time">
              <i class="fas fa-clock"></i>
              <span>${restaurant.deliveryTime}</span>
            </div>
          </div>
          <a href="restaurant-detail.html?id=${restaurant.id}" class="btn btn-primary">View Menu</a>
        </div>
      </div>
    `
  })

  restaurantList.innerHTML = restaurantHTML
}
