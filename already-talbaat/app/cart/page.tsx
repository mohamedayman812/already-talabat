// app/cart/page.tsx
"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
  CardFooter,
} from "@/components/ui/card";
import { ShoppingCart, ArrowLeft } from "lucide-react";
import { useToast } from "@/hooks/use-toast";
import { API_URL } from "@/lib/utils";

type RawItem = { id: string; name: string; price: number };
type CartItem = RawItem & { quantity: number };

export default function CartPage() {
  const router = useRouter();
  const { toast } = useToast();

  const [cartItems, setCartItems] = useState<CartItem[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  // Helper to get your JWT header
  const tokenHeader = (json = false): Record<string, string> => {
    const token = localStorage.getItem("auth-token");
    if (!token) {
      router.push("/auth/login");
      return {};
    }
    return {
      Authorization: `Bearer ${token}`,
      ...(json ? { "Content-Type": "application/json" } : {}),
    };
  };

  // Fetch the cart (uses your CustomerController endpoint)
  useEffect(() => {
    (async () => {
      try {
        const res = await fetch(
          `${API_URL}/api/customers/cart/details`,
          { headers: tokenHeader() }
        );

        if (res.status === 401) {
          router.push("/auth/login");
          return;
        }

        if (res.status === 404) {
          // no cart yet
          setCartItems([]);
        } else if (!res.ok) {
          throw new Error((await res.text()) || "Failed to load cart");
        } else {
          const { items } = (await res.json()) as { items: RawItem[] };
          // group into quantities
          const map: Record<string, { item: RawItem; qty: number }> = {};
          items.forEach((i) => {
            if (!map[i.id]) map[i.id] = { item: i, qty: 1 };
            else map[i.id].qty++;
          });
          setCartItems(
            Object.values(map).map(({ item, qty }) => ({
              ...item,
              quantity: qty,
            }))
          );
        }
      } catch (err: any) {
        toast({
          title: "Error",
          description: err.message || "Failed to load cart",
          variant: "destructive",
        });
      } finally {
        setIsLoading(false);
      }
    })();
  }, [router, toast]);

  const getTotal = () =>
    cartItems.reduce((sum, i) => sum + i.price * i.quantity, 0);

  if (isLoading) return <p>Loading cart…</p>;

  return (
    <div className="container mx-auto px-4 py-8">
      <Button variant="link" onClick={() => router.back()}>
        <ArrowLeft className="inline-block mr-2" /> Back to Restaurants
      </Button>

      <h1 className="text-3xl font-bold mb-6">Your Cart</h1>

      {cartItems.length === 0 ? (
        <Card className="text-center py-12">
          <ShoppingCart
            className="mx-auto mb-4 text-muted-foreground"
            size={48}
          />
          <p className="mb-6">Your cart is empty</p>
          <Button onClick={() => router.push("/restaurants")}>
            Browse Restaurants
          </Button>
        </Card>
      ) : (
        <Card>
          <CardHeader>
            <CardTitle>Items</CardTitle>
          </CardHeader>

          <CardContent className="space-y-4">
            {cartItems.map((i) => (
              <div key={i.id} className="flex justify-between">
                <span>
                  {i.quantity} × {i.name}
                </span>
                <span>${(i.price * i.quantity).toFixed(2)}</span>
              </div>
            ))}

            <div className="border-t pt-4 font-semibold flex justify-between">
              <span>Total</span>
              <span>${getTotal().toFixed(2)}</span>
            </div>
          </CardContent>

          <CardFooter className="flex flex-col">
            <Button
              className="w-full"
              onClick={() => router.push("/checkout")}
            >
              Proceed to Checkout
            </Button>
          </CardFooter>
        </Card>
      )}
    </div>
  );
}
