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
    status: "preparing",
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
    status: "ready",
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
  {
    id: "ORD-006",
    date: "2023-05-17T17:10:00",
    customer: "Emily Davis",
    items: [
      { name: "Spaghetti Bolognese", quantity: 1, price: 11.99 },
      { name: "Caprese Salad", quantity: 1, price: 9.99 },
    ],
    total: 23.98,
    status: "completed",
  },
  {
    id: "ORD-007",
    date: "2023-05-16T20:05:00",
    customer: "Michael Wilson",
    items: [
      { name: "Pepperoni Pizza", quantity: 1, price: 15.99 },
      { name: "Chicken Wings", quantity: 1, price: 8.99 },
      { name: "Soda", quantity: 2, price: 2.99 },
    ],
    total: 32.96,
    status: "completed",
  },
]

export default function VendorOrdersPage() {
  const [orders, setOrders] = useState([])
  const [isLoading, setIsLoading] = useState(true)
  const [searchQuery, setSearchQuery] = useState("")
  const [activeTab, setActiveTab] = useState("all")
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

    // In a real app, you would fetch orders from your API
    // Simulate API call
    setTimeout(() => {
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

  const filteredOrders = orders.filter((order) => {
    const matchesSearch =
      order.id.toLowerCase().includes(searchQuery.toLowerCase()) ||
      order.customer.toLowerCase().includes(searchQuery.toLowerCase())

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
      <h1 className="text-3xl font-bold mb-8">Manage Orders</h1>

      <div className="mb-6">
        <div className="relative">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
          <Input
            placeholder="Search orders by ID or customer name..."
            className="pl-10"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
          />
        </div>
      </div>

      <Tabs defaultValue="all" onValueChange={setActiveTab}>
        <TabsList className="mb-6">
          <TabsTrigger value="all">All Orders</TabsTrigger>
          <TabsTrigger value="pending">Pending</TabsTrigger>
          <TabsTrigger value="preparing">Preparing</TabsTrigger>
          <TabsTrigger value="ready">Ready</TabsTrigger>
          <TabsTrigger value="completed">Completed</TabsTrigger>
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

        {["pending", "preparing", "ready", "completed"].map((status) => (
          <TabsContent key={status} value={status} className="space-y-6">
            {filteredOrders.length === 0 ? (
              <p className="text-center py-8 text-muted-foreground">No {status} orders found matching your search.</p>
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

          <div className="flex flex-wrap gap-2 justify-between items-center">
            <div className="space-x-2">
              {order.status === "pending" && (
                <Button size="sm" onClick={() => updateOrderStatus(order.id, "preparing")}>
                  Start Preparing
                </Button>
              )}

              {order.status === "preparing" && (
                <Button size="sm" onClick={() => updateOrderStatus(order.id, "ready")}>
                  Mark as Ready
                </Button>
              )}

              {order.status === "ready" && (
                <Button size="sm" onClick={() => updateOrderStatus(order.id, "completed")}>
                  Complete Order
                </Button>
              )}
            </div>

            <Link href={`/vendor/orders/${order.id}`}>
              <Button variant="outline" size="sm">
                View Details
              </Button>
            </Link>
          </div>
        </div>
      </CardContent>
    </Card>
  )
}
