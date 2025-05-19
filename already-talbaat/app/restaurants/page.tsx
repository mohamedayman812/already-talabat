"use client"

import { useState, useEffect } from "react"
import Link from "next/link"
import { Card, CardContent } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { MapPin } from "lucide-react"
import { useToast } from "@/hooks/use-toast"
import { API_URL } from "@/lib/utils"

interface Restaurant {
  id: string
  name: string
  address: string
  businessHours?: string
  // if you add 'cuisine' server-side, uncomment this:
  // cuisine?: string
}

export default function RestaurantsPage() {
  const [restaurants, setRestaurants] = useState<Restaurant[]>([])
  const [filtered,    setFiltered]    = useState<Restaurant[]>([])
  const [searchQuery, setSearchQuery] = useState("")
  const [activeTab,   setActiveTab]   = useState("all")
  const [isLoading,   setIsLoading]   = useState(true)
  const [error,       setError]       = useState<string | null>(null)
  const { toast } = useToast()

  useEffect(() => {
    async function load() {
      try {
        const res = await fetch(`${API_URL}/api/restaurants`)
        if (!res.ok) throw new Error("Failed to load restaurants")
        const data: Restaurant[] = await res.json()
        setRestaurants(data)
        setFiltered(data)
      } catch (err: any) {
        console.error(err)
        setError(err.message)
        toast({ title: "Error", description: err.message, variant: "destructive" })
      } finally {
        setIsLoading(false)
      }
    }
    load()
  }, [])

  useEffect(() => {
    let arr = [...restaurants]

    // 1) search
    if (searchQuery) {
      const q = searchQuery.toLowerCase()
      arr = arr.filter(r =>
        r.name.toLowerCase().includes(q) ||
        r.address.toLowerCase().includes(q)
      )
    }

    // 2) tab filter (if you have a "cuisine" field)
    // if (activeTab !== "all" && cuisineFieldExists) {
    //   arr = arr.filter(r => r.cuisine?.toLowerCase() === activeTab)
    // }

    setFiltered(arr)
  }, [searchQuery, activeTab, restaurants])

  if (isLoading) return <p>Loading restaurants…</p>
  if (error)     return <p className="text-red-600">{error}</p>

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-6">Restaurants</h1>

      <div className="relative mb-6">
        <Input
          placeholder="Search by name or address…"
          className="pl-3"
          value={searchQuery}
          onChange={e => setSearchQuery(e.target.value)}
        />
      </div>

      <Tabs defaultValue="all" onValueChange={setActiveTab} className="mb-8">
        <TabsContent value="all" className="mt-0">
          <Grid restaurants={filtered} />
        </TabsContent>
      </Tabs>
    </div>
  )
}

function Grid({ restaurants }: { restaurants: Restaurant[] }) {
  if (restaurants.length === 0) {
    return <p>No restaurants found.</p>
  }
  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      {restaurants.map(r => (
        <Link key={r.id} href={`/restaurants/${r.id}`}>
          <Card className="hover:shadow-md transition-shadow">
            <CardContent className="space-y-2">
              <h3 className="font-semibold text-lg">{r.name}</h3>
              <div className="flex items-center text-sm text-muted-foreground">
                <MapPin className="h-4 w-4 mr-1" />
                {r.address}
              </div>
              {r.businessHours && (
                <p className="text-sm text-muted-foreground">
                  Hours: {r.businessHours}
                </p>
              )}
            </CardContent>
          </Card>
        </Link>
      ))}
    </div>
  )
}
