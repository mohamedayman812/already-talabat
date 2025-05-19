// File: vendor/orders/[id]/page.tsx
"use client";

import { useState, useEffect } from "react";
import { useParams, useRouter } from "next/navigation";
import { API_URL } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import { Card, CardHeader, CardTitle, CardContent, CardFooter } from "@/components/ui/card";
import { useToast } from "@/hooks/use-toast";

type OrderDetailDTO = {
  id: string;
  status: string;
  total: number;
  customer: {
    id: string;
    username: string;
    name: string;
  };
  items: {
    id: string;
    name: string;
    quantity: number;
    price: number;
  }[];
};

export default function OrderDetailPage() {
  const { toast } = useToast();
  const { id } = useParams<{ id: string }>();
  const router = useRouter();

  const [order, setOrder] = useState<OrderDetailDTO | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  const tokenHeader = () => ({ Authorization: `Bearer ${localStorage.getItem("auth-token")}` });

  useEffect(() => {
    if (!id) return;
    (async () => {
      try {
        const res = await fetch(`${API_URL}/api/orders/${id}`, {
          headers: tokenHeader(),
        });
        if (!res.ok) throw new Error(await res.text());
        setOrder(await res.json());
      } catch (err: any) {
        toast({ title: "Error loading order", description: err.message, variant: "destructive" });
        // Optionally navigate back if the order doesn't exist:
        // router.push("/vendor/orders");
      } finally {
        setIsLoading(false);
      }
    })();
  }, [id, toast]);

  if (isLoading) return <p className="p-8">Loading order…</p>;
  if (!order) return <p className="p-8">Order not found.</p>;

  return (
    <div className="container mx-auto py-8">
      <Button variant="ghost" onClick={() => router.push("/vendor/orders")}>
        ← Back to Orders
      </Button>

      <Card className="mt-4">
        <CardHeader>
          <CardTitle>Order #{order.id}</CardTitle>
        </CardHeader>

        <CardContent className="space-y-4">
          <p><strong>Status:</strong> {order.status}</p>
          <p><strong>Customer:</strong> {order.customer.name} ({order.customer.username})</p>
          <p><strong>Total:</strong> ${order.total.toFixed(2)}</p>

          <div>
            <h3 className="font-semibold mb-2">Items</h3>
            <ul className="list-disc pl-5 space-y-1">
              {order.items.map(item => (
                <li key={item.id}>
                  {item.name} — Qty: {item.quantity} @ ${item.price.toFixed(2)} each
                </li>
              ))}
            </ul>
          </div>
        </CardContent>

        <CardFooter>
          {/* Example: you could add a “Mark as Delivered” or “Cancel” button here */}
        </CardFooter>
      </Card>
    </div>
  );
}
