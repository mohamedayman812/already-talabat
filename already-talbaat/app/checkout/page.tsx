"use client"

import { useState, useEffect } from "react"
import { useRouter } from "next/navigation"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group"
import { Separator } from "@/components/ui/separator"
import { Textarea } from "@/components/ui/textarea"
import { useToast } from "@/hooks/use-toast"

export default function CheckoutPage() {
  const [cartItems, setCartItems] = useState([])
  const [isLoading, setIsLoading] = useState(true)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [paymentMethod, setPaymentMethod] = useState("card")
  const [formData, setFormData] = useState({
    name: "",
    address: "",
    phone: "",
    notes: "",
  })

  const router = useRouter()
  const { toast } = useToast()

  useEffect(() => {
    // Check if user is logged in
    const token = localStorage.getItem("auth-token")
    if (!token) {
      toast({
        title: "Authentication required",
        description: "Please log in to continue with checkout.",
        variant: "destructive",
      })
      router.push("/auth/login")
      return
    }

    // In a real app, you would fetch the cart from your API
    const storedCart = localStorage.getItem("cart")
    if (storedCart) {
      setCartItems(JSON.parse(storedCart))
    } else {
      toast({
        title: "Cart is empty",
        description: "Please add items to your cart before checking out.",
        variant: "destructive",
      })
      router.push("/restaurants")
      return
    }

    // In a real app, you would fetch the user's profile data
    setFormData({
      name: "John Doe",
      address: "123 Main St, City, Country",
      phone: "+1234567890",
      notes: "",
    })

    setIsLoading(false)
  }, [router, toast])

  const handleChange = (e) => {
    const { name, value } = e.target
    setFormData((prev) => ({ ...prev, [name]: value }))
  }

  const getSubtotal = () => {
    return cartItems.reduce((total, item) => total + item.price * item.quantity, 0)
  }

  const getDeliveryFee = () => {
    return cartItems.length > 0 ? 2.99 : 0
  }

  const getTax = () => {
    return getSubtotal() * 0.1 // 10% tax
  }

  const getTotal = () => {
    return getSubtotal() + getDeliveryFee() + getTax()
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setIsSubmitting(true)

    try {
      // In a real app, you would submit the order to your API
      // const response = await fetch('/api/orders', {
      //   method: 'POST',
      //   headers: { 'Content-Type': 'application/json' },
      //   body: JSON.stringify({
      //     items: cartItems,
      //     deliveryDetails: formData,
      //     paymentMethod,
      //     total: getTotal(),
      //   }),
      // });

      // Mock successful order
      await new Promise((resolve) => setTimeout(resolve, 1500))

      // Clear cart
      localStorage.removeItem("cart")

      toast({
        title: "Order placed successfully",
        description: "Your order has been placed and will be delivered soon.",
      })

      router.push("/orders")
    } catch (error) {
      toast({
        title: "Failed to place order",
        description: "There was an error processing your order. Please try again.",
        variant: "destructive",
      })
    } finally {
      setIsSubmitting(false)
    }
  }

  if (isLoading) {
    return (
      <div className="container mx-auto px-4 py-8 flex justify-center items-center min-h-[60vh]">
        <p>Loading checkout...</p>
      </div>
    )
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">Checkout</h1>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <div className="lg:col-span-2">
          <form onSubmit={handleSubmit}>
            <Card className="mb-8">
              <CardHeader>
                <CardTitle>Delivery Details</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <Label htmlFor="name">Full Name</Label>
                    <Input id="name" name="name" value={formData.name} onChange={handleChange} required />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="phone">Phone Number</Label>
                    <Input id="phone" name="phone" type="tel" value={formData.phone} onChange={handleChange} required />
                  </div>
                </div>
                <div className="space-y-2">
                  <Label htmlFor="address">Delivery Address</Label>
                  <Textarea id="address" name="address" value={formData.address} onChange={handleChange} required />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="notes">Delivery Notes (Optional)</Label>
                  <Textarea
                    id="notes"
                    name="notes"
                    placeholder="Any special instructions for delivery"
                    value={formData.notes}
                    onChange={handleChange}
                  />
                </div>
              </CardContent>
            </Card>

            <Card className="mb-8">
              <CardHeader>
                <CardTitle>Payment Method</CardTitle>
              </CardHeader>
              <CardContent>
                <RadioGroup value={paymentMethod} onValueChange={setPaymentMethod} className="space-y-3">
                  <div className="flex items-center space-x-2 border rounded-md p-3">
                    <RadioGroupItem value="card" id="card" />
                    <Label htmlFor="card" className="flex-1 cursor-pointer">
                      Credit/Debit Card
                    </Label>
                  </div>
                  <div className="flex items-center space-x-2 border rounded-md p-3">
                    <RadioGroupItem value="cash" id="cash" />
                    <Label htmlFor="cash" className="flex-1 cursor-pointer">
                      Cash on Delivery
                    </Label>
                  </div>
                  <div className="flex items-center space-x-2 border rounded-md p-3">
                    <RadioGroupItem value="wallet" id="wallet" />
                    <Label htmlFor="wallet" className="flex-1 cursor-pointer">
                      Digital Wallet
                    </Label>
                  </div>
                </RadioGroup>
              </CardContent>
            </Card>

            <div className="flex justify-end">
              <Button type="submit" size="lg" disabled={isSubmitting}>
                {isSubmitting ? "Processing..." : "Place Order"}
              </Button>
            </div>
          </form>
        </div>

        <div>
          <Card className="sticky top-20">
            <CardHeader>
              <CardTitle>Order Summary</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {cartItems.map((item) => (
                  <div key={item.id} className="flex justify-between">
                    <span>
                      {item.quantity} x {item.name}
                    </span>
                    <span>${(item.price * item.quantity).toFixed(2)}</span>
                  </div>
                ))}

                <Separator />

                <div className="space-y-2">
                  <div className="flex justify-between">
                    <span>Subtotal</span>
                    <span>${getSubtotal().toFixed(2)}</span>
                  </div>
                  <div className="flex justify-between">
                    <span>Delivery Fee</span>
                    <span>${getDeliveryFee().toFixed(2)}</span>
                  </div>
                  <div className="flex justify-between">
                    <span>Tax</span>
                    <span>${getTax().toFixed(2)}</span>
                  </div>
                </div>

                <Separator />

                <div className="flex justify-between font-semibold text-lg">
                  <span>Total</span>
                  <span>${getTotal().toFixed(2)}</span>
                </div>
              </div>
            </CardContent>
            <CardFooter>
              <p className="text-sm text-muted-foreground">
                By placing your order, you agree to our Terms of Service and Privacy Policy.
              </p>
            </CardFooter>
          </Card>
        </div>
      </div>
    </div>
  )
}
