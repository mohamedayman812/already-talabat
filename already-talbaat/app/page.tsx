import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { ArrowRight, UtensilsCrossed } from "lucide-react"
import Image from "next/image"
import Link from "next/link"

export default function Home() {
  return (
    <div className="container mx-auto px-4 py-8">
      {/* Hero Section */}
      <section className="flex flex-col md:flex-row items-center justify-between py-12 gap-8">
        <div className="flex-1 space-y-6">
          <h1 className="text-4xl md:text-5xl font-bold tracking-tight">
            Food delivery made <span className="text-primary">simple</span>
          </h1>
          <p className="text-lg text-muted-foreground">
            Order food and groceries from your favorite restaurants and stores with just a few clicks.
          </p>
          <div className="flex flex-col sm:flex-row gap-4">
            <Link href="/restaurants">
              <Button size="lg" className="gap-2">
                Browse Restaurants <ArrowRight className="h-4 w-4" />
              </Button>
            </Link>
            <Link href="/auth/register">
              <Button size="lg" variant="outline">
                Sign Up
              </Button>
            </Link>
          </div>
        </div>
        <div className="flex-1">
          <Image
            src="/placeholder.svg?height=400&width=500"
            alt="Food delivery illustration"
            width={500}
            height={400}
            className="rounded-lg"
            priority
          />
        </div>
      </section>

      {/* Features Section */}
      <section className="py-12">
        <h2 className="text-3xl font-bold text-center mb-12">How it works</h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          <Card>
            <CardContent className="pt-6 flex flex-col items-center text-center">
              <div className="bg-primary/10 p-3 rounded-full mb-4">
                <UtensilsCrossed className="h-8 w-8 text-primary" />
              </div>
              <h3 className="text-xl font-semibold mb-2">Choose a Restaurant</h3>
              <p className="text-muted-foreground">
                Browse through our extensive list of restaurants and grocery stores.
              </p>
            </CardContent>
          </Card>
          <Card>
            <CardContent className="pt-6 flex flex-col items-center text-center">
              <div className="bg-primary/10 p-3 rounded-full mb-4">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="24"
                  height="24"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="2"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  className="h-8 w-8 text-primary"
                >
                  <path d="M6 2 3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4Z" />
                  <path d="M3 6h18" />
                  <path d="M16 10a4 4 0 0 1-8 0" />
                </svg>
              </div>
              <h3 className="text-xl font-semibold mb-2">Select Your Items</h3>
              <p className="text-muted-foreground">Add your favorite items to your cart and customize your order.</p>
            </CardContent>
          </Card>
          <Card>
            <CardContent className="pt-6 flex flex-col items-center text-center">
              <div className="bg-primary/10 p-3 rounded-full mb-4">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="24"
                  height="24"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="2"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  className="h-8 w-8 text-primary"
                >
                  <path d="m12 14 4-4" />
                  <path d="M3.34 19a10 10 0 1 1 17.32 0" />
                  <path d="M19 16.7V20" />
                  <path d="M5 16.7V20" />
                  <path d="M12 16.7V20" />
                </svg>
              </div>
              <h3 className="text-xl font-semibold mb-2">Fast Delivery</h3>
              <p className="text-muted-foreground">
                Track your order in real-time and enjoy fast delivery to your doorstep.
              </p>
            </CardContent>
          </Card>
        </div>
      </section>

      {/* Popular Restaurants Section */}
      <section className="py-12">
        <div className="flex justify-between items-center mb-8">
          <h2 className="text-3xl font-bold">Popular Restaurants</h2>
          <Link href="/restaurants">
            <Button variant="ghost" className="gap-2">
              View All <ArrowRight className="h-4 w-4" />
            </Button>
          </Link>
        </div>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {[1, 2, 3].map((i) => (
            <Link href={`/restaurants/${i}`} key={i}>
              <Card className="overflow-hidden hover:shadow-md transition-shadow">
                <Image
                  src={`/placeholder.svg?height=200&width=400&text=Restaurant ${i}`}
                  alt={`Restaurant ${i}`}
                  width={400}
                  height={200}
                  className="w-full h-48 object-cover"
                />
                <CardContent className="p-4">
                  <h3 className="font-semibold text-lg">Restaurant {i}</h3>
                  <p className="text-muted-foreground text-sm">Italian • 25-35 min</p>
                  <div className="flex items-center mt-2">
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="24"
                      height="24"
                      viewBox="0 0 24 24"
                      fill="none"
                      stroke="currentColor"
                      strokeWidth="2"
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      className="h-4 w-4 text-yellow-500 mr-1"
                    >
                      <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2" />
                    </svg>
                    <span className="text-sm">4.8 (120+ ratings)</span>
                  </div>
                </CardContent>
              </Card>
            </Link>
          ))}
        </div>
      </section>
    </div>
  )
}
