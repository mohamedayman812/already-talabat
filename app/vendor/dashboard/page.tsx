"use client"

import { useState, useEffect } from "react"
import Link from "next/link"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { useToast } from "@/hooks/use-toast"

// Mock data
const mockRestaurant = {
  id: "1",
  name: "Pasta Paradise",
  address: "123 Main St, City, Country",
  totalOrders: 156,
  pendingOrders: 3,
  totalRevenue: 4250.75,
}

const mockOrders = [
  {
    id: "ORD-001",
    date: "2023-05-18T14:30:00",
    customer: "John Doe",
    items: [
      { name: "Spaghetti Carbonara", quantity: 1, price: 12.99 },
      { name: "Tiramisu", quantity: 1, price: 7.99 },
    ],
    total: 23.97,
    status: "pending",
  },
  {
    id: "ORD-002",
    date: "2023-05-18T13:45:00",
    customer: "Jane Smith",
    items: [
      { name: "Fettuccine Alfredo", quantity: 1, price: 11.99 },
      { name: "Garlic Bread", quantity: 1, price: 4.99 },
    ],
    total: 18.98,
    status: "pending",
  },
  {
    id: "ORD-003",
    date: "2023-05-18T12:15:00",
    customer: "Mike Johnson",
    items: [
      { name: "Penne Arrabbiata", quantity: 1, price: 10.99 },
      { name: "Caesar Salad", quantity: 1, price: 8.99 },
      { name: "Soda", quantity: 1, price: 2.99 },
    ],
    total: 24.97,
    status: "pending",
  },
  {
    id: "ORD-004",
    date: "2023-05-17T19:30:00",
    customer: "Sarah Williams",
    items: [
      { name: "Margherita Pizza", quantity: 1, price: 14.99 },
      { name: "Tiramisu", quantity: 1, price: 7.99 },
    ],
    total: 24.98,
    status: "completed",
  },
  {
    id: "ORD-005",
    date: "2023-05-17T18:20:00",
    customer: "David Brown",
    items: [
      { name: "Lasagna", quantity: 1, price: 13.99 },
      { name: "Garlic Bread", quantity: 1, price: 4.99 },
      { name: "Soda", quantity: 2, price: 2.99 },
    ],
    total: 26.96,
    status: "completed",
  },
]

export default function VendorDashboard() {
  const [restaurant, setRestaurant] = useState(null)
  const [orders, setOrders] = useState([])
  const [isLoading, setIsLoading] = useState(true)
  const { toast } = useToast()

  useEffect(() => {
    // Check if user is logged in and is a vendor
    const token = localStorage.getItem("auth-token")
    const role = localStorage.getItem("user-role")

    if (!token || role !== "VENDOR") {
      toast({
        title: "Access denied",
        description: "You must be logged in as a vendor to access this page.",
        variant: "destructive",
      })
      // In a real app, you would redirect to login page
      return
    }

    // In a real app, you would fetch restaurant and orders from your API
    // Simulate API call
    setTimeout(() => {
      setRestaurant(mockRestaurant)
      setOrders(mockOrders)
      setIsLoading(false)
    }, 1000)
  }, [toast])

  const getStatusBadge = (status) => {
    switch (status) {
      case "pending":
        return <Badge>Pending</Badge>
      case "preparing":
        return <Badge variant="secondary">Preparing</Badge>
      case "ready":
        return <Badge variant="outline">Ready for Delivery</Badge>
      case "completed":
        return <Badge variant="default">Completed</Badge>
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
      description: `Order #${orderId} has been marked as ${newStatus}.`,
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
      <h1 className="text-3xl font-bold mb-8">Vendor Dashboard</h1>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">Restaurant</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{restaurant.name}</div>
            <p className="text-xs text-muted-foreground mt-1">{restaurant.address}</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">Total Orders</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{restaurant.totalOrders}</div>
            <p className="text-xs text-muted-foreground mt-1">All time orders</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">Pending Orders</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{restaurant.pendingOrders}</div>
            <p className="text-xs text-muted-foreground mt-1">Awaiting preparation</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">Total Revenue</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">${restaurant.totalRevenue.toFixed(2)}</div>
            <p className="text-xs text-muted-foreground mt-1">All time revenue</p>
          </CardContent>
        </Card>
      </div>

      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-bold">Recent Orders</h2>
        <Link href="/vendor/orders">
          <Button variant="outline">View All Orders</Button>
        </Link>
      </div>

      <Tabs defaultValue="pending">
        <TabsList className="mb-6">
          <TabsTrigger value="pending">Pending</TabsTrigger>
          <TabsTrigger value="completed">Completed</TabsTrigger>
        </TabsList>

        <TabsContent value="pending" className="space-y-6">
          {orders.filter((order) => order.status === "pending").length === 0 ? (
            <p className="text-center py-8 text-muted-foreground">No pending orders at the moment.</p>
          ) : (
            orders
              .filter((order) => order.status === "pending")
              .map((order) => (
                <Card key={order.id}>
                  <CardHeader className="pb-2">
                    <div className="flex justify-between items-start">
                      <div>
                        <CardTitle className="text-lg">Order #{order.id}</CardTitle>
                        <CardDescription>
                          {formatDate(order.date)} • {order.customer}
                        </CardDescription>
                      </div>
                      {getStatusBadge(order.status)}
                    </div>
                  </CardHeader>
                  <CardContent>
                    <div className="space-y-4">
                      <div className="space-y-2">
                        {order.items.map((item, index) => (
                          <div key={index} className="flex justify-between text-sm">
                            <span>
                              {item.quantity} x {item.name}
                            </span>
                            <span>${(item.price * item.quantity).toFixed(2)}</span>
                          </div>
                        ))}

                        <div className="flex justify-between font-semibold pt-2 border-t">
                          <span>Total</span>
                          <span>${order.total.toFixed(2)}</span>
                        </div>
                      </div>

                      <div className="flex justify-between items-center">
                        <Button variant="outline" size="sm" onClick={() => updateOrderStatus(order.id, "preparing")}>
                          Start Preparing
                        </Button>

                        <Link href={`/vendor/orders/${order.id}`}>
                          <Button variant="ghost" size="sm">
                            View Details
                          </Button>
                        </Link>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              ))
          )}
        </TabsContent>

        <TabsContent value="completed" className="space-y-6">
          {orders.filter((order) => order.status === "completed").length === 0 ? (
            <p className="text-center py-8 text-muted-foreground">No completed orders to display.</p>
          ) : (
            orders
              .filter((order) => order.status === "completed")
              .map((order) => (
                <Card key={order.id}>
                  <CardHeader className="pb-2">
                    <div className="flex justify-between items-start">
                      <div>
                        <CardTitle className="text-lg">Order #{order.id}</CardTitle>
                        <CardDescription>
                          {formatDate(order.date)} • {order.customer}
                        </CardDescription>
                      </div>
                      {getStatusBadge(order.status)}
                    </div>
                  </CardHeader>
                  <CardContent>
                    <div className="space-y-4">
                      <div className="space-y-2">
                        {order.items.map((item, index) => (
                          <div key={index} className="flex justify-between text-sm">
                            <span>
                              {item.quantity} x {item.name}
                            </span>
                            <span>${(item.price * item.quantity).toFixed(2)}</span>
                          </div>
                        ))}

                        <div className="flex justify-between font-semibold pt-2 border-t">
                          <span>Total</span>
                          <span>${order.total.toFixed(2)}</span>
                        </div>
                      </div>

                      <div className="flex justify-end">
                        <Link href={`/vendor/orders/${order.id}`}>
                          <Button variant="ghost" size="sm">
                            View Details
                          </Button>
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
