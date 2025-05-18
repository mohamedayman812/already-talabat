// File: vendor/orders/[id]/page.tsx
"use client";

import { useState, useEffect } from "react";
import { useParams, useRouter } from "next/navigation";
import { API_URL } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import { Card, CardHeader, CardTitle, CardContent, CardFooter } from "@/components/ui/card";
import { useToast } from "@/hooks/use-toast";
import { Skeleton } from "@/components/ui/skeleton";

type OrderDetailDTO = {
  id: string;
  customerId: string;
  restaurantId: string;
  deliveryGuyId?: string;
  cartId: string;
  items: string[]; // Array of menu item IDs
  status: string;
  paymentMethod: string;
};

type MenuItemDetails = {
  id: string;
  name: string;
  description?: string;
  price: number;
  imageUrl?: string;
};

// Define order status flow
const ORDER_STATUS = {
  PLACED: 'PLACED',
  PREPARING: 'PREPARING',
  PREPARED: 'PREPARED',
  COMPLETED: 'COMPLETED'
} as const;

export default function OrderDetailPage() {
  const { toast } = useToast();
  const { id } = useParams<{ id: string }>();
  const router = useRouter();

  const [order, setOrder] = useState<OrderDetailDTO | null>(null);
  const [menuItems, setMenuItems] = useState<MenuItemDetails[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  const tokenHeader = () => ({ 
    Authorization: `Bearer ${localStorage.getItem("auth-token")}`,
    "Content-Type": "application/json"
  });

  // Fetch order details and menu items
  useEffect(() => {
    if (!id) return;

    const fetchData = async () => {
      try {
        setIsLoading(true);
        
        // 1. Fetch the order itself
        const orderRes = await fetch(`${API_URL}/api/orders/${id}`, {
          headers: tokenHeader(),
        });
        
        if (!orderRes.ok) throw new Error("Failed to fetch order");
        const orderData: OrderDetailDTO = await orderRes.json();
        setOrder(orderData);

        // 2. Fetch menu items one by one
        const itemsPromises = orderData.items.map(itemId => 
          fetch(`${API_URL}/api/menu-items/${itemId}`, {
            headers: tokenHeader(),
          }).then(res => res.ok ? res.json() : null)
        );

        const itemsResults = await Promise.all(itemsPromises);
        const validItems = itemsResults.filter(item => item !== null) as MenuItemDetails[];
        setMenuItems(validItems);

      } catch (err: any) {
        toast({ 
          title: "Error loading order details", 
          description: err.message, 
          variant: "destructive" 
        });
      } finally {
        setIsLoading(false);
      }
    };

    fetchData();
  }, [id, toast]);

  if (isLoading) return (
    <div className="container mx-auto py-8 space-y-4">
      <Skeleton className="h-8 w-24" />
      <div className="space-y-2">
        <Skeleton className="h-4 w-full" />
        <Skeleton className="h-4 w-3/4" />
        <Skeleton className="h-4 w-1/2" />
      </div>
    </div>
  );

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
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <h3 className="font-semibold mb-2">Order Information</h3>
              <p><strong>Status:</strong> {order.status}</p>
              <p><strong>Payment Method:</strong> {order.paymentMethod}</p>
            </div>
          </div>

          <div className="mt-4">
            <h3 className="font-semibold mb-2">Order Items</h3>
            {menuItems.length > 0 ? (
              <ul className="space-y-2">
                {menuItems.map(item => (
                  <li key={item.id} className="border-b pb-2 last:border-b-0">
                    <div className="flex justify-between">
                      <div>
                        <p className="font-medium">{item.name}</p>
                        {item.description && <p className="text-sm text-gray-500">{item.description}</p>}
                      </div>
                      <p className="text-sm text-gray-600">${item.price.toFixed(2)}</p>
                    </div>
                  </li>
                ))}
              </ul>
            ) : (
              <p>No items found in this order</p>
            )}
          </div>
        </CardContent>

        <CardFooter className="flex justify-end gap-2">
          <Button variant="outline" onClick={() => router.push("/vendor/orders")}>
            Back
          </Button>
          
          {/* Status progression buttons */}
          {order.status === ORDER_STATUS.PLACED && (
            <Button onClick={() => handleStatusChange(ORDER_STATUS.PREPARED)}>
               Mark as Prepared
            </Button>
          )}
          
          {/* {order.status === ORDER_STATUS.PREPARING && (
            <Button onClick={() => handleStatusChange(ORDER_STATUS.PREPARED)}>
              Mark as Prepared
            </Button>
          )} */}
          
          {/* {order.status === ORDER_STATUS.PREPARED && (
            <Button onClick={() => handleStatusChange(ORDER_STATUS.COMPLETED)}>
              Complete Order
            </Button>
          )} */}
        </CardFooter>
      </Card>
    </div>
  );

  async function handleStatusChange(newStatus: string) {
    try {

      const res = await fetch(`${API_URL}/api/vendors/status/${order?.id}`, {
        method: 'PUT',  
        headers: tokenHeader(),
      });
      
      if (!res.ok) {
        const errorData = await res.json();
        throw new Error(errorData.message || "Failed to update order status");
      }
      
      const updatedOrder = await res.json();
      setOrder(updatedOrder);
      toast({ 
        title: "Success", 
        description: `Order status updated to ${newStatus}`,
        variant: "default"
      });
      
    } catch (err: any) {
      toast({
        title: "Update Failed",
        description: err.message,
        variant: "destructive"
      });
    }
  }
}