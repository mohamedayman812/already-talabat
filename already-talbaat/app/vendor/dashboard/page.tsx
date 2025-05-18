"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { API_URL } from "@/lib/utils";
import { Card, CardHeader, CardTitle, CardContent, CardFooter } from "@/components/ui/card";
import { useToast } from "@/hooks/use-toast";
import { Button } from "@/components/ui/button";
import { Skeleton } from "@/components/ui/skeleton";

type OrderDTO = {
  id: string;
  status: string;
  createdAt?: string;
  paymentMethod: string;
};

type VendorDTO = {
  vendorId: string;
  userId: string;
  name: string;
  email: string;
  restaurantId: string;
  restaurantName: string;
  restaurantAddress: string;
};

export default function VendorDashboardPage() {
  const { toast } = useToast();
  const [vendor, setVendor] = useState<VendorDTO | null>(null);
  const [orders, setOrders] = useState<OrderDTO[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  const tokenHeader = () => ({ Authorization: `Bearer ${localStorage.getItem("auth-token")}` });

  useEffect(() => {
    (async () => {
      try {
        setIsLoading(true);
        
        // 1️⃣ Get vendor details
        const vRes = await fetch(`${API_URL}/api/vendors`, { headers: tokenHeader() });
        if (!vRes.ok) throw new Error(await vRes.text());
        const vendorData = await vRes.json();
        setVendor(vendorData);

        // 2️⃣ Fetch restaurant's orders
        const oRes = await fetch(
          `${API_URL}/api/orders/restaurant/${vendorData.restaurantId}`,
          { headers: tokenHeader() }
        );
        if (!oRes.ok) throw new Error(await oRes.text());
        setOrders(await oRes.json());

      } catch (err: any) {
        toast({ 
          title: "Error loading dashboard", 
          description: err.message, 
          variant: "destructive" 
        });
      } finally {
        setIsLoading(false);
      }
    })();
  }, [toast]);

  if (isLoading) return (
    <div className="container mx-auto py-8 space-y-6">
      <Skeleton className="h-10 w-64" />
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        {[...Array(5)].map((_, i) => (
          <Skeleton key={i} className="h-36 rounded-lg" />
        ))}
      </div>
    </div>
  );

  // Calculate stats
  const totalOrders = orders.length;
  const todayOrders = orders.filter(order => {
    if (!order.createdAt) return false;
    const orderDate = new Date(order.createdAt);
    const today = new Date();
    return orderDate.toDateString() === today.toDateString();
  }).length;

  const statusCounts = orders.reduce<Record<string, number>>((acc, order) => {
    acc[order.status] = (acc[order.status] || 0) + 1;
    return acc;
  }, {});

  const paymentMethods = orders.reduce<Record<string, number>>((acc, order) => {
    acc[order.paymentMethod] = (acc[order.paymentMethod] || 0) + 1;
    return acc;
  }, {});

  const uniqueStatuses = Object.keys(statusCounts);

  return (
    <div className="container mx-auto py-8 space-y-6">
      {/* Restaurant Header */}
      <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-6">
        <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
          <div>
            <h1 className="text-3xl font-bold">{vendor?.restaurantName}</h1>
            <div className="mt-2 space-y-1 text-gray-600 dark:text-gray-300">
              <p>{vendor?.restaurantAddress}</p>
              <p>Managed by: {vendor?.name}</p>
              <p className="text-sm text-gray-500 dark:text-gray-400">
                Vendor ID: {vendor?.vendorId}
              </p>
            </div>
          </div>
          <div className="flex gap-2">
            <Button variant="outline" asChild>
              <Link href="/vendor/orders">View All Orders</Link>
            </Button>
          </div>
        </div>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        <Card className="border-blue-200 dark:border-blue-800">
          <CardHeader>
            <CardTitle>Total Orders</CardTitle>
          </CardHeader>
          <CardContent className="text-4xl font-bold">{totalOrders}</CardContent>
          <CardFooter>
            <Link href="/vendor/orders" className="text-blue-600 dark:text-blue-400 hover:underline">
              View all orders
            </Link>
          </CardFooter>
        </Card>

        <Card className="border-green-200 dark:border-green-800">
          <CardHeader>
            <CardTitle>Today's Orders</CardTitle>
          </CardHeader>
          <CardContent className="text-4xl font-bold">{todayOrders}</CardContent>
          <CardFooter className="text-sm text-green-600 dark:text-green-400">
            {new Date().toLocaleDateString()}
          </CardFooter>
        </Card>

        {uniqueStatuses.map((status) => (
          <Card key={status} className={
            status === 'COMPLETED' ? 'border-green-200 dark:border-green-800' :
            status === 'CANCELLED' ? 'border-red-200 dark:border-red-800' :
            'border-yellow-200 dark:border-yellow-800'
          }>
            <CardHeader>
              <CardTitle>{status}</CardTitle>
            </CardHeader>
            <CardContent className="text-4xl font-bold">{statusCounts[status]}</CardContent>
            <CardFooter>
              <Link 
                href={`/vendor/orders?status=${status}`} 
                className="text-sm hover:underline"
              >
                View {status.toLowerCase()} orders
              </Link>
            </CardFooter>
          </Card>
        ))}
      </div>

      {/* Payment Methods Section */}
      <Card>
        <CardHeader>
          <CardTitle>Payment Methods</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
            {Object.entries(paymentMethods).map(([method, count]) => (
              <div key={method} className="border rounded-lg p-4">
                <p className="font-medium capitalize">{method}</p>
                <p className="text-2xl font-bold mt-2">{count}</p>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>

      {/* Recent Orders Section */}
      <Card>
        <CardHeader>
          <div className="flex justify-between items-center">
            <CardTitle>Recent Orders</CardTitle>
            <Button variant="outline" asChild>
              <Link href="/vendor/orders">View All</Link>
            </Button>
          </div>
        </CardHeader>
        <CardContent>
          {orders.slice(0, 5).length > 0 ? (
            <div className="space-y-4">
              {orders.slice(0, 5).map(order => (
                <div key={order.id} className="flex justify-between items-center p-3 border rounded-lg">
                  <div>
                    <p className="font-medium">Order #{order.id.slice(-6)}</p>
                    {order.createdAt && (
                      <p className="text-sm text-gray-500">
                        {new Date(order.createdAt).toLocaleString()}
                      </p>
                    )}
                  </div>
                  <div className="flex items-center gap-4">
                    <span className={`px-3 py-1 rounded-full text-xs font-medium ${
                      order.status === 'COMPLETED' ? 'bg-green-100 text-green-800' :
                      order.status === 'CANCELLED' ? 'bg-red-100 text-red-800' :
                      'bg-yellow-100 text-yellow-800'
                    }`}>
                      {order.status}
                    </span>
                    <span className="text-sm capitalize text-gray-500">
                      {order.paymentMethod}
                    </span>
                    <Link href={`/vendor/orders/${order.id}`} className="text-sm text-blue-600 hover:underline">
                      Details
                    </Link>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <p className="text-center text-gray-500 py-6">No recent orders found</p>
          )}
        </CardContent>
      </Card>
    </div>
  );
}