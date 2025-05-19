"use client";

import { useState, useEffect } from "react";
import { useParams, useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { ShoppingCart, Plus, Minus } from "lucide-react";
import { useToast } from "@/hooks/use-toast";
import { API_URL } from "@/lib/utils";

type MenuItem = {
  id: string;
  name: string;
  description: string;
  price: number;
  restaurantId: string;
};

type CartItem = MenuItem & { quantity: number };

export default function RestaurantPage() {
  const { id } = useParams<{ id: string }>();
  const router = useRouter();
  const { toast } = useToast();

  const [restaurant, setRestaurant] = useState<{
    id: string;
    name: string;
    address: string;
    menuItems: MenuItem[];
  } | null>(null);

  const [cartItems, setCartItems]     = useState<CartItem[]>([]);
  const [customerId, setCustomerId]   = useState<string>("");
  const [hasCart, setHasCart]         = useState<boolean>(false);
  const [isLoading, setIsLoading]     = useState<boolean>(true);

  const tokenHeader = (json = false): Record<string, string> => {
  const token = localStorage.getItem("auth-token");
  if (!token) throw new Error("Not authenticated. Please log in.");
  const headers: Record<string, string> = {
    Authorization: `Bearer ${token}`,
  };
  if (json) headers["Content-Type"] = "application/json";
  return headers;
};

  // On mount: load customer, restaurant, and existing cart (if any)
  useEffect(() => {
  if (!id) return;

  (async () => {
    try {
      // 1) Who am I as a customer?
      const custRes = await fetch(`${API_URL}/api/customers/me`, {
        headers: tokenHeader(),
      });
      if (!custRes.ok) throw new Error("Failed to load customer profile");
      const cust = (await custRes.json()) as { customerId: string };
      setCustomerId(cust.customerId);

      // 2) Load restaurant
      const res = await fetch(`${API_URL}/api/restaurants/single/${id}`);
      setRestaurant(await res.json());

      // 3) Load existing cart (if any)
      await fetchCart(cust.customerId);
    } catch (err: any) {
      toast({ title: "Error", description: err.message, variant: "destructive" });
      // Optionally, redirect to login page if not authenticated
      // router.push("/login");
    } finally {
      setIsLoading(false);
    }
  })();
}, [id, toast]);;

  // Fetch existing cart; set hasCart=true if it exists
  async function fetchCart(custId: string) {
    try {
      const res = await fetch(`${API_URL}/api/carts/${custId}`, {
        headers: tokenHeader(),
      })

      if (res.status === 404) {
        // no cart yet
        setHasCart(false);
        return;
      }

      // cart exists
      setHasCart(true);
      const cart = (await res.json()) as { id: string; items: MenuItem[] };

      // build quantities
      const map: Record<string, { item: MenuItem; qty: number }> = {};
      for (const it of cart.items) {
        if (!map[it.id]) map[it.id] = { item: it, qty: 1 };
        else              map[it.id].qty++;
      }
      setCartItems(
        Object.values(map).map(({ item, qty }) => ({ ...item, quantity: qty }))
      );
    } catch (e: any) {
      setHasCart(false);
    }
  }

  // Local add/remove
  const addToCart = (item: MenuItem) => {
    setCartItems(prev => {
      const found = prev.find(i => i.id === item.id);
      if (found) {
        return prev.map(i =>
          i.id === item.id ? { ...i, quantity: i.quantity + 1 } : i
        );
      }
      return [...prev, { ...item, quantity: 1 }];
    });
  };
  
  const removeFromCart = (itemId: string) => {
    setCartItems(prev => {
      const found = prev.find(i => i.id === itemId);
      if (!found) return prev;
      if (found.quantity > 1) {
        return prev.map(i =>
          i.id === itemId ? { ...i, quantity: i.quantity - 1 } : i
        );
      }
      return prev.filter(i => i.id !== itemId);
    });
  };

  // Compute total
  const getTotal = () =>
    cartItems.reduce((sum, i) => sum + i.price * i.quantity, 0);

  // On submit: create or update cart
  // inside RestaurantPage component

// inside your Cart page component

const handleSubmit = async () => {
  if (cartItems.length === 0) {
    toast({ title: "Empty", description: "Add something first", variant: "destructive" });
    return;
  }

  // flatten out N copies of each menu-item ID
  const allIds = cartItems.flatMap(i => Array(i.quantity).fill(i.id));

  try {
    // 1️⃣ Try to update existing cart
    let res = await fetch(
      `${API_URL}/api/carts/${customerId}`,
      {
        method: "PUT",
        headers: tokenHeader(true),
        body: JSON.stringify({
          restaurantId: id,
          menuItemIds: allIds,
        }),
      }
    );

    // 2️⃣ If that comes back 401 Unauthorized **or** 404 Not Found, fall back to creating
    if (res.status === 401 || res.status === 404) {
      res = await fetch(
        `${API_URL}/api/carts`,
        {
          method: "POST",
          headers: tokenHeader(true),
          body: JSON.stringify({
            customerId,
            restaurantId: id,
            menuItemIds: allIds,
          }),
        }
      );
      if (!res.ok) {
        const err = await res.text();
        throw new Error(err || "Failed to create new cart");
      }
      setHasCart(true);
    }
    // 3️⃣ Any other non-OK? blow up
    else if (!res.ok) {
      const err = await res.text();
      throw new Error(err || "Failed to update cart");
    }

    // 4️⃣ All good, go to checkout
    router.push("/cart");
  } catch (err: any) {
    toast({ title: "Error", description: err.message, variant: "destructive" });
  }
};


  if (isLoading) return <p>Loading…</p>;
  if (!restaurant) return <p className="text-red-600">Restaurant not found.</p>;

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-2">{restaurant.name}</h1>
      <p className="mb-6 text-muted-foreground">{restaurant.address}</p>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
        {restaurant.menuItems.map(item => (
          <Card key={item.id} className="hover:shadow-lg transition-shadow">
            <CardContent className="space-y-2">
              <h3 className="font-semibold">{item.name}</h3>
              <p className="text-sm text-muted-foreground">{item.description}</p>
              <div className="flex justify-between items-center">
                <span className="font-medium">${item.price.toFixed(2)}</span>
                <Button variant="outline" size="sm" onClick={() => addToCart(item)}>
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
            <div className="flex items-center mb-4">
              <ShoppingCart className="h-5 w-5 mr-2" />
              <h3 className="text-lg font-semibold">Your Order</h3>
              <span className="ml-auto bg-blue-500 text-white rounded-full h-6 w-6 flex items-center justify-center text-sm">
                {cartItems.reduce((sum, i) => sum + i.quantity, 0)}
              </span>
            </div>

            {cartItems.length === 0 ? (
              <p className="text-center text-muted-foreground py-4">Cart is empty</p>
            ) : (
              <div className="space-y-4">
                {cartItems.map(i => (
                  <div key={i.id} className="flex justify-between items-center">
                    <div>
                      <span className="font-medium">
                        {i.quantity} × {i.name}
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

            <Button className="w-full mt-6" onClick={handleSubmit}>
              Submit
            </Button>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
