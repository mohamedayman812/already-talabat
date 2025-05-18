import Image from "next/image"
import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"

export default function AboutPage() {
  return (
    <div className="container mx-auto px-4 py-8">
      <section className="mb-12">
        <h1 className="text-4xl font-bold mb-6">About Already Talbt</h1>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-8 items-center">
          <div>
            <p className="text-lg mb-4">
              Already Talbt is a comprehensive food and grocery ordering platform designed to connect customers with
              their favorite restaurants and stores.
            </p>
            <p className="mb-4">
              Our mission is to make food delivery simple, efficient, and enjoyable for everyone involved - customers,
              restaurant owners, and delivery personnel.
            </p>
            <p className="mb-4">
              Founded in 2023, Already Talbt has quickly grown to become a trusted platform for food delivery services,
              focusing on quality, reliability, and customer satisfaction.
            </p>
          </div>
          <div className="flex justify-center">
            <Image
              src="/placeholder.svg?height=300&width=400&text=Already Talbt"
              alt="Already Talbt Platform"
              width={400}
              height={300}
              className="rounded-lg"
            />
          </div>
        </div>
      </section>

      <section className="mb-12">
        <h2 className="text-3xl font-bold mb-6">Our Services</h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <Card>
            <CardContent className="pt-6">
              <div className="text-center mb-4">
                <div className="bg-primary/10 p-3 rounded-full inline-block mb-2">
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
                    <path d="M15 11h.01" />
                    <path d="M11 15h.01" />
                    <path d="M16 16h.01" />
                    <path d="m2 16 20 6-6-20A20 20 0 0 0 2 16" />
                    <path d="M5.71 17.11a17.04 17.04 0 0 1 11.4-11.4" />
                  </svg>
                </div>
                <h3 className="text-xl font-semibold">Fast Delivery</h3>
              </div>
              <p className="text-center text-muted-foreground">
                We ensure your food arrives hot and fresh with our efficient delivery network.
              </p>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="pt-6">
              <div className="text-center mb-4">
                <div className="bg-primary/10 p-3 rounded-full inline-block mb-2">
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
                    <path d="M6 13.87A4 4 0 0 1 7.41 6a5.11 5.11 0 0 1 1.05-1.54 5 5 0 0 1 7.08 0A5.11 5.11 0 0 1 16.59 6 4 4 0 0 1 18 13.87V21H6Z" />
                    <line x1="6" x2="18" y1="17" y2="17" />
                  </svg>
                </div>
                <h3 className="text-xl font-semibold">Quality Restaurants</h3>
              </div>
              <p className="text-center text-muted-foreground">
                We partner with the best restaurants to provide a wide variety of quality food options.
              </p>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="pt-6">
              <div className="text-center mb-4">
                <div className="bg-primary/10 p-3 rounded-full inline-block mb-2">
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
                    <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10" />
                    <path d="m9 12 2 2 4-4" />
                  </svg>
                </div>
                <h3 className="text-xl font-semibold">Secure Payments</h3>
              </div>
              <p className="text-center text-muted-foreground">
                Our platform ensures secure and convenient payment options for all transactions.
              </p>
            </CardContent>
          </Card>
        </div>
      </section>

      <section className="mb-12">
        <h2 className="text-3xl font-bold mb-6">How It Works</h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          <div className="flex flex-col items-center text-center">
            <div className="bg-primary/10 w-12 h-12 rounded-full flex items-center justify-center mb-4">
              <span className="text-xl font-bold text-primary">1</span>
            </div>
            <h3 className="text-xl font-semibold mb-2">Choose a Restaurant</h3>
            <p className="text-muted-foreground">
              Browse through our extensive list of restaurants and grocery stores.
            </p>
          </div>

          <div className="flex flex-col items-center text-center">
            <div className="bg-primary/10 w-12 h-12 rounded-full flex items-center justify-center mb-4">
              <span className="text-xl font-bold text-primary">2</span>
            </div>
            <h3 className="text-xl font-semibold mb-2">Place Your Order</h3>
            <p className="text-muted-foreground">
              Select your favorite items, customize your order, and choose your payment method.
            </p>
          </div>

          <div className="flex flex-col items-center text-center">
            <div className="bg-primary/10 w-12 h-12 rounded-full flex items-center justify-center mb-4">
              <span className="text-xl font-bold text-primary">3</span>
            </div>
            <h3 className="text-xl font-semibold mb-2">Enjoy Your Delivery</h3>
            <p className="text-muted-foreground">
              Track your order in real-time and enjoy fast delivery to your doorstep.
            </p>
          </div>
        </div>
      </section>

      <section className="mb-12">
        <h2 className="text-3xl font-bold mb-6">Our Team</h2>
        <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
          {[
            { name: "Sarah Johnson", role: "CEO & Founder" },
            { name: "Michael Chen", role: "CTO" },
            { name: "Aisha Patel", role: "Operations Manager" },
            { name: "David Rodriguez", role: "Marketing Director" },
          ].map((member, index) => (
            <Card key={index}>
              <CardContent className="pt-6 text-center">
                <Image
                  src={`/placeholder.svg?height=150&width=150&text=${member.name.split(" ")[0]}`}
                  alt={member.name}
                  width={150}
                  height={150}
                  className="rounded-full mx-auto mb-4"
                />
                <h3 className="font-semibold text-lg">{member.name}</h3>
                <p className="text-muted-foreground">{member.role}</p>
              </CardContent>
            </Card>
          ))}
        </div>
      </section>

      <section>
        <div className="bg-muted rounded-lg p-8 text-center">
          <h2 className="text-3xl font-bold mb-4">Join Already Talbt Today</h2>
          <p className="text-lg mb-6 max-w-2xl mx-auto">
            Whether you're hungry for your favorite meal, a restaurant owner looking to expand your business, or
            interested in joining our delivery team, Already Talbt has something for you.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Link href="/auth/register">
              <Button size="lg">Sign Up Now</Button>
            </Link>
            <Link href="/restaurants">
              <Button variant="outline" size="lg">
                Explore Restaurants
              </Button>
            </Link>
          </div>
        </div>
      </section>
    </div>
  )
}
