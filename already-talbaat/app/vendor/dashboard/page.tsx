"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { API_URL } from "@/lib/utils";
import { Card, CardHeader, CardTitle, CardContent, CardFooter } from "@/components/ui/card";
import { useToast } from "@/hooks/use-toast";

type OrderDTO = {
  id: string;
  status: string;
  // you can add more fields here if you want to display them
};

export default function VendorDashboardPage() {
  const { toast } = useToast();
  const [restaurantId, setRestaurantId] = useState<string>("");
  const [orders, setOrders] = useState<OrderDTO[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  const tokenHeader = () => ({ Authorization: `Bearer ${localStorage.getItem("auth-token")}` });

  useEffect(() => {
    (async () => {
      try {
        // 1️⃣ Get vendor → restaurantId
        const vRes = await fetch(`${API_URL}/api/vendors`, { headers: tokenHeader() });
        if (!vRes.ok) throw new Error(await vRes.text());
        const vendor = await vRes.json();
        setRestaurantId(vendor.restaurantId);

        // 2️⃣ Fetch this restaurant’s orders
        const oRes = await fetch(
          `${API_URL}/api/orders/restaurant/${vendor.restaurantId}`,
          { headers: tokenHeader() }
        );
        if (!oRes.ok) throw new Error(await oRes.text());
        setOrders(await oRes.json());
      } catch (err: any) {
        toast({ title: "Error loading dashboard", description: err.message, variant: "destructive" });
      } finally {
        setIsLoading(false);
      }
    })();
  }, [toast]);

  if (isLoading) return <p className="p-8">Loading dashboard…</p>;

  // aggregate stats
  const totalOrders = orders.length;
  const statusCounts = orders.reduce<Record<string, number>>((acc, order) => {
    acc[order.status] = (acc[order.status] || 0) + 1;
    return acc;
  }, {});
  const uniqueStatuses = Object.keys(statusCounts);

  return (
    <div className="container mx-auto py-8">
      <h1 className="text-3xl font-bold mb-6">Dashboard</h1>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        <Card>
          <CardHeader>
            <CardTitle>Total Orders</CardTitle>
          </CardHeader>
          <CardContent className="text-4xl font-bold">{totalOrders}</CardContent>
          <CardFooter>
            <Link href="/vendor/orders" className="underline">
              View orders
            </Link>
          </CardFooter>
        </Card>

        {uniqueStatuses.map((status) => (
          <Card key={status}>
            <CardHeader>
              <CardTitle>{status}</CardTitle>
            </CardHeader>
            <CardContent className="text-4xl font-bold">{statusCounts[status]}</CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
}
