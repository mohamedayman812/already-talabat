"use client"

import { useState, useEffect } from "react"
import Link from "next/link"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Input } from "@/components/ui/input"
import { Search } from "lucide-react"
import { useToast } from "@/hooks/use-toast"

// Mock orders data
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
  {
    id: "ORD-005",
    date: "2023-05-17T18:20:00",
    customer: {
      name: "Edward Miller",
      address: "606 Spruce St, City",
      phone: "+1654789321",
    },
    restaurant: {
      name: "Taco Time",
      address: "707 Fir Ave, City",
    },
    items: [
      { name: "Beef Tacos", quantity: 3 },
      { name: "Nachos", quantity: 1 },
      { name: "Horchata", quantity: 2 },
    ],
    total: 28.97,
    status: "delivered",
  },
  {
    id: "ORD-006",
    date: "2023-05-16T15:45:00",
    customer: {
      name: "Fiona Garcia",
      address: "808 Pine St, City",
      phone: "+1321456987",
    },
    restaurant: {
      name: "Curry Corner",
      address: "909 Oak Ln, City",
    },
    items: [
      { name: "Chicken Tikka Masala", quantity: 1 },
      { name: "Garlic Naan", quantity: 2 },
      { name: "Mango Lassi", quantity: 1 },
    ],
    total: 32.98,
    status: "delivered",
  },
]

export default function DeliveryOrdersPage() {
  const [orders, setOrders] = useState([])
  const [isLoading, setIsLoading] = useState(true)
  const [searchQuery, setSearchQuery] = useState("")
  const [activeTab, setActiveTab] = useState("all")
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

    // In a real app, you would fetch orders from your API
    // Simulate API call
    setTimeout(() => {
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

  const filteredOrders = orders.filter((order) => {
    const matchesSearch =
      order.id.toLowerCase().includes(searchQuery.toLowerCase()) ||
      order.customer.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      order.restaurant.name.toLowerCase().includes(searchQuery.toLowerCase())

    if (activeTab === "all") return matchesSearch
    return matchesSearch && order.status === activeTab
  })

  if (isLoading) {
    return (
      <div className="container mx-auto px-4 py-8 flex justify-center items-center min-h-[60vh]">
        <p>Loading orders...</p>
      </div>
    )
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">Delivery Orders</h1>

      <div className="mb-6">
        <div className="relative">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
          <Input
            placeholder="Search orders by ID, customer, or restaurant..."
            className="pl-10"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
          />
        </div>
      </div>

      <Tabs defaultValue="all" onValueChange={setActiveTab}>
        <TabsList className="mb-6">
          <TabsTrigger value="all">All Orders</TabsTrigger>
          <TabsTrigger value="ready_for_pickup">Ready for Pickup</TabsTrigger>
          <TabsTrigger value="out_for_delivery">Out for Delivery</TabsTrigger>
          <TabsTrigger value="delivered">Delivered</TabsTrigger>
        </TabsList>

        <TabsContent value="all" className="space-y-6">
          {filteredOrders.length === 0 ? (
            <p className="text-center py-8 text-muted-foreground">No orders found matching your search.</p>
          ) : (
            filteredOrders.map((order) => (
              <OrderCard
                key={order.id}
                order={order}
                getStatusBadge={getStatusBadge}
                formatDate={formatDate}
                updateOrderStatus={updateOrderStatus}
              />
            ))
          )}
        </TabsContent>

        {["ready_for_pickup", "out_for_delivery", "delivered"].map((status) => (
          <TabsContent key={status} value={status} className="space-y-6">
            {filteredOrders.length === 0 ? (
              <p className="text-center py-8 text-muted-foreground">
                No {status.replace("_", " ")} orders found matching your search.
              </p>
            ) : (
              filteredOrders.map((order) => (
                <OrderCard
                  key={order.id}
                  order={order}
                  getStatusBadge={getStatusBadge}
                  formatDate={formatDate}
                  updateOrderStatus={updateOrderStatus}
                />
              ))
            )}
          </TabsContent>
        ))}
      </Tabs>
    </div>
  )
}

function OrderCard({ order, getStatusBadge, formatDate, updateOrderStatus }) {
  return (
    <Card>
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
            ) : (
              <div></div>
            )}

            <Link href={`/delivery/orders/${order.id}`}>
              <Button variant="outline">View Details</Button>
            </Link>
          </div>
        </div>
      </CardContent>
    </Card>
  )
}
