"use client"

import { useState, useEffect } from "react"
import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { useToast } from "@/hooks/use-toast"

// Mock order data
const mockOrders = [
  {
    id: "ORD-001",
    date: "2023-05-15T14:30:00",
    restaurant: "Pasta Paradise",
    items: [
      { name: "Spaghetti Carbonara", quantity: 1, price: 12.99 },
      { name: "Tiramisu", quantity: 1, price: 7.99 },
    ],
    total: 23.97,
    status: "delivered",
    deliveryPerson: "John Smith",
  },
  {
    id: "ORD-002",
    date: "2023-05-10T18:45:00",
    restaurant: "Burger Bliss",
    items: [
      { name: "Cheeseburger", quantity: 2, price: 9.99 },
      { name: "French Fries", quantity: 1, price: 4.99 },
      { name: "Chocolate Shake", quantity: 1, price: 5.99 },
    ],
    total: 30.96,
    status: "delivered",
    deliveryPerson: "Sarah Johnson",
  },
  {
    id: "ORD-003",
    date: "2023-05-17T12:15:00",
    restaurant: "Sushi Sensation",
    items: [
      { name: "California Roll", quantity: 1, price: 8.99 },
      { name: "Salmon Nigiri", quantity: 2, price: 6.99 },
      { name: "Miso Soup", quantity: 1, price: 3.99 },
    ],
    total: 26.96,
    status: "in_progress",
    deliveryPerson: "Mike Davis",
  },
  {
    id: "ORD-004",
    date: "2023-05-18T19:30:00",
    restaurant: "Pizza Palace",
    items: [
      { name: "Pepperoni Pizza", quantity: 1, price: 15.99 },
      { name: "Garlic Bread", quantity: 1, price: 4.99 },
      { name: "Soda", quantity: 2, price: 2.49 },
    ],
    total: 25.96,
    status: "pending",
    deliveryPerson: null,
  },
]

const getStatusBadge = (status) => {
  switch (status) {
    case "pending":
      return <Badge variant="outline">Pending</Badge>
    case "in_progress":
      return <Badge variant="secondary">In Progress</Badge>
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

export default function OrdersPage() {
  const [orders, setOrders] = useState([])
  const [isLoading, setIsLoading] = useState(true)
  const { toast } = useToast()

  useEffect(() => {
    // Check if user is logged in
    const token = localStorage.getItem("auth-token")
    if (!token) {
      toast({
        title: "Authentication required",
        description: "Please log in to view your orders.",
        variant: "destructive",
      })
      // In a real app, you would redirect to login page
      return
    }

    // In a real app, you would fetch orders from your API
    // Simulate API call
    setTimeout(() => {
      setOrders(mockOrders)
      setIsLoading(false)
    }, 1000)
  }, [toast])

  if (isLoading) {
    return (
      <div className="container mx-auto px-4 py-8 flex justify-center items-center min-h-[60vh]">
        <p>Loading your orders...</p>
      </div>
    )
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">Your Orders</h1>

      <Tabs defaultValue="all">
        <TabsList className="mb-6">
          <TabsTrigger value="all">All Orders</TabsTrigger>
          <TabsTrigger value="pending">Pending</TabsTrigger>
          <TabsTrigger value="in_progress">In Progress</TabsTrigger>
          <TabsTrigger value="delivered">Delivered</TabsTrigger>
        </TabsList>

        <TabsContent value="all" className="space-y-6">
          {orders.length === 0 ? (
            <p className="text-center py-8 text-muted-foreground">You don't have any orders yet.</p>
          ) : (
            orders.map((order) => <OrderCard key={order.id} order={order} />)
          )}
        </TabsContent>

        {["pending", "in_progress", "delivered"].map((status) => (
          <TabsContent key={status} value={status} className="space-y-6">
            {orders.filter((order) => order.status === status).length === 0 ? (
              <p className="text-center py-8 text-muted-foreground">
                You don't have any {status.replace("_", " ")} orders.
              </p>
            ) : (
              orders
                .filter((order) => order.status === status)
                .map((order) => <OrderCard key={order.id} order={order} />)
            )}
          </TabsContent>
        ))}
      </Tabs>
    </div>
  )
}

function OrderCard({ order }) {
  return (
    <Card>
      <CardHeader className="pb-2">
        <div className="flex justify-between items-start">
          <div>
            <CardTitle className="text-lg">{order.restaurant}</CardTitle>
            <p className="text-sm text-muted-foreground">
              Order #{order.id} • {formatDate(order.date)}
            </p>
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

          {order.status === "in_progress" && order.deliveryPerson && (
            <div className="bg-muted p-3 rounded-md text-sm">
              <p className="font-semibold">Delivery Information</p>
              <p>Your order is on the way with {order.deliveryPerson}</p>
            </div>
          )}

          <div className="flex justify-between items-center">
            <Link href={`/orders/${order.id}`}>
              <Button variant="outline" size="sm">
                View Details
              </Button>
            </Link>

            {order.status === "delivered" && (
              <Link href={`/feedback/${order.id}`}>
                <Button variant="outline" size="sm">
                  Leave Feedback
                </Button>
              </Link>
            )}
          </div>
        </div>
      </CardContent>
    </Card>
  )
}
