"use client"

import { useState, useEffect } from "react"
import { useRouter } from "next/navigation"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { Textarea } from "@/components/ui/textarea"
import { useToast } from "@/hooks/use-toast"
import { Star } from "lucide-react"

// Mock order data
const mockOrder = {
  id: "ORD-001",
  date: "2023-05-15T14:30:00",
  restaurant: {
    id: "1",
    name: "Pasta Paradise",
  },
  items: [
    { name: "Spaghetti Carbonara", quantity: 1, price: 12.99 },
    { name: "Tiramisu", quantity: 1, price: 7.99 },
  ],
  total: 23.97,
  status: "delivered",
  deliveryPerson: "John Smith",
}

export default function FeedbackPage({ params }) {
  const [order, setOrder] = useState(null)
  const [isLoading, setIsLoading] = useState(true)
  const [foodRating, setFoodRating] = useState(0)
  const [deliveryRating, setDeliveryRating] = useState(0)
  const [comment, setComment] = useState("")
  const [isSubmitting, setIsSubmitting] = useState(false)

  const router = useRouter()
  const { toast } = useToast()

  useEffect(() => {
    // Check if user is logged in
    const token = localStorage.getItem("auth-token")
    if (!token) {
      toast({
        title: "Authentication required",
        description: "Please log in to leave feedback.",
        variant: "destructive",
      })
      router.push("/auth/login")
      return
    }

    // In a real app, you would fetch the order from your API
    // Simulate API call
    setTimeout(() => {
      setOrder({ ...mockOrder, id: params.id })
      setIsLoading(false)
    }, 1000)
  }, [params.id, router, toast])

  const handleSubmit = async (e) => {
    e.preventDefault()

    if (foodRating === 0 || deliveryRating === 0) {
      toast({
        title: "Rating required",
        description: "Please rate both the food and delivery service.",
        variant: "destructive",
      })
      return
    }

    setIsSubmitting(true)

    try {
      // In a real app, you would submit the feedback to your API
      // Simulate API call
      await new Promise((resolve) => setTimeout(resolve, 1500))

      toast({
        title: "Feedback submitted",
        description: "Thank you for your feedback!",
      })

      router.push("/orders")
    } catch (error) {
      toast({
        title: "Submission failed",
        description: "There was an error submitting your feedback. Please try again.",
        variant: "destructive",
      })
    } finally {
      setIsSubmitting(false)
    }
  }

  const renderStarRating = (rating, setRating, label) => {
    return (
      <div className="space-y-2">
        <p className="font-medium">{label}</p>
        <div className="flex gap-1">
          {[1, 2, 3, 4, 5].map((star) => (
            <button
              key={star}
              type="button"
              onClick={() => setRating(star)}
              className="focus:outline-none"
              aria-label={`${star} stars`}
            >
              <Star
                className={`h-8 w-8 ${
                  star <= rating ? "text-yellow-400 fill-yellow-400" : "text-gray-300 dark:text-gray-600"
                } hover:text-yellow-400 transition-colors`}
              />
            </button>
          ))}
        </div>
      </div>
    )
  }

  if (isLoading) {
    return (
      <div className="container mx-auto px-4 py-8 flex justify-center items-center min-h-[60vh]">
        <p>Loading...</p>
      </div>
    )
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">Leave Feedback</h1>

      <Card>
        <CardHeader>
          <CardTitle>Order #{order.id}</CardTitle>
          <CardDescription>
            {new Date(order.date).toLocaleDateString()} • {order.restaurant.name}
          </CardDescription>
        </CardHeader>
        <form onSubmit={handleSubmit}>
          <CardContent className="space-y-6">
            <div className="space-y-4">
              {renderStarRating(foodRating, setFoodRating, "How would you rate the food?")}
              {renderStarRating(deliveryRating, setDeliveryRating, "How would you rate the delivery service?")}
            </div>

            <div className="space-y-2">
              <p className="font-medium">Additional comments</p>
              <Textarea
                placeholder="Share your experience with this order..."
                value={comment}
                onChange={(e) => setComment(e.target.value)}
                rows={4}
              />
            </div>
          </CardContent>
          <CardFooter>
            <Button type="submit" className="w-full" disabled={isSubmitting}>
              {isSubmitting ? "Submitting..." : "Submit Feedback"}
            </Button>
          </CardFooter>
        </form>
      </Card>
    </div>
  )
}
