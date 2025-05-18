"use client"

import { useState, useEffect } from "react"
import { useRouter } from "next/navigation"
import Image from "next/image"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { Separator } from "@/components/ui/separator"
import { Plus, Minus, Trash2, ShoppingCart } from "lucide-react"
import { useToast } from "@/hooks/use-toast"

export default function CartPage() {
  const [cartItems, setCartItems] = useState([])
  const [isLoading, setIsLoading] = useState(true)
  const router = useRouter()
  const { toast } = useToast()

  useEffect(() => {
    // In a real app, you would fetch the cart from your API
    const storedCart = localStorage.getItem("cart")
    if (storedCart) {
      setCartItems(JSON.parse(storedCart))
    }
    setIsLoading(false)
  }, [])

  const updateCart = (updatedCart) => {
    setCartItems(updatedCart)
    localStorage.setItem("cart", JSON.stringify(updatedCart))
  }

  const increaseQuantity = (itemId) => {
    const updatedCart = cartItems.map((item) => (item.id === itemId ? { ...item, quantity: item.quantity + 1 } : item))
    updateCart(updatedCart)
  }

  const decreaseQuantity = (itemId) => {
    const existingItem = cartItems.find((item) => item.id === itemId)

    if (existingItem.quantity === 1) {
      removeItem(itemId)
    } else {
      const updatedCart = cartItems.map((item) =>
        item.id === itemId ? { ...item, quantity: item.quantity - 1 } : item,
      )
      updateCart(updatedCart)
    }
  }

  const removeItem = (itemId) => {
    const updatedCart = cartItems.filter((item) => item.id !== itemId)
    updateCart(updatedCart)

    toast({
      title: "Item removed",
      description: "The item has been removed from your cart.",
    })
  }

  const clearCart = () => {
    updateCart([])
    toast({
      title: "Cart cleared",
      description: "All items have been removed from your cart.",
    })
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

  const handleCheckout = () => {
    if (cartItems.length === 0) {
      toast({
        title: "Cart is empty",
        description: "Please add items to your cart before checking out.",
        variant: "destructive",
      })
      return
    }

    router.push("/checkout")
  }

  if (isLoading) {
    return (
      <div className="container mx-auto px-4 py-8 flex justify-center items-center min-h-[60vh]">
        <p>Loading your cart...</p>
      </div>
    )
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">Your Cart</h1>

      {cartItems.length === 0 ? (
        <div className="text-center py-12">
          <ShoppingCart className="h-16 w-16 mx-auto mb-4 text-muted-foreground" />
          <h2 className="text-2xl font-semibold mb-2">Your cart is empty</h2>
          <p className="text-muted-foreground mb-6">Looks like you haven't added any items to your cart yet.</p>
          <Button onClick={() => router.push("/restaurants")}>Browse Restaurants</Button>
        </div>
      ) : (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          <div className="lg:col-span-2">
            <Card>
              <CardHeader className="flex flex-row items-center justify-between">
                <CardTitle>Cart Items ({cartItems.length})</CardTitle>
                <Button variant="ghost" size="sm" onClick={clearCart} className="text-red-500 hover:text-red-700">
                  <Trash2 className="h-4 w-4 mr-2" /> Clear Cart
                </Button>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  {cartItems.map((item) => (
                    <div key={item.id} className="flex items-center gap-4">
                      <div className="relative h-20 w-20 rounded-md overflow-hidden shrink-0">
                        <Image
                          src={item.image || "/placeholder.svg?height=80&width=80"}
                          alt={item.name}
                          fill
                          className="object-cover"
                        />
                      </div>
                      <div className="flex-1">
                        <h3 className="font-semibold">{item.name}</h3>
                        <p className="text-sm text-muted-foreground">${item.price.toFixed(2)} each</p>
                      </div>
                      <div className="flex items-center gap-2">
                        <Button
                          variant="outline"
                          size="icon"
                          className="h-8 w-8"
                          onClick={() => decreaseQuantity(item.id)}
                        >
                          <Minus className="h-4 w-4" />
                        </Button>
                        <span className="w-8 text-center">{item.quantity}</span>
                        <Button
                          variant="outline"
                          size="icon"
                          className="h-8 w-8"
                          onClick={() => increaseQuantity(item.id)}
                        >
                          <Plus className="h-4 w-4" />
                        </Button>
                      </div>
                      <div className="text-right min-w-[80px]">
                        <div className="font-semibold">${(item.price * item.quantity).toFixed(2)}</div>
                        <Button
                          variant="ghost"
                          size="sm"
                          className="h-8 text-red-500 hover:text-red-700 p-0"
                          onClick={() => removeItem(item.id)}
                        >
                          Remove
                        </Button>
                      </div>
                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>
          </div>

          <div>
            <Card>
              <CardHeader>
                <CardTitle>Order Summary</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
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
                  <Separator />
                  <div className="flex justify-between font-semibold text-lg">
                    <span>Total</span>
                    <span>${getTotal().toFixed(2)}</span>
                  </div>
                </div>
              </CardContent>
              <CardFooter>
                <Button className="w-full" onClick={handleCheckout}>
                  Proceed to Checkout
                </Button>
              </CardFooter>
            </Card>
          </div>
        </div>
      )}
    </div>
  )
}
