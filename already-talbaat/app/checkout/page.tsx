// app/checkout/page.tsx
"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { API_URL } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardHeader,
  CardContent,
  CardFooter,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { Separator } from "@/components/ui/separator";
import { Textarea } from "@/components/ui/textarea";
import { useToast } from "@/hooks/use-toast";

type MenuItemDTO = {
  id: string;
  name: string;
  price: number;
  description?: string;
};

type CartItem = MenuItemDTO & { quantity: number };

export default function CheckoutPage() {
  const [cartItems, setCartItems] = useState<CartItem[]>([]);
  const [cartId, setCartId] = useState<string>("");
  const [isLoading, setIsLoading] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [paymentMethod, setPaymentMethod] = useState<"card" | "cash" | "wallet">("card");
  const [formData, setFormData] = useState({
    name: "",
    address: "",
    phone: "",
    notes: "",
  });

  const router = useRouter();
  const { toast } = useToast();

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

  useEffect(() => {
    const token = localStorage.getItem("auth-token");
    if (!token) {
      toast({
        title: "Authentication required",
        description: "Please log in to continue with checkout.",
        variant: "destructive",
      });
      router.push("/auth/login");
      return;
    }

    (async () => {
      try {
        // 1️⃣ Get current user
        const meRes = await fetch(`${API_URL}/api/auth/me`, {
          headers: tokenHeader(),
        });
        if (!meRes.ok) throw new Error(await meRes.text());
        const user = (await meRes.json()) as {
          id: string;
          username: string;
          address?: string;
          phone?: string;
        };

        setFormData({
          name: user.username,
          address: user.address || "",
          phone: user.phone || "",
          notes: "",
        });

        // 2️⃣ Load their cart details via CustomerController
        const cartRes = await fetch(`${API_URL}/api/customers/cart/details`, {
          headers: tokenHeader(),
        });
        if (cartRes.status === 401) {
          router.push("/auth/login");
          return;
        }
        if (!cartRes.ok) throw new Error(await cartRes.text());

        // CartWithItemsDTO: { id, customerId, restaurantId, items }
        const cartData = (await cartRes.json()) as {
          id: string;
          items: MenuItemDTO[];
        };
        setCartId(cartData.id);

        // 3️⃣ Group into quantities
        const map: Record<string, { item: MenuItemDTO; qty: number }> = {};
        cartData.items.forEach((i) => {
          if (!map[i.id]) map[i.id] = { item: i, qty: 1 };
          else map[i.id].qty++;
        });
        setCartItems(
          Object.values(map).map(({ item, qty }) => ({
            ...item,
            quantity: qty,
          }))
        );
      } catch (err: any) {
        toast({
          title: "Error loading checkout",
          description: err.message,
          variant: "destructive",
        });
        router.push("/");
      } finally {
        setIsLoading(false);
      }
    })();
  }, [router, toast]);

  const getSubtotal = () =>
    cartItems.reduce((sum, i) => sum + i.price * i.quantity, 0);
  const getDeliveryFee = () => (cartItems.length > 0 ? 2.99 : 0);
  const getTax = () => getSubtotal() * 0.1;
  const getTotal = () => getSubtotal() + getDeliveryFee() + getTax();

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setFormData((p) => ({ ...p, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);

    try {
      // Submit via CustomerController endpoint
      const res = await fetch(
        `${API_URL}/api/customers/cart/submit/${cartId}/paymentMethod?paymentMethod=${paymentMethod}`,
        {
          method: "POST",
          headers: tokenHeader(),
        }
      );
      if (res.status === 401) {
        router.push("/auth/login");
        return;
      }
      if (!res.ok) throw new Error(await res.text());

      // Delete the cart for current customer
      await fetch(`${API_URL}/api/customers/cart/remove`, {
        method: "DELETE",
        headers: tokenHeader(),
      });

      toast({ title: "Order placed", description: "Your order is on its way!" });

      // 4️⃣ Delete the customer’s cart after placing the order
      await fetch(`${API_URL}/api/customers/cart/${cartId}`, {
        method: "DELETE",
        headers: tokenHeader(),
      });

      router.push("/restaurants");
    } catch (err: any) {
      toast({
        title: "Failed to place order",
        description: err.message,
        variant: "destructive",
      });
    } finally {
      setIsSubmitting(false);
    }
  };

  if (isLoading) {
    return (
      <div className="container mx-auto px-4 py-8 flex justify-center items-center min-h-[60vh]">
        <p>Loading checkout...</p>
      </div>
    );
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
                    <Input
                      id="name"
                      name="name"
                      value={formData.name}
                      onChange={handleChange}
                      required
                    />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="phone">Phone Number</Label>
                    <Input
                      id="phone"
                      name="phone"
                      type="tel"
                      value={formData.phone}
                      onChange={handleChange}
                      required
                    />
                  </div>
                </div>
                <div className="space-y-2">
                  <Label htmlFor="address">Delivery Address</Label>
                  <Textarea
                    id="address"
                    name="address"
                    value={formData.address}
                    onChange={handleChange}
                    required
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="notes">Delivery Notes</Label>
                  <Textarea
                    id="notes"
                    name="notes"
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
                <RadioGroup
                  value={paymentMethod}
                  onValueChange={(v) => setPaymentMethod(v as any)}
                  className="space-y-3"
                >
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
              <Button type="submit" size="lg" disabled={isSubmitting || !cartId}>
                {isSubmitting ? "Processing..." : "Place Order"}
              </Button>
            </div>
          </form>
        </div>

        <Card className="sticky top-20">
          <CardHeader>
            <CardTitle>Order Summary</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {cartItems.map((i) => (
                <div key={i.id} className="flex justify-between">
                  <span>
                    {i.quantity} × {i.name}
                  </span>
                  <span>${(i.price * i.quantity).toFixed(2)}</span>
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
              By placing your order, you agree to our Terms of Service and
              Privacy Policy.
            </p>
          </CardFooter>
        </Card>
      </div>
    </div>
  );
}
