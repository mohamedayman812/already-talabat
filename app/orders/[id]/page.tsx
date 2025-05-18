"use client"

import { useState, useEffect } from "react"
import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Separator } from "@/components/ui/separator"
import { useToast } from "@/hooks/use-toast"
import { ArrowLeft, MapPin, Phone, Clock, CheckCircle2 } from "lucide-react"

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
  status: "delivered",
  paymentMethod: "card",
  deliveryPerson: {
    id: "1",
    name: "Mike Davis",
    phone: "+1122334455",
  },
  timeline: [
    { status: "placed", time: "2023-05-15T14:30:00", description: "Order placed" },
    { status: "confirmed", time: "2023-05-15T14:35:00", description: "Order confirmed by restaurant" },
    { status: "preparing", time: "2023-05-15T14:40:00", description: "Restaurant is preparing your order" },
    { status: "ready", time: "2023-05-15T15:00:00", description: "Order is ready for pickup" },
    { status: "picked_up", time: "2023-05-15T15:10:00", description: "Order picked up by delivery person" },
    { status: "delivered", time: "2023-05-15T15:30:00", description: "Order delivered" },
  ],
}

export default function OrderDetailsPage({ params }) {
  const [order, setOrder] = useState(null)
  const [isLoading, setIsLoading] = useState(true)
  const [userRole, setUserRole] = useState(null)
  const { toast } = useToast()

  useEffect(() => {
    // Check if user is logged in
    const token = localStorage.getItem("auth-token")
    if (!token) {
      toast({
        title: "Authentication required",
        description: "Please log in to view order details.",
        variant: "destructive",
      })
      // In a real app, you would redirect to login page
      return
    }

    const role = localStorage.getItem("user-role")
    setUserRole(role)

    // In a real app, you would fetch the order from your API
    // Simulate API call
    setTimeout(() => {
      setOrder({ ...mockOrder, id: params.id })
      setIsLoading(false)
    }, 1000)
  }, [params.id, toast])

  const getStatusBadge = (status) => {
    switch (status) {
      case "pending":
        return <Badge>Pending</Badge>
      case "confirmed":
        return <Badge variant="outline">Confirmed</Badge>
      case "preparing":
        return <Badge variant="secondary">Preparing</Badge>
      case "ready":
        return <Badge variant="outline">Ready for Pickup</Badge>
      case "picked_up":
        return <Badge variant="secondary">Out for Delivery</Badge>
      case "delivered":
        return <Badge variant="default">Delivered</Badge>
      case "cancelled":
        return <Badge variant="destructive">Cancelled</Badge>
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
        <Link href="/orders">
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
              <div className="relative">
                <ol className="relative border-l border-muted-foreground/20 ml-3 space-y-6 py-2">
                  {order.timeline.map((event, index) => (
                    <li key={index} className="ml-6">
                      <span
                        className={`absolute flex items-center justify-center w-6 h-6 rounded-full -left-3 ${
                          index === order.timeline.length - 1
                            ? "bg-primary text-primary-foreground"
                            : "bg-muted text-muted-foreground"
                        }`}
                      >
                        <CheckCircle2 className="w-3 h-3" />
                      </span>
                      <h3 className="font-medium">{event.description}</h3>
                      <p className="text-sm text-muted-foreground">{formatDate(event.time)}</p>
                    </li>
                  ))}
                </ol>
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

          {userRole === "CUSTOMER" && order.status === "delivered" && (
            <div className="flex justify-end">
              <Link href={`/feedback/${order.id}`}>
                <Button>Leave Feedback</Button>
              </Link>
            </div>
          )}
        </div>

        <div className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle>Restaurant</CardTitle>
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
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Delivery Information</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="space-y-2">
                <h3 className="font-semibold">Delivery Address</h3>
                <div className="flex items-start gap-2 text-sm">
                  <MapPin className="h-4 w-4 mt-0.5 text-muted-foreground" />
                  <span>{order.customer.address}</span>
                </div>
              </div>

              {order.deliveryPerson && (
                <div className="space-y-2">
                  <h3 className="font-semibold">Delivery Person</h3>
                  <p className="text-sm">{order.deliveryPerson.name}</p>
                  <div className="flex items-center gap-2 text-sm">
                    <Phone className="h-4 w-4 text-muted-foreground" />
                    <span>{order.deliveryPerson.phone}</span>
                  </div>
                </div>
              )}

              <div className="space-y-2">
                <h3 className="font-semibold">Estimated Delivery Time</h3>
                <div className="flex items-center gap-2 text-sm">
                  <Clock className="h-4 w-4 text-muted-foreground" />
                  <span>
                    {order.status === "delivered"
                      ? "Delivered at " + formatDate(order.timeline[order.timeline.length - 1].time)
                      : "25-35 minutes"}
                  </span>
                </div>
              </div>
            </CardContent>
          </Card>

          {userRole === "CUSTOMER" && (
            <Card>
              <CardHeader>
                <CardTitle>Need Help?</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <p className="text-sm">Having an issue with your order? Contact our support team.</p>
                <Button variant="outline" className="w-full">
                  Contact Support
                </Button>
              </CardContent>
            </Card>
          )}
        </div>
      </div>
    </div>
  )
}
