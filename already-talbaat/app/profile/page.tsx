"use client"

import { useState, useEffect } from "react"
import { useRouter } from "next/navigation"
import { Button } from "@/components/ui/button"
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Textarea } from "@/components/ui/textarea"
import { useToast } from "@/hooks/use-toast"
import { API_URL } from "@/lib/utils"

type User = {
  id: string
  username: string
  email: string
  address: string
  phone: string
  role: string
}

export default function ProfilePage() {
  const [user, setUser] = useState<User | null>(null)
  const [isLoading, setIsLoading] = useState(true)

  // Profile form state
  const [isProfileEditing, setIsProfileEditing] = useState(false)
  const [profileForm, setProfileForm] = useState({
    username: "",
    email: "",
    address: "",
    phone: "",
  })

  // Password form state
  const [passwordForm, setPasswordForm] = useState({
    currentPassword: "",
    newPassword: "",
    confirmPassword: "",
  })

  // Vendor / Restaurant state
  const [restaurant, setRestaurant] = useState<{
    id: string
    name: string
    address: string
    businessHours: string
  }>({ id: "", name: "", address: "", businessHours: "" })
  const [isRestEditing, setIsRestEditing] = useState(false)
  const [restForm, setRestForm] = useState({
    name: "",
    address: "",
    businessHours: "",
  })

  const router = useRouter()
  const { toast } = useToast()

  useEffect(() => {
    const token = localStorage.getItem("auth-token")
    if (!token) {
      toast({
        title: "Login required",
        description: "Please sign in.",
        variant: "destructive",
      })
      router.push("/auth/login")
      return
    }

    async function loadData() {
      try {
        // 1) Authenticated user
        const userRes = await fetch(`${API_URL}/api/auth/me`, {
          headers: { Authorization: `Bearer ${token}` },
        })
        if (!userRes.ok) throw new Error("Failed to fetch user data")
        const u: User = await userRes.json()
        setUser(u)
        setProfileForm({
          username: u.username,
          email: u.email,
          address: u.address,
          phone: u.phone,
        })

        // 2) Vendor‐specific
        if (u.role === "VENDOR") {
          const vRes = await fetch(`${API_URL}/api/vendors/me`, {
            headers: { Authorization: `Bearer ${token}` },
          })
          if (!vRes.ok) throw new Error("Failed to fetch vendor data")
          const v = await vRes.json()

          const rRes = await fetch(
            `${API_URL}/api/restaurants/${v.restaurantId}`,
            { headers: { Authorization: `Bearer ${token}` } }
          )
          if (!rRes.ok) throw new Error("Failed to fetch restaurant data")
          const r = await rRes.json()
          setRestaurant(r)
          setRestForm({
            name: r.name,
            address: r.address,
            businessHours: r.businessHours || "",
          })
        }
      } catch (err: any) {
        console.error("loadData error:", err)
        toast({
          title: "Error",
          description: err.message || "Could not load data",
          variant: "destructive",
        })
      } finally {
        setIsLoading(false)
      }
    }

    loadData()
  }, [router, toast])

  const handleProfileChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) =>
    setProfileForm((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }))
  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) =>
    setPasswordForm((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }))
  const handleRestChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) =>
    setRestForm((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }))

  // Save profile
  const saveProfile = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!user) return
    const token = localStorage.getItem("auth-token")

    const res = await fetch(`${API_URL}/api/users/${user.id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        username: profileForm.username,
        email: profileForm.email,
        address: profileForm.address,
        phone: profileForm.phone,
      }),
    })

    if (!res.ok) {
      const errText = await res.text()
      console.error("Profile update failed:", res.status, errText)
      toast({
        title: "Error",
        description: errText || `HTTP ${res.status}`,
        variant: "destructive",
      })
      return
    }

    const updated: User = await res.json()
    setUser(updated)
    setProfileForm({
      username: updated.username,
      email: updated.email,
      address: updated.address,
      phone: updated.phone,
    })
    setIsProfileEditing(false)
    toast({ title: "Saved", description: "Profile updated." })
  }

  // Save password (unchanged)…
  const savePassword = async (e: React.FormEvent) => {
    e.preventDefault()
    if (passwordForm.newPassword !== passwordForm.confirmPassword) {
      toast({
        title: "Mismatch",
        description: "Passwords must match",
        variant: "destructive",
      })
      return
    }
    const token = localStorage.getItem("auth-token")
    const res = await fetch(`${API_URL}/api/auth/change-password`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        currentPassword: passwordForm.currentPassword,
        newPassword: passwordForm.newPassword,
      }),
    })
    if (!res.ok) {
      const err = await res.text()
      toast({ title: "Error", description: err, variant: "destructive" })
      return
    }
    setPasswordForm({ currentPassword: "", newPassword: "", confirmPassword: "" })
    toast({ title: "Saved", description: "Password updated." })
  }

  // Save restaurant (unchanged)…
  const saveRestaurant = async (e: React.FormEvent) => {
    e.preventDefault()
    const token = localStorage.getItem("auth-token")
    const res = await fetch(`${API_URL}/api/restaurants/${restaurant.id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(restForm),
    })
    if (!res.ok) {
      const err = await res.text()
      toast({ title: "Error", description: err, variant: "destructive" })
      return
    }
    setRestaurant((prev) => ({ ...prev, ...restForm }))
    setIsRestEditing(false)
    toast({ title: "Saved", description: "Restaurant updated." })
  }

  if (isLoading) return <p>Loading profile...</p>
  if (!user) return <p>Could not load profile.</p>

  return (
    <div className="container mx-auto py-8">
      <h1 className="text-3xl mb-6">My Profile</h1>
      <div className="grid lg:grid-cols-3 gap-8">
        {/* LEFT COLUMN: TABS */}
        <div className="lg:col-span-2">
          <Tabs defaultValue="profile">
            <TabsList className="mb-4">
              <TabsTrigger value="profile">Profile</TabsTrigger>
              <TabsTrigger value="security">Security</TabsTrigger>
              {user.role === "VENDOR" && (
                <TabsTrigger value="vendor">Vendor Settings</TabsTrigger>
              )}
            </TabsList>

            {/* PROFILE TAB */}
            <TabsContent value="profile">
              <Card as="form" onSubmit={saveProfile}>
                <CardHeader>
                  <CardTitle>Profile Information</CardTitle>
                </CardHeader>
                <CardContent className="space-y-4">
                  <Label>Username</Label>
                  <Input
                    name="username"
                    value={profileForm.username}
                    onChange={handleProfileChange}
                    disabled={!isProfileEditing}
                  />
                  <Label>Email</Label>
                  <Input
                    name="email"
                    type="email"
                    value={profileForm.email}
                    onChange={handleProfileChange}
                    disabled={!isProfileEditing}
                  />
                  <Label>Address</Label>
                  <Textarea
                    name="address"
                    value={profileForm.address}
                    onChange={handleProfileChange}
                    disabled={!isProfileEditing}
                  />
                  <Label>Phone</Label>
                  <Input
                    name="phone"
                    value={profileForm.phone}
                    onChange={handleProfileChange}
                    disabled={!isProfileEditing}
                  />
                </CardContent>
                <CardFooter>
                  {/** ← Conditional Edit vs Save/Cancel */}
                  {isProfileEditing ? (
                    <>
                      <Button
                        type="button"
                        variant="outline"
                        onClick={() => setIsProfileEditing(false)}
                      >
                        Cancel
                      </Button>
                      <Button type="submit">Save</Button>
                    </>
                  ) : (
                    <Button
                      type="button"
                      onClick={() => setIsProfileEditing(true)}
                    >
                      Edit Profile
                    </Button>
                  )}
                </CardFooter>
              </Card>
            </TabsContent>

            {/* SECURITY TAB */}
            <TabsContent value="security">
              <Card as="form" onSubmit={savePassword}>
                <CardHeader>
                  <CardTitle>Security</CardTitle>
                </CardHeader>
                <CardContent className="space-y-4">
                  <Label>Current Password</Label>
                  <Input
                    name="currentPassword"
                    type="password"
                    value={passwordForm.currentPassword}
                    onChange={handlePasswordChange}
                    required
                  />
                  <Label>New Password</Label>
                  <Input
                    name="newPassword"
                    type="password"
                    value={passwordForm.newPassword}
                    onChange={handlePasswordChange}
                    required
                  />
                  <Label>Confirm New Password</Label>
                  <Input
                    name="confirmPassword"
                    type="password"
                    value={passwordForm.confirmPassword}
                    onChange={handlePasswordChange}
                    required
                  />
                </CardContent>
                <CardFooter>
                  <Button type="submit">Update Password</Button>
                </CardFooter>
              </Card>
            </TabsContent>

            {/* VENDOR SETTINGS */}
            {user.role === "VENDOR" && (
              <TabsContent value="vendor">
                <Card as="form" onSubmit={saveRestaurant}>
                  <CardHeader>
                    <CardTitle>Vendor Settings</CardTitle>
                  </CardHeader>
                  <CardContent className="space-y-4">
                    <Label>Restaurant Name</Label>
                    <Input
                      name="name"
                      value={restForm.name}
                      onChange={handleRestChange}
                      disabled={!isRestEditing}
                    />
                    <Label>Restaurant Address</Label>
                    <Textarea
                      name="address"
                      value={restForm.address}
                      onChange={handleRestChange}
                      disabled={!isRestEditing}
                    />
                    <Label>Business Hours</Label>
                    <Input
                      name="businessHours"
                      value={restForm.businessHours}
                      onChange={handleRestChange}
                      disabled={!isRestEditing}
                    />
                  </CardContent>
                  <CardFooter>
                    <Button
                      type="button"
                      variant="outline"
                      onClick={() => setIsRestEditing(false)}
                    >
                      Cancel
                    </Button>
                    <Button type="submit">Save Restaurant</Button>
                  </CardFooter>
                </Card>
              </TabsContent>
            )}
          </Tabs>
        </div>

        {/* RIGHT COLUMN: SUMMARY */}
        <div>
          <Card>
            <CardHeader>
              <CardTitle>Account Summary</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <p>
                <strong>Username:</strong> {user.username}
              </p>
              <p>
                <strong>Email:</strong> {user.email}
              </p>
              <p>
                <strong>Role:</strong> {user.role}
              </p>
            </CardContent>
            <CardFooter className="flex flex-col gap-2">
              <Button
                variant="outline"
                onClick={() => router.push("/orders")}
              >
                View Orders
              </Button>
              {user.role === "VENDOR" && (
                <Button
                  variant="outline"
                  onClick={() => router.push("/vendor/dashboard")}
                >
                  Vendor Dashboard
                </Button>
              )}
            </CardFooter>
          </Card>
        </div>
      </div>
    </div>
  )
}