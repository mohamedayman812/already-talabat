"use client"

import { useState, useEffect } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { useToast } from "@/hooks/use-toast"
import { API_URL } from "@/lib/utils"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"

type OrderStatus = 'Prepared' | 'placed' | 'out_for_delivery' | 'delivered'

interface Order {
  id: string
  customerId: string
  restaurantId: string
  deliveryGuyId?: string | null
  status: OrderStatus
  paymentMethod: string
  items: string[] 
}

interface Restaurant {
  id: string
  name: string
}

interface MenuItem {
  id: string
  name: string
  price: number
}

export default function DeliveryOrders() {
  const [orders, setOrders] = useState<Order[]>([])
  const [restaurants, setRestaurants] = useState<Record<string, Restaurant>>({})
  const [items, setMenuItems] = useState<Record<string, MenuItem>>({})
  const [orderPrices, setOrderPrices] = useState<Record<string, number>>({})
  const [isLoading, setIsLoading] = useState(true)
  const { toast } = useToast()

  // Fetch menu item details with enhanced error handling
  const fetchMenuItem = async (id: string, token: string): Promise<MenuItem | null> => {
    if (!id) {
      console.error('Empty menu item ID')
      return null
    }

    try {
      const res = await fetch(`${API_URL}/api/menu-items/${id}`, {
        headers: { Authorization: `Bearer ${token}` }
      })
      
      if (!res.ok) {
        console.error(`Failed to fetch menu item ${id}:`, res.status)
        return null
      }
      
      const data = await res.json()
      
      if (!data?.id || typeof data?.price !== 'number') {
        console.error('Invalid menu item data:', data)
        return null
      }
      
      return data
    } catch (error) {
      console.error(`Error fetching menu item ${id}:`, error)
      return null
    }
  }

  // Fetch all data with proper sequencing
  useEffect(() => {
    const fetchData = async () => {
      const token = localStorage.getItem("auth-token")
      if (!token) {
        toast({
          title: "Authentication required",
          description: "Please login to access orders",
          variant: "destructive"
        })
        setIsLoading(false)
        return
      }

      try {
        // 1. Fetch orders
        const ordersRes = await fetch(`${API_URL}/api/delivery/summary`, {
          headers: { Authorization: `Bearer ${token}` }
        })
        
        if (!ordersRes.ok) throw new Error(`Failed to fetch orders: ${ordersRes.status}`)
        
        const ordersData: Order[] = await ordersRes.json()
        console.log('Fetched orders:', ordersData)

        // 2. Get all unique menu item IDs from orders
        const allMenuItemIds = ordersData.flatMap(order => 
          Array.isArray(order.items) ? order.items : []
        ).filter(id => id && typeof id === 'string')
        
        const uniqueMenuItemIds = [...new Set(allMenuItemIds)]
        console.log('Unique menu item IDs:', uniqueMenuItemIds)

        // 3. Fetch all menu items first
        const menuItemPromises = uniqueMenuItemIds.map(id => fetchMenuItem(id, token))
        const menuItemsResults = await Promise.all(menuItemPromises)
        const validMenuItems = menuItemsResults.filter(Boolean) as MenuItem[]
        
        const menuItemsMap = validMenuItems.reduce((acc, item) => {
          acc[item.id] = item
          return acc
        }, {} as Record<string, MenuItem>)
        
        console.log('Menu items map:', menuItemsMap)
        setMenuItems(menuItemsMap)

        // 4. Now calculate prices with the populated menuItemsMap
        const pricesMap = ordersData.reduce((acc, order) => {
          const validItems = Array.isArray(order.items) ? order.items : []
          acc[order.id] = validItems.reduce((sum, itemId) => {
            const itemPrice = menuItemsMap[itemId]?.price || 0
            console.log(`Item ${itemId} price: ${itemPrice}`)
            return sum + itemPrice
          }, 0)
          return acc
        }, {} as Record<string, number>)
        
        console.log('Calculated prices:', pricesMap)
        setOrderPrices(pricesMap)

        // 5. Fetch restaurants
        const restaurantIds = [...new Set(ordersData.map(order => order.restaurantId))]
        const validRestaurantIds = restaurantIds.filter(id => id && typeof id === 'string')
        
        const restaurantPromises = validRestaurantIds.map(id =>
          fetch(`${API_URL}/api/restaurants/single/${id}`, {
            headers: { Authorization: `Bearer ${token}` }
          }).then(res => res.ok ? res.json() : null)
        )
        
        const restaurantsData = await Promise.all(restaurantPromises)
        const restaurantsMap = restaurantsData.reduce((acc, restaurant) => {
          if (restaurant?.id) acc[restaurant.id] = restaurant
          return acc
        }, {} as Record<string, Restaurant>)
        
        setRestaurants(restaurantsMap)
        setOrders(ordersData)

      } catch (error: unknown) {
        const errorMessage = error instanceof Error ? error.message : "An unknown error occurred"
        toast({
          title: "Error",
          description: errorMessage,
          variant: "destructive"
        })
      } finally {
        setIsLoading(false)
      }
    }
    
    fetchData()
  }, [toast])
  // Assign order to current delivery guy
  const assignOrder = async (orderId: string) => {
    const token = localStorage.getItem("auth-token")
    if (!token) {
      toast({
        title: "Authentication required",
        description: "Please login to accept orders",
        variant: "destructive"
      })
      return
    }

    try {
      const assignRes = await fetch(
        `${API_URL}/api/delivery/assign-order/${orderId}/to-deliveryGuy`,
        { method: "PUT", headers: { Authorization: `Bearer ${token}` } }
      )
      
      if (!assignRes.ok) throw new Error(`Assignment failed: ${assignRes.status}`)

      setOrders(orders.map(order => 
        order.id === orderId ? { ...order, deliveryGuyId: "current-user" } : order
      ))

      toast({ title: "Success", description: "Order assigned to you!" })
    } catch (error: unknown) {
      const errorMessage = error instanceof Error ? error.message : "Failed to assign order"
      toast({
        title: "Error",
        description: errorMessage,
        variant: "destructive"
      })
    }
  }

  // Update order status
  const updateStatus = async (orderId: string, newStatus: OrderStatus) => {
    const token = localStorage.getItem("auth-token")
    if (!token) {
      toast({
        title: "Authentication required",
        description: "Please login to update status",
        variant: "destructive"
      })
      return
    }

    try {
      const res = await fetch(
        `${API_URL}/api/delivery/${orderId}/status?status=${newStatus}`,
        { method: "PUT", headers: { Authorization: `Bearer ${token}` } }
      )
      
      if (!res.ok) throw new Error(`Status update failed: ${res.status}`)

      setOrders(orders.map(order => 
        order.id === orderId ? { ...order, status: newStatus } : order
      ))

      toast({
        title: "Updated",
        description: `Status changed to ${newStatus.replace(/_/g, ' ')}`
      })
    } catch (error: unknown) {
      const errorMessage = error instanceof Error ? error.message : "Failed to update status"
      toast({
        title: "Error",
        description: errorMessage,
        variant: "destructive"
      })
    }
  }

  if (isLoading) return <div>Loading orders...</div>

  const availableOrders = orders.filter(order => order.deliveryGuyId !== "current-user")
  const myOrders = orders.filter(order => order.deliveryGuyId === "current-user")

   return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-6">Delivery Orders</h1>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* Available Orders */}
        <div>
          <h2 className="text-xl font-bold mb-4">Available Orders</h2>
          {availableOrders.length > 0 ? (
            availableOrders.map(order => {
              const validMenuItems = Array.isArray(order.items) ? order.items : []
              return (
                <Card key={order.id} className="mb-4">
                  <CardHeader>
                    <CardTitle>Order #{order.id}</CardTitle>
                    <div className="flex gap-2 items-center">
                      <Badge>{order.status}</Badge>
                    
                      <Badge variant="secondary">${orderPrices[order.id]?.toFixed(2) || '0.00'}</Badge>
                    </div>
                  </CardHeader>
                  <CardContent>
                    <p>Restaurant: {restaurants[order.restaurantId]?.name || 'Loading...'}</p>
                    <p>Payment Method: {order.paymentMethod}</p>
                    <div className="mt-2">
                      <p className="font-semibold">Items:</p>
                      <ul className="list-disc pl-5">
                        {validMenuItems.map((itemId, idx) => (
                          <li key={`${itemId}-${idx}`}>
                            {items[itemId]?.name || 'Loading...'} - 
                            ${items[itemId]?.price?.toFixed(2) || '0.00'}
                          </li>
                        ))}
                      </ul>
                    </div>
                    <Button 
                      onClick={() => assignOrder(order.id)}
                      className="mt-4 w-full"
                      disabled={order.status !== 'Prepared'}
                      variant={order.status === 'Prepared' ? 'default' : 'outline'}
                    >
                      {order.status === 'Prepared' ? 'Assign to Me' : 'Waiting for preparation'}
                    </Button>
                  </CardContent>
                </Card>
              )
            })
          ) : (
            <p className="text-muted-foreground">No available orders</p>
          )}
        </div>

        {/* My Assigned Orders */}
        <div>
          <h2 className="text-xl font-bold mb-4">My Orders</h2>
          {myOrders.length > 0 ? (
            myOrders.map(order => {
              const validMenuItems = Array.isArray(order.items) ? order.items : []
              return (
                <Card key={order.id} className="mb-4">
                  <CardHeader>
                    <CardTitle>Order #{order.id}</CardTitle>
                    <div className="flex gap-2 items-center">
                      <Badge>{order.status}</Badge>
                      <Badge variant="secondary">${orderPrices[order.id]?.toFixed(2) || '0.00'}</Badge>
                    </div>
                  </CardHeader>
                  <CardContent>
                    <div className="flex items-center gap-4 mb-4">
                      <Select
                        value={order.status}
                        onValueChange={(value) => updateStatus(order.id, value as OrderStatus)}
                      >
                        <SelectTrigger>
                          <SelectValue />
                        </SelectTrigger>
                        <SelectContent>
                          <SelectItem value="Prepared">Prepared</SelectItem>
                          <SelectItem value="out_for_delivery">Out for Delivery</SelectItem>
                          <SelectItem value="delivered">Delivered</SelectItem>
                        </SelectContent>
                      </Select>
                    </div>
                    <p>Restaurant: {restaurants[order.restaurantId]?.name || 'Loading...'}</p>
                    <p>Payment Method: {order.paymentMethod}</p>
                    <div className="mt-2">
                      <p className="font-semibold">Items:</p>
                      <ul className="list-disc pl-5">
                        {validMenuItems.map((itemId, idx) => (
                          <li key={`${itemId}-${idx}`}>
                            {items[itemId]?.name || 'Loading...'} - 
                            ${items[itemId]?.price?.toFixed(2) || '0.00'}
                          </li>
                        ))}
                      </ul>
                    </div>
                  </CardContent>
                </Card>
              )
            })
          ) : (
            <p className="text-muted-foreground">No assigned orders</p>
          )}
        </div>
      </div>
    </div>
  )
}