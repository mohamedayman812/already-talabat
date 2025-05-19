"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { API_URL } from "@/lib/utils";
import { Card, CardHeader, CardTitle, CardContent, CardFooter } from "@/components/ui/card";
import { useToast } from "@/hooks/use-toast";

type OrderDTO = {
  id: string;
  items: string[];
  status: string;
};

export default function VendorOrdersPage() {
  const { toast } = useToast();
  const [restaurantId, setRestaurantId] = useState<string>("");
  const [orders, setOrders] = useState<OrderDTO[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  const tokenHeader = () => ({ Authorization: `Bearer ${localStorage.getItem("auth-token")}` });

  useEffect(() => {
    (async () => {
      try {
        // get vendor → restaurantId
        const vRes = await fetch(`${API_URL}/api/vendors`, { headers: tokenHeader() });
        if (!vRes.ok) throw new Error(await vRes.text());
        const vendor = await vRes.json();
        setRestaurantId(vendor.restaurantId);

        // fetch orders for that restaurant
        const oRes = await fetch(
          `${API_URL}/api/orders/restaurant/${vendor.restaurantId}`,
          { headers: tokenHeader() }
        );
        if (!oRes.ok) throw new Error(await oRes.text());
        setOrders(await oRes.json());
      } catch (err: any) {
        toast({ title: "Error loading orders", description: err.message, variant: "destructive" });
      } finally {
        setIsLoading(false);
      }
    })();
  }, [toast]);

  if (isLoading) return <p className="p-8">Loading orders…</p>;
  if (orders.length === 0) return <p className="p-8">No orders found.</p>;

  return (
    <div className="container mx-auto py-8">
      <h1 className="text-3xl font-bold mb-6">Orders</h1>
      <div className="space-y-4">
        {orders.map((order) => (
          <Card key={order.id}>
            <CardHeader>
              <CardTitle>Order #{order.id}</CardTitle>
            </CardHeader>
            <CardContent>
              <p>Items: {order.items.length}</p>
              <p>Status: {order.status}</p>
            </CardContent>
            <CardFooter className="justify-end">
              <Link href={`/vendor/orders/${order.id}`} className="underline">
                Details
              </Link>
            </CardFooter>
          </Card>
        ))}
      </div>
    </div>
  );
}
