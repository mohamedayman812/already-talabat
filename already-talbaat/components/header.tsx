"use client"

import { useState, useEffect } from "react"
import Link from "next/link"
import { usePathname } from "next/navigation"
import { Button } from "@/components/ui/button"
import { ShoppingCart, User, Menu } from "lucide-react"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { Sheet, SheetContent, SheetTrigger } from "@/components/ui/sheet"
import { ModeToggle } from "./mode-toggle"

export default function Header() {
  const [isLoggedIn, setIsLoggedIn] = useState(false)
  const [userRole, setUserRole] = useState<string | null>(null)
  const pathname = usePathname()

  // Mock authentication check - in a real app, this would check your auth state
  useEffect(() => {
    // Check local storage for auth token
    const token = localStorage.getItem("auth-token")
    const role = localStorage.getItem("user-role")

    if (token) {
      setIsLoggedIn(true)
      setUserRole(role)
    }
  }, [])

  const handleLogout = () => {
    localStorage.removeItem("auth-token")
    localStorage.removeItem("user-role")
    setIsLoggedIn(false)
    setUserRole(null)
    // In a real app, you would redirect to home or login page
  }

  const navItems = [
    { name: "Home", href: "/" },
    { name: "Restaurants", href: "/restaurants" },
    { name: "About", href: "/about" },
  ]

  const roleBasedLinks = () => {
    if (!isLoggedIn) return null

    switch (userRole) {
      case "VENDOR":
        return (
          <>
            <DropdownMenuItem asChild>
              <Link href="/vendor/dashboard">Dashboard</Link>
            </DropdownMenuItem>
            <DropdownMenuItem asChild>
              <Link href="/vendor/menu">Manage Menu</Link>
            </DropdownMenuItem>
            <DropdownMenuItem asChild>
              <Link href="/vendor/orders">Orders</Link>
            </DropdownMenuItem>
          </>
        )
      case "DELIVERY":
        return (
          <>
            <DropdownMenuItem asChild>
              <Link href="/delivery/dashboard">Dashboard</Link>
            </DropdownMenuItem>
            <DropdownMenuItem asChild>
              <Link href="/delivery/orders">Manage Deliveries</Link>
            </DropdownMenuItem>
          </>
        )
      default: // CUSTOMER
        return (
          <>
            <DropdownMenuItem asChild>
              <Link href="/profile">My Profile</Link>
            </DropdownMenuItem>
            <DropdownMenuItem asChild>
              <Link href="/orders">My Orders</Link>
            </DropdownMenuItem>
          </>
        )
    }
  }

  return (
    <header className="sticky top-0 z-50 w-full border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
      <div className="container flex h-16 items-center justify-between">
        <div className="flex items-center gap-2 md:gap-6">
          <Sheet>
            <SheetTrigger asChild>
              <Button variant="ghost" size="icon" className="md:hidden">
                <Menu className="h-6 w-6" />
                <span className="sr-only">Toggle menu</span>
              </Button>
            </SheetTrigger>
            <SheetContent side="left" className="w-[300px] sm:w-[400px]">
              <nav className="flex flex-col gap-4 mt-8">
                {navItems.map((item) => (
                  <Link
                    key={item.href}
                    href={item.href}
                    className={`text-lg font-medium ${
                      pathname === item.href ? "text-primary" : "text-muted-foreground"
                    }`}
                  >
                    {item.name}
                  </Link>
                ))}
                {!isLoggedIn && (
                  <>
                    <Link href="/auth/login" className="text-lg font-medium">
                      Login
                    </Link>
                    <Link href="/auth/register" className="text-lg font-medium">
                      Register
                    </Link>
                  </>
                )}
              </nav>
            </SheetContent>
          </Sheet>

          <Link href="/" className="flex items-center space-x-2">
            <span className="font-bold text-xl">Already Talbt</span>
          </Link>

          <nav className="hidden md:flex gap-6 ml-6">
            {navItems.map((item) => (
              <Link
                key={item.href}
                href={item.href}
                className={`text-sm font-medium ${
                  pathname === item.href ? "text-primary" : "text-muted-foreground"
                } transition-colors hover:text-primary`}
              >
                {item.name}
              </Link>
            ))}
          </nav>
        </div>

        <div className="flex items-center gap-2">
          {isLoggedIn && userRole === "CUSTOMER" && (
            <Link href="/cart">
              <Button variant="ghost" size="icon">
                <ShoppingCart className="h-5 w-5" />
                <span className="sr-only">Cart</span>
              </Button>
            </Link>
          )}

          <ModeToggle />

          {isLoggedIn ? (
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button variant="ghost" size="icon" className="rounded-full">
                  <User className="h-5 w-5" />
                  <span className="sr-only">User menu</span>
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent align="end">
                {roleBasedLinks()}
                <DropdownMenuSeparator />
                <DropdownMenuItem asChild>
                  <Link href="/settings">Settings</Link>
                </DropdownMenuItem>
                <DropdownMenuItem onClick={handleLogout}>Logout</DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
          ) : (
            <div className="hidden md:flex items-center gap-2">
              <Link href="/auth/login">
                <Button variant="ghost" size="sm">
                  Login
                </Button>
              </Link>
              <Link href="/auth/register">
                <Button size="sm">Register</Button>
              </Link>
            </div>
          )}
        </div>
      </div>
    </header>
  )
}
