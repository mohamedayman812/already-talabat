"use client"

import { useState, useEffect } from "react"
import Link from "next/link"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { useToast } from "@/hooks/use-toast"

// Mock data
const mockDeliveryGuy = {
  id: "1",
  name: "John Smith",
  totalDeliveries: 156,
  activeDeliveries: 2,
  completedToday: 8,
}

const mockOrders = [
  {
    id: "ORD-001",
    date: "2023-05-18T14:30:00",
    customer: {
      name: "Alice Johnson",
      address: "123 Main St, Apt 4B, City",
      phone: "+1234567890",
    },
    restaurant: {
      name: "Pasta Paradise",
      address: "456 Oak Ave, City",
    },
    items: [
      { name: "Spaghetti Carbonara", quantity: 1 },
      { name: "Tiramisu", quantity: 1 },
    ],
    total: 23.97,
    status: "ready_for_pickup",
  },
  {
    id: "ORD-002",
    date: "2023-05-18T13:45:00",
    customer: {
      name: "Bob Williams",
      address: "789 Pine Rd, City",
      phone: "+1987654321",
    },
    restaurant: {
      name: "Burger Bliss",
      address: "101 Elm St, City",
    },
    items: [
      { name: "Cheeseburger", quantity: 2 },
      { name: "French Fries", quantity: 1 },
      { name: "Chocolate Shake", quantity: 1 },
    ],
    total: 30.96,
    status: "out_for_delivery",
  },
  {
    id: "ORD-003",
    date: "2023-05-18T12:15:00",
    customer: {
      name: "Charlie Brown",
      address: "202 Maple Dr, City",
      phone: "+1122334455",
    },
    restaurant: {
      name: "Sushi Sensation",
      address: "303 Cedar Ln, City",
    },
    items: [
      { name: "California Roll", quantity: 1 },
      { name: "Salmon Nigiri", quantity: 2 },
      { name: "Miso Soup", quantity: 1 },
    ],
    total: 26.96,
    status: "delivered",
  },
  {
    id: "ORD-004",
    date: "2023-05-17T19:30:00",
    customer: {
      name: "Diana Evans",
      address: "404 Birch Blvd, City",
      phone: "+1567891234",
    },
    restaurant: {
      name: "Pizza Palace",
      address: "505 Walnut Way, City",
    },
    items: [
      { name: "Pepperoni Pizza", quantity: 1 },
      { name: "Garlic Bread", quantity: 1 },
      { name: "Soda", quantity: 2 },
    ],
    total: 25.96,
    status: "delivered",
  },
]

export default function DeliveryDashboard() {
  const [deliveryGuy, setDeliveryGuy] = useState(null)
  const [orders, setOrders] = useState([])
  const [isLoading, setIsLoading] = useState(true)
  const { toast } = useToast()

  useEffect(() => {
    // Check if user is logged in and is a delivery person
    const token = localStorage.getItem("auth-token")
    const role = localStorage.getItem("user-role")

    if (!token || role !== "DELIVERY") {
      toast({
        title: "Access denied",
        description: "You must be logged in as a delivery person to access this page.",
        variant: "destructive",
      })
      // In a real app, you would redirect to login page
      return
    }

    // In a real app, you would fetch delivery guy info and orders from your API
    // Simulate API call
    setTimeout(() => {
      setDeliveryGuy(mockDeliveryGuy)
      setOrders(mockOrders)
      setIsLoading(false)
    }, 1000)
  }, [toast])

  const getStatusBadge = (status) => {
    switch (status) {
      case "ready_for_pickup":
        return <Badge variant="outline">Ready for Pickup</Badge>
      case "out_for_delivery":
        return <Badge variant="secondary">Out for Delivery</Badge>
      case "delivered":
        return <Badge variant="default">Delivered</Badge>
      default:
        return <Badge variant="outline">{status}</Badge>
    }
  }

  const formatDate = (dateString) => {
    const date = new Date(dateString)
    return new Intl.DateTimeFormat("en-US", {
      dateStyle: "medium",
      timeStyle: "short",
    }).format(date)
  }

  const updateOrderStatus = (orderId, newStatus) => {
    // In a real app, you would call your API to update the order status
    setOrders(orders.map((order) => (order.id === orderId ? { ...order, status: newStatus } : order)))

    toast({
      title: "Order status updated",
      description: `Order #${orderId} has been marked as ${newStatus.replace("_", " ")}.`,
    })
  }

  if (isLoading) {
    return (
      <div className="container mx-auto px-4 py-8 flex justify-center items-center min-h-[60vh]">
        <p>Loading dashboard...</p>
      </div>
    )
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">Delivery Dashboard</h1>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">Active Deliveries</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{deliveryGuy.activeDeliveries}</div>
            <p className="text-xs text-muted-foreground mt-1">Orders in progress</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">Completed Today</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{deliveryGuy.completedToday}</div>
            <p className="text-xs text-muted-foreground mt-1">Deliveries completed today</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">Total Deliveries</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{deliveryGuy.totalDeliveries}</div>
            <p className="text-xs text-muted-foreground mt-1">All time deliveries</p>
          </CardContent>
        </Card>
      </div>

      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-bold">Orders</h2>
        <Link href="/delivery/orders">
          <Button variant="outline">View All Orders</Button>
        </Link>
      </div>

      <Tabs defaultValue="active">
        <TabsList className="mb-6">
          <TabsTrigger value="active">Active</TabsTrigger>
          <TabsTrigger value="completed">Completed</TabsTrigger>
        </TabsList>

        <TabsContent value="active" className="space-y-6">
          {orders.filter((order) => order.status !== "delivered").length === 0 ? (
            <p className="text-center py-8 text-muted-foreground">No active orders at the moment.</p>
          ) : (
            orders
              .filter((order) => order.status !== "delivered")
              .map((order) => (
                <Card key={order.id}>
                  <CardHeader className="pb-2">
                    <div className="flex justify-between items-start">
                      <div>
                        <CardTitle className="text-lg">Order #{order.id}</CardTitle>
                        <CardDescription>{formatDate(order.date)}</CardDescription>
                      </div>
                      {getStatusBadge(order.status)}
                    </div>
                  </CardHeader>
                  <CardContent>
                    <div className="space-y-4">
                      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div>
                          <h3 className="font-semibold text-sm mb-1">Restaurant</h3>
                          <p className="text-sm">{order.restaurant.name}</p>
                          <p className="text-xs text-muted-foreground">{order.restaurant.address}</p>
                        </div>
                        <div>
                          <h3 className="font-semibold text-sm mb-1">Customer</h3>
                          <p className="text-sm">{order.customer.name}</p>
                          <p className="text-xs text-muted-foreground">{order.customer.address}</p>
                          <p className="text-xs text-muted-foreground">{order.customer.phone}</p>
                        </div>
                      </div>

                      <div>
                        <h3 className="font-semibold text-sm mb-1">Items</h3>
                        <ul className="text-sm space-y-1">
                          {order.items.map((item, index) => (
                            <li key={index}>
                              {item.quantity} x {item.name}
                            </li>
                          ))}
                        </ul>
                        <div className="flex justify-between font-semibold mt-2 pt-2 border-t text-sm">
                          <span>Total</span>
                          <span>${order.total.toFixed(2)}</span>
                        </div>
                      </div>

                      <div className="flex justify-between items-center">
                        {order.status === "ready_for_pickup" ? (
                          <Button onClick={() => updateOrderStatus(order.id, "out_for_delivery")}>Pick Up Order</Button>
                        ) : order.status === "out_for_delivery" ? (
                          <Button onClick={() => updateOrderStatus(order.id, "delivered")}>Mark as Delivered</Button>
                        ) : null}

                        <Link href={`/delivery/orders/${order.id}`}>
                          <Button variant="outline">View Details</Button>
                        </Link>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              ))
          )}
        </TabsContent>

        <TabsContent value="completed" className="space-y-6">
          {orders.filter((order) => order.status === "delivered").length === 0 ? (
            <p className="text-center py-8 text-muted-foreground">No completed orders to display.</p>
          ) : (
            orders
              .filter((order) => order.status === "delivered")
              .map((order) => (
                <Card key={order.id}>
                  <CardHeader className="pb-2">
                    <div className="flex justify-between items-start">
                      <div>
                        <CardTitle className="text-lg">Order #{order.id}</CardTitle>
                        <CardDescription>{formatDate(order.date)}</CardDescription>
                      </div>
                      {getStatusBadge(order.status)}
                    </div>
                  </CardHeader>
                  <CardContent>
                    <div className="space-y-4">
                      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div>
                          <h3 className="font-semibold text-sm mb-1">Restaurant</h3>
                          <p className="text-sm">{order.restaurant.name}</p>
                          <p className="text-xs text-muted-foreground">{order.restaurant.address}</p>
                        </div>
                        <div>
                          <h3 className="font-semibold text-sm mb-1">Customer</h3>
                          <p className="text-sm">{order.customer.name}</p>
                          <p className="text-xs text-muted-foreground">{order.customer.address}</p>
                        </div>
                      </div>

                      <div>
                        <h3 className="font-semibold text-sm mb-1">Items</h3>
                        <ul className="text-sm space-y-1">
                          {order.items.map((item, index) => (
                            <li key={index}>
                              {item.quantity} x {item.name}
                            </li>
                          ))}
                        </ul>
                        <div className="flex justify-between font-semibold mt-2 pt-2 border-t text-sm">
                          <span>Total</span>
                          <span>${order.total.toFixed(2)}</span>
                        </div>
                      </div>

                      <div className="flex justify-end">
                        <Link href={`/delivery/orders/${order.id}`}>
                          <Button variant="outline">View Details</Button>
                        </Link>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              ))
          )}
        </TabsContent>
      </Tabs>
    </div>
  )
}
