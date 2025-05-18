"use client"

import { useState, useEffect } from "react"
import { useParams, useRouter } from "next/navigation"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { ShoppingCart, Plus, Minus, MapPin } from "lucide-react"
import { useToast } from "@/hooks/use-toast"
import { API_URL } from "@/lib/utils"

type MenuItem = {
  id: string
  name: string
  description: string
  price: number
  restaurantId: string
}

type Restaurant = {
  id: string
  name: string
  address: string
  vendorId: string
  menuItems: MenuItem[]
}

export default function RestaurantPage() {
  // 1) grab the `id` out of the URL
  const params = useParams()             // { id: string }
  const id = params?.id as string

  const [restaurant, setRestaurant] = useState<Restaurant | null>(null)
  const [cartItems,   setCartItems]    = useState<(MenuItem & { quantity: number })[]>([])
  const [isLoading,   setIsLoading]    = useState(true)

  const router = useRouter()
  const { toast } = useToast()

  // 2) fetch the restaurant once we know `id`
  useEffect(() => {
    if (!id) return

    async function fetchRestaurant() {
      try {
        const res = await fetch(`${API_URL}/api/restaurants/single/${id}`)
        if (!res.ok) {
          const text = await res.text()
          throw new Error(text || `HTTP ${res.status}`)
        }
        setRestaurant(await res.json())
      } catch (err: any) {
        console.error(err)
        toast({ title: "Error", description: err.message, variant: "destructive" })
      } finally {
        setIsLoading(false)
      }
    }

    fetchRestaurant()
  }, [id, toast])

  // 3) cart helpers remain unchanged
  const addToCart = (item: MenuItem) => {
    setCartItems(prev => {
      const found = prev.find(i => i.id === item.id)
      if (found) {
        return prev.map(i =>
          i.id === item.id ? { ...i, quantity: i.quantity + 1 } : i
        )
      }
      return [...prev, { ...item, quantity: 1 }]
    })
    toast({ title: "Added", description: `${item.name} added.` })
  }

  const removeFromCart = (itemId: string) => {
    setCartItems(prev => {
      const found = prev.find(i => i.id === itemId)
      if (!found) return prev
      if (found.quantity === 1) return prev.filter(i => i.id !== itemId)
      return prev.map(i =>
        i.id === itemId ? { ...i, quantity: i.quantity - 1 } : i
      )
    })
  }

  const getTotal = () => cartItems.reduce((sum, i) => sum + i.price * i.quantity, 0)

  const handleCheckout = () => {
    if (cartItems.length === 0) {
      toast({ title: "Cart empty", description: "Add items first", variant: "destructive" })
      return
    }
    localStorage.setItem("cart", JSON.stringify(cartItems))
    router.push("/checkout")
  }

  // 4) render states
  if (isLoading) return <p>Loading…</p>
  if (!restaurant) return <p className="text-red-600">Restaurant not found.</p>

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-2">{restaurant.name}</h1>
      <div className="flex items-center text-muted-foreground mb-6">
        <MapPin className="h-4 w-4 mr-1" />
        {restaurant.address}
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
        {restaurant.menuItems.map(item => (
          <Card key={item.id} className="hover:shadow-lg transition-shadow">
            <CardContent className="space-y-2">
              <h3 className="font-semibold">{item.name}</h3>
              <p className="text-sm text-muted-foreground">{item.description}</p>
              <div className="flex justify-between items-center">
                <span className="font-medium">${item.price.toFixed(2)}</span>
                <Button variant="outline" size="sm" onClick={() => addToCart(item)} className="gap-1">
                  <Plus className="h-4 w-4" /> Add
                </Button>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      <div className="sticky top-20 lg:w-80">
        <Card>
          <CardContent className="p-6">
            <h3 className="text-lg font-semibold flex items-center mb-4">
              <ShoppingCart className="h-5 w-5 mr-2" /> Your Order
            </h3>
            {cartItems.length === 0 ? (
              <p className="text-center text-muted-foreground py-4">Cart is empty</p>
            ) : (
              <div className="space-y-4">
                {cartItems.map(i => (
                  <div key={i.id} className="flex justify-between items-center">
                    <div>
                      <span className="font-medium">
                        {i.quantity} x {i.name}
                      </span>
                      <p className="text-sm text-muted-foreground">
                        ${(i.price * i.quantity).toFixed(2)}
                      </p>
                    </div>
                    <div className="flex items-center gap-2">
                      <Button variant="ghost" size="icon" onClick={() => removeFromCart(i.id)}>
                        <Minus className="h-3 w-3" />
                      </Button>
                      <Button variant="ghost" size="icon" onClick={() => addToCart(i)}>
                        <Plus className="h-3 w-3" />
                      </Button>
                    </div>
                  </div>
                ))}

                <div className="border-t pt-4 mt-4">
                  <div className="flex justify-between font-semibold">
                    <span>Total</span>
                    <span>${getTotal().toFixed(2)}</span>
                  </div>
                </div>
              </div>
            )}

            <Button className="w-full mt-6" onClick={handleCheckout} disabled={cartItems.length === 0}>
              Checkout
            </Button>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
