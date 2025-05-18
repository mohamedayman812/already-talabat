"use client"

import { useState, useEffect } from "react"
import { useRouter } from "next/navigation"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Switch } from "@/components/ui/switch"
import { Label } from "@/components/ui/label"
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group"
import { useToast } from "@/hooks/use-toast"

export default function SettingsPage() {
  const [isLoading, setIsLoading] = useState(true)
  const [settings, setSettings] = useState({
    notifications: {
      email: true,
      push: true,
      orderUpdates: true,
      promotions: false,
    },
    preferences: {
      language: "english",
      currency: "usd",
      theme: "system",
    },
    privacy: {
      shareData: false,
      locationTracking: true,
    },
  })

  const router = useRouter()
  const { toast } = useToast()

  useEffect(() => {
    // Check if user is logged in
    const token = localStorage.getItem("auth-token")
    if (!token) {
      toast({
        title: "Authentication required",
        description: "Please log in to view your settings.",
        variant: "destructive",
      })
      router.push("/auth/login")
      return
    }

    // In a real app, you would fetch user settings from your API
    // Simulate API call
    setTimeout(() => {
      setIsLoading(false)
    }, 1000)
  }, [router, toast])

  const handleToggleChange = (category, setting) => {
    setSettings((prev) => ({
      ...prev,
      [category]: {
        ...prev[category],
        [setting]: !prev[category][setting],
      },
    }))
  }

  const handleRadioChange = (category, setting, value) => {
    setSettings((prev) => ({
      ...prev,
      [category]: {
        ...prev[category],
        [setting]: value,
      },
    }))
  }

  const handleSaveSettings = async () => {
    // In a real app, you would call your API to save the settings
    // Simulate API call
    try {
      await new Promise((resolve) => setTimeout(resolve, 1000))

      toast({
        title: "Settings saved",
        description: "Your settings have been saved successfully.",
      })
    } catch (error) {
      toast({
        title: "Save failed",
        description: "There was an error saving your settings.",
        variant: "destructive",
      })
    }
  }

  if (isLoading) {
    return (
      <div className="container mx-auto px-4 py-8 flex justify-center items-center min-h-[60vh]">
        <p>Loading settings...</p>
      </div>
    )
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">Settings</h1>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        <Card>
          <CardHeader>
            <CardTitle>Notifications</CardTitle>
            <CardDescription>Manage how you receive notifications and updates.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="flex items-center justify-between">
              <div className="space-y-0.5">
                <Label htmlFor="email-notifications">Email Notifications</Label>
                <p className="text-sm text-muted-foreground">Receive notifications via email.</p>
              </div>
              <Switch
                id="email-notifications"
                checked={settings.notifications.email}
                onCheckedChange={() => handleToggleChange("notifications", "email")}
              />
            </div>

            <div className="flex items-center justify-between">
              <div className="space-y-0.5">
                <Label htmlFor="push-notifications">Push Notifications</Label>
                <p className="text-sm text-muted-foreground">Receive notifications on your device.</p>
              </div>
              <Switch
                id="push-notifications"
                checked={settings.notifications.push}
                onCheckedChange={() => handleToggleChange("notifications", "push")}
              />
            </div>

            <div className="flex items-center justify-between">
              <div className="space-y-0.5">
                <Label htmlFor="order-updates">Order Updates</Label>
                <p className="text-sm text-muted-foreground">Receive updates about your orders.</p>
              </div>
              <Switch
                id="order-updates"
                checked={settings.notifications.orderUpdates}
                onCheckedChange={() => handleToggleChange("notifications", "orderUpdates")}
              />
            </div>

            <div className="flex items-center justify-between">
              <div className="space-y-0.5">
                <Label htmlFor="promotions">Promotions and Offers</Label>
                <p className="text-sm text-muted-foreground">Receive promotional emails and special offers.</p>
              </div>
              <Switch
                id="promotions"
                checked={settings.notifications.promotions}
                onCheckedChange={() => handleToggleChange("notifications", "promotions")}
              />
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Preferences</CardTitle>
            <CardDescription>Customize your app experience.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-6">
            <div className="space-y-2">
              <Label>Language</Label>
              <RadioGroup
                value={settings.preferences.language}
                onValueChange={(value) => handleRadioChange("preferences", "language", value)}
              >
                <div className="flex items-center space-x-2">
                  <RadioGroupItem value="english" id="english" />
                  <Label htmlFor="english">English</Label>
                </div>
                <div className="flex items-center space-x-2">
                  <RadioGroupItem value="french" id="french" />
                  <Label htmlFor="french">French</Label>
                </div>
                <div className="flex items-center space-x-2">
                  <RadioGroupItem value="spanish" id="spanish" />
                  <Label htmlFor="spanish">Spanish</Label>
                </div>
              </RadioGroup>
            </div>

            <div className="space-y-2">
              <Label>Currency</Label>
              <RadioGroup
                value={settings.preferences.currency}
                onValueChange={(value) => handleRadioChange("preferences", "currency", value)}
              >
                <div className="flex items-center space-x-2">
                  <RadioGroupItem value="usd" id="usd" />
                  <Label htmlFor="usd">USD ($)</Label>
                </div>
                <div className="flex items-center space-x-2">
                  <RadioGroupItem value="eur" id="eur" />
                  <Label htmlFor="eur">EUR (€)</Label>
                </div>
                <div className="flex items-center space-x-2">
                  <RadioGroupItem value="gbp" id="gbp" />
                  <Label htmlFor="gbp">GBP (£)</Label>
                </div>
              </RadioGroup>
            </div>

            <div className="space-y-2">
              <Label>Theme</Label>
              <RadioGroup
                value={settings.preferences.theme}
                onValueChange={(value) => handleRadioChange("preferences", "theme", value)}
              >
                <div className="flex items-center space-x-2">
                  <RadioGroupItem value="light" id="light" />
                  <Label htmlFor="light">Light</Label>
                </div>
                <div className="flex items-center space-x-2">
                  <RadioGroupItem value="dark" id="dark" />
                  <Label htmlFor="dark">Dark</Label>
                </div>
                <div className="flex items-center space-x-2">
                  <RadioGroupItem value="system" id="system" />
                  <Label htmlFor="system">System</Label>
                </div>
              </RadioGroup>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Privacy</CardTitle>
            <CardDescription>Manage your privacy settings.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="flex items-center justify-between">
              <div className="space-y-0.5">
                <Label htmlFor="share-data">Data Sharing</Label>
                <p className="text-sm text-muted-foreground">
                  Allow us to share anonymized data to improve our services.
                </p>
              </div>
              <Switch
                id="share-data"
                checked={settings.privacy.shareData}
                onCheckedChange={() => handleToggleChange("privacy", "shareData")}
              />
            </div>

            <div className="flex items-center justify-between">
              <div className="space-y-0.5">
                <Label htmlFor="location-tracking">Location Tracking</Label>
                <p className="text-sm text-muted-foreground">Allow location tracking for delivery services.</p>
              </div>
              <Switch
                id="location-tracking"
                checked={settings.privacy.locationTracking}
                onCheckedChange={() => handleToggleChange("privacy", "locationTracking")}
              />
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Account Actions</CardTitle>
            <CardDescription>Manage your account settings.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            <Button variant="outline" className="w-full" onClick={() => router.push("/profile")}>
              Edit Profile
            </Button>
            <Button variant="outline" className="w-full text-yellow-600 hover:text-yellow-700">
              Download My Data
            </Button>
            <Button variant="outline" className="w-full text-red-600 hover:text-red-700">
              Delete Account
            </Button>
          </CardContent>
        </Card>
      </div>

      <div className="flex justify-end mt-8">
        <Button size="lg" onClick={handleSaveSettings}>
          Save All Settings
        </Button>
      </div>
    </div>
  )
}
