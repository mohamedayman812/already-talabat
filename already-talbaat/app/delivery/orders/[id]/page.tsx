"use client"

import { useState, useEffect } from "react"
import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Separator } from "@/components/ui/separator"
import { useToast } from "@/hooks/use-toast"
import { ArrowLeft, MapPin, Phone, Navigation, Clock } from "lucide-react"

// Mock order data
const mockOrder = {
  id: "ORD-001",
  date: "2023-05-15T14:30:00",
  restaurant: {
    id: "1",
    name: "Pasta Paradise",
    address: "123 Main St, City, Country",
    phone: "+1234567890",
  },
  customer: {
    id: "1",
    name: "John Doe",
    address: "456 Oak Ave, City, Country",
    phone: "+0987654321",
  },
  items: [
    { id: "101", name: "Spaghetti Carbonara", quantity: 1, price: 12.99 },
    { id: "301", name: "Tiramisu", quantity: 1, price: 7.99 },
    { id: "401", name: "Sparkling Water", quantity: 1, price: 2.99 },
  ],
  subtotal: 23.97,
  deliveryFee: 2.99,
  tax: 2.4,
  total: 29.36,
  status: "ready_for_pickup",
  paymentMethod: "card",
  notes: "Please ring the doorbell twice.",
}

export default function DeliveryOrderDetailsPage({ params }) {
  const [order, setOrder] = useState(null)
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

    // In a real app, you would fetch the order from your API
    // Simulate API call
    setTimeout(() => {
      setOrder({ ...mockOrder, id: params.id })
      setIsLoading(false)
    }, 1000)
  }, [params.id, toast])

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

  const updateOrderStatus = (newStatus) => {
    // In a real app, you would call your API to update the order status
    setOrder((prev) => ({ ...prev, status: newStatus }))

    toast({
      title: "Order status updated",
      description: `Order #${order.id} has been marked as ${newStatus.replace("_", " ")}.`,
    })
  }

  if (isLoading) {
    return (
      <div className="container mx-auto px-4 py-8 flex justify-center items-center min-h-[60vh]">
        <p>Loading order details...</p>
      </div>
    )
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="flex items-center mb-8">
        <Link href="/delivery/orders">
          <Button variant="ghost" size="sm" className="gap-1">
            <ArrowLeft className="h-4 w-4" /> Back to Orders
          </Button>
        </Link>
        <h1 className="text-3xl font-bold ml-4">Order #{order.id}</h1>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <div className="lg:col-span-2 space-y-6">
          <Card>
            <CardHeader className="pb-2">
              <div className="flex justify-between items-start">
                <div>
                  <CardTitle>Order Status</CardTitle>
                  <CardDescription>{formatDate(order.date)}</CardDescription>
                </div>
                {getStatusBadge(order.status)}
              </div>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                <div className="flex flex-wrap gap-2">
                  {order.status === "ready_for_pickup" && (
                    <Button onClick={() => updateOrderStatus("out_for_delivery")}>Pick Up Order</Button>
                  )}

                  {order.status === "out_for_delivery" && (
                    <Button onClick={() => updateOrderStatus("delivered")}>Mark as Delivered</Button>
                  )}
                </div>
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Order Details</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="space-y-2">
                {order.items.map((item, index) => (
                  <div key={index} className="flex justify-between">
                    <div>
                      <span className="font-medium">
                        {item.quantity} x {item.name}
                      </span>
                    </div>
                    <span>${(item.price * item.quantity).toFixed(2)}</span>
                  </div>
                ))}
              </div>

              <Separator />

              <div className="space-y-1">
                <div className="flex justify-between text-sm">
                  <span>Subtotal</span>
                  <span>${order.subtotal.toFixed(2)}</span>
                </div>
                <div className="flex justify-between text-sm">
                  <span>Delivery Fee</span>
                  <span>${order.deliveryFee.toFixed(2)}</span>
                </div>
                <div className="flex justify-between text-sm">
                  <span>Tax</span>
                  <span>${order.tax.toFixed(2)}</span>
                </div>
                <Separator className="my-2" />
                <div className="flex justify-between font-bold">
                  <span>Total</span>
                  <span>${order.total.toFixed(2)}</span>
                </div>
              </div>

              <div className="pt-2">
                <p className="text-sm text-muted-foreground">
                  Payment Method: <span className="font-medium capitalize">{order.paymentMethod}</span>
                </p>
              </div>
            </CardContent>
          </Card>
        </div>

        <div className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle>Restaurant Information</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="space-y-2">
                <h3 className="font-semibold text-lg">{order.restaurant.name}</h3>
                <div className="flex items-start gap-2 text-sm">
                  <MapPin className="h-4 w-4 mt-0.5 text-muted-foreground" />
                  <span>{order.restaurant.address}</span>
                </div>
                <div className="flex items-center gap-2 text-sm">
                  <Phone className="h-4 w-4 text-muted-foreground" />
                  <span>{order.restaurant.phone}</span>
                </div>
                <Button variant="outline" size="sm" className="w-full mt-2 gap-2">
                  <Navigation className="h-4 w-4" /> Navigate to Restaurant
                </Button>
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Customer Information</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="space-y-2">
                <h3 className="font-semibold text-lg">{order.customer.name}</h3>
                <div className="flex items-start gap-2 text-sm">
                  <MapPin className="h-4 w-4 mt-0.5 text-muted-foreground" />
                  <span>{order.customer.address}</span>
                </div>
                <div className="flex items-center gap-2 text-sm">
                  <Phone className="h-4 w-4 text-muted-foreground" />
                  <span>{order.customer.phone}</span>
                </div>
                <Button variant="outline" size="sm" className="w-full mt-2 gap-2">
                  <Navigation className="h-4 w-4" /> Navigate to Customer
                </Button>
              </div>

              {order.notes && (
                <div className="bg-muted p-3 rounded-md">
                  <p className="font-semibold text-sm">Delivery Notes:</p>
                  <p className="text-sm">{order.notes}</p>
                </div>
              )}
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Delivery Information</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="space-y-2">
                <div className="flex items-center gap-2">
                  <Clock className="h-4 w-4 text-muted-foreground" />
                  <span>Estimated delivery time: 15-20 minutes</span>
                </div>
                <p className="text-sm text-muted-foreground">
                  {order.paymentMethod === "cash"
                    ? "Collect cash payment of $" + order.total.toFixed(2) + " upon delivery."
                    : "Payment has been processed online. No payment collection needed."}
                </p>
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  )
}
