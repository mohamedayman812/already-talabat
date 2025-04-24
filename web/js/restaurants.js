// Restaurants page specific functionality

// Import restaurants data (assuming it's in a separate file)
// If not in a separate file, define it here.
// Example:
const restaurantsData = [
  {
    id: 1,
    name: "Restaurant A",
    cuisine: "Italian",
    rating: 4.5,
    priceRange: "$$",
    deliveryTime: "30-40 min",
    image: "path/to/image1.jpg",
  },
  {
    id: 2,
    name: "Restaurant B",
    cuisine: "Mexican",
    rating: 4.0,
    priceRange: "$",
    deliveryTime: "25-35 min",
    image: "path/to/image2.jpg",
  },
  {
    id: 3,
    name: "Restaurant C",
    cuisine: "Japanese",
    rating: 4.8,
    priceRange: "$$$",
    deliveryTime: "40-50 min",
    image: "path/to/image3.jpg",
  },
  {
    id: 4,
    name: "Restaurant D",
    cuisine: "Italian",
    rating: 3.5,
    priceRange: "$$",
    deliveryTime: "30-40 min",
    image: "path/to/image4.jpg",
  },
  {
    id: 5,
    name: "Restaurant E",
    cuisine: "Indian",
    rating: 4.2,
    priceRange: "$$",
    deliveryTime: "35-45 min",
    image: "path/to/image5.jpg",
  },
  {
    id: 6,
    name: "Restaurant F",
    cuisine: "Chinese",
    rating: 3.9,
    priceRange: "$",
    deliveryTime: "20-30 min",
    image: "path/to/image6.jpg",
  },
  {
    id: 7,
    name: "Restaurant G",
    cuisine: "American",
    rating: 4.6,
    priceRange: "$$$",
    deliveryTime: "45-55 min",
    image: "path/to/image7.jpg",
  },
  {
    id: 8,
    name: "Restaurant H",
    cuisine: "Thai",
    rating: 4.1,
    priceRange: "$$",
    deliveryTime: "30-40 min",
    image: "path/to/image8.jpg",
  },
  {
    id: 9,
    name: "Restaurant I",
    cuisine: "Vietnamese",
    rating: 4.3,
    priceRange: "$",
    deliveryTime: "25-35 min",
    image: "path/to/image9.jpg",
  },
  {
    id: 10,
    name: "Restaurant J",
    cuisine: "Korean",
    rating: 4.7,
    priceRange: "$$$",
    deliveryTime: "40-50 min",
    image: "path/to/image10.jpg",
  },
]

document.addEventListener("DOMContentLoaded", () => {
  // Load all restaurants
  loadRestaurants()

  // Setup search and filters
  setupSearchAndFilters()
})

// Function to load restaurants
function loadRestaurants(filters = {}) {
  const restaurantList = document.getElementById("restaurant-list")
  if (!restaurantList) return

  // Filter restaurants based on search and filters
  let filteredRestaurants = [...restaurantsData]

  if (filters.search) {
    const searchTerm = filters.search.toLowerCase()
    filteredRestaurants = filteredRestaurants.filter(
      (restaurant) =>
        restaurant.name.toLowerCase().includes(searchTerm) || restaurant.cuisine.toLowerCase().includes(searchTerm),
    )
  }

  if (filters.cuisine) {
    filteredRestaurants = filteredRestaurants.filter((restaurant) =>
      restaurant.cuisine.toLowerCase().includes(filters.cuisine.toLowerCase()),
    )
  }

  if (filters.rating) {
    filteredRestaurants = filteredRestaurants.filter(
      (restaurant) => restaurant.rating >= Number.parseFloat(filters.rating),
    )
  }

  if (filters.price) {
    filteredRestaurants = filteredRestaurants.filter((restaurant) => restaurant.priceRange === filters.price)
  }

  let restaurantHTML = ""

  if (filteredRestaurants.length === 0) {
    restaurantHTML = `
      <div class="no-results">
        <p>No restaurants found matching your criteria.</p>
        <button class="btn btn-outline" id="clear-filters">Clear Filters</button>
      </div>
    `
  } else {
    filteredRestaurants.forEach((restaurant) => {
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
              <div class="price-range">${restaurant.priceRange}</div>
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
  }

  restaurantList.innerHTML = restaurantHTML

  // Add event listener to clear filters button
  const clearFiltersBtn = document.getElementById("clear-filters")
  if (clearFiltersBtn) {
    clearFiltersBtn.addEventListener("click", () => {
      document.getElementById("restaurant-search").value = ""
      document.getElementById("cuisine-filter").value = ""
      document.getElementById("rating-filter").value = ""
      document.getElementById("price-filter").value = ""

      loadRestaurants()
    })
  }
}

// Function to setup search and filters
function setupSearchAndFilters() {
  const searchInput = document.getElementById("restaurant-search")
  const cuisineFilter = document.getElementById("cuisine-filter")
  const ratingFilter = document.getElementById("rating-filter")
  const priceFilter = document.getElementById("price-filter")

  // Search input event
  if (searchInput) {
    searchInput.addEventListener(
      "input",
      debounce(() => {
        const filters = {
          search: searchInput.value,
          cuisine: cuisineFilter ? cuisineFilter.value : "",
          rating: ratingFilter ? ratingFilter.value : "",
          price: priceFilter ? priceFilter.value : "",
        }

        loadRestaurants(filters)
      }, 300),
    )
  }
  // Filter change events
  ;[cuisineFilter, ratingFilter, priceFilter].forEach((filter) => {
    if (filter) {
      filter.addEventListener("change", () => {
        const filters = {
          search: searchInput ? searchInput.value : "",
          cuisine: cuisineFilter ? cuisineFilter.value : "",
          rating: ratingFilter ? ratingFilter.value : "",
          price: priceFilter ? priceFilter.value : "",
        }

        loadRestaurants(filters)
      })
    }
  })
}

// Debounce function to limit how often a function can be called
function debounce(func, delay) {
  let timeout
  return function () {
    
    const args = arguments
    clearTimeout(timeout)
    timeout = setTimeout(() => func.apply(this, args), delay)
  }
}
