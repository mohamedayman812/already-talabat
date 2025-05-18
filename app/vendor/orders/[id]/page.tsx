"use client"

import { useState, useEffect } from "react"
import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Separator } from "@/components/ui/separator"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { useToast } from "@/hooks/use-toast"
import { ArrowLeft, MapPin, Phone, Clock } from "lucide-react"

// Mock order data
const mockOrder = {
  id: "ORD-001",
  date: "2023-05-15T14:30:00",
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
  status: "pending",
  paymentMethod: "card",
  notes: "Please include extra napkins and utensils.",
}

// Mock delivery personnel
const mockDeliveryPersonnel = [
  { id: "1", name: "Mike Davis" },
  { id: "2", name: "Sarah Johnson" },
  { id: "3", name: "David Brown" },
]

export default function VendorOrderDetailsPage({ params }) {
  const [order, setOrder] = useState(null)
  const [isLoading, setIsLoading] = useState(true)
  const [selectedDeliveryPerson, setSelectedDeliveryPerson] = useState("")
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
      case "preparing":
        return <Badge variant="secondary">Preparing</Badge>
      case "ready":
        return <Badge variant="outline">Ready for Pickup</Badge>
      case "completed":
        return <Badge variant="default">Completed</Badge>
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

  const updateOrderStatus = (newStatus) => {
    // In a real app, you would call your API to update the order status
    setOrder((prev) => ({ ...prev, status: newStatus }))

    toast({
      title: "Order status updated",
      description: `Order #${order.id} has been marked as ${newStatus}.`,
    })
  }

  const assignDeliveryPerson = () => {
    if (!selectedDeliveryPerson) {
      toast({
        title: "Selection required",
        description: "Please select a delivery person.",
        variant: "destructive",
      })
      return
    }

    // In a real app, you would call your API to assign the delivery person
    const deliveryPerson = mockDeliveryPersonnel.find((dp) => dp.id === selectedDeliveryPerson)

    toast({
      title: "Delivery person assigned",
      description: `${deliveryPerson.name} has been assigned to this order.`,
    })

    // Update order status to ready for pickup
    updateOrderStatus("ready")
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
        <Link href="/vendor/orders">
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
                  {order.status === "pending" && (
                    <Button onClick={() => updateOrderStatus("preparing")}>Start Preparing</Button>
                  )}

                  {order.status === "preparing" && (
                    <Button onClick={() => updateOrderStatus("ready")}>Mark as Ready</Button>
                  )}

                  {order.status === "ready" && (
                    <Button onClick={() => updateOrderStatus("completed")}>Complete Order</Button>
                  )}

                  <Button variant="outline" onClick={() => updateOrderStatus("cancelled")}>
                    Cancel Order
                  </Button>
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

              {order.notes && (
                <div className="bg-muted p-3 rounded-md">
                  <p className="font-semibold text-sm">Customer Notes:</p>
                  <p className="text-sm">{order.notes}</p>
                </div>
              )}
            </CardContent>
          </Card>
        </div>

        <div className="space-y-6">
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
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Assign Delivery</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="space-y-2">
                <p className="text-sm">Select a delivery person to handle this order:</p>
                <Select value={selectedDeliveryPerson} onValueChange={setSelectedDeliveryPerson}>
                  <SelectTrigger>
                    <SelectValue placeholder="Select delivery person" />
                  </SelectTrigger>
                  <SelectContent>
                    {mockDeliveryPersonnel.map((person) => (
                      <SelectItem key={person.id} value={person.id}>
                        {person.name}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              <Button onClick={assignDeliveryPerson} className="w-full" disabled={order.status !== "preparing"}>
                Assign Delivery Person
              </Button>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Estimated Preparation</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="flex items-center gap-2">
                <Clock className="h-4 w-4 text-muted-foreground" />
                <span>15-20 minutes</span>
              </div>
              <p className="text-sm text-muted-foreground">
                This is the estimated time to prepare this order based on the items ordered.
              </p>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  )
}
