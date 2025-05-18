"use client"

import { useEffect, useState } from "react"
import { Plus, Edit, Trash2 } from "lucide-react"
import { API_URL } from "@/lib/utils"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { useToast } from "@/hooks/use-toast"

type MenuItemDTO = { id: string; name: string; description: string; price: number }

export default function VendorMenuPage() {
  const { toast } = useToast()

  /* ------------------------------------------------ state */
  const [restaurantId, setRestaurantId] = useState("")
  const [menuItems, setMenuItems] = useState<MenuItemDTO[]>([])
  const [isLoading, setIsLoading] = useState(true)

  const [isAdding, setIsAdding] = useState(false)
  const [newItem, setNewItem] = useState({ name: "", description: "", price: "", restaurantId: "" })

  const [isEditing, setIsEditing] = useState(false)
  const [editItem, setEditItem] = useState<MenuItemDTO | null>(null)

  /* ---------------------------------------------- bootstrap */
  useEffect(() => {
    const token = localStorage.getItem("auth-token")
    if (!token) {
      toast({ title:"Login required", description:"You must be a vendor to manage the menu.", variant:"destructive" })
      return
    }

    ;(async () => {
      try {
        /* vendor → restaurantId */
        const vRes = await fetch(`${API_URL}/api/vendors`, { headers:{ Authorization:`Bearer ${token}` } })
        if (!vRes.ok) throw new Error(await vRes.text())
        const vendor = await vRes.json()
        setRestaurantId(vendor.restaurantId)

        /* menu items */
        const mRes = await fetch(`${API_URL}/api/restaurants/menuItems/${vendor.restaurantId}`, { headers:{ Authorization:`Bearer ${token}` } })
        if (!mRes.ok) throw new Error(await mRes.text())
        setMenuItems(await mRes.json())
      } catch (e:any) {
        toast({ title:"Error", description:e.message, variant:"destructive" })
      } finally { setIsLoading(false) }
    })()
  }, [toast])

  /* helper */
  const tokenHeader = () => ({ Authorization:`Bearer ${localStorage.getItem("auth-token")}` })

  /* ----------------------------------------------- ADD */
  const handleAddItem = async () => {
    try {
      const body = { name:newItem.name, description:newItem.description, price:parseFloat(newItem.price), restaurantId}
      const res  = await fetch(`${API_URL}/api/vendors/vendorAdd/menuitems`, {
        method:"POST",
        headers:{ "Content-Type":"application/json", ...tokenHeader() },
        body: JSON.stringify(body),
      })
      if (!res.ok) throw new Error(await res.text() || `HTTP ${res.status}`)

      const created:MenuItemDTO = await res.json()
      setMenuItems(prev=>[ ...prev, created ])
      setNewItem({ name:"", description:"", price:"", restaurantId:"" })
      setIsAdding(false)
      toast({ title:"Added", description:`"${created.name}" created.` })
    } catch(e:any) { toast({ title:"Error", description:e.message, variant:"destructive" }) }
  }

  /* ----------------------------------------------- EDIT */
  const handleEditClick = (item: MenuItemDTO) => {
    setEditItem(item)
    setIsEditing(true)
  }

  const handleSaveEdit = async () => {
    if (!editItem) return
    try {
      const body = { name:editItem.name, description:editItem.description, price:editItem.price }
      const res  = await fetch(`${API_URL}/api/vendors/vendorUpdate/menuitems/${editItem.id}`, {
        method:"PUT",
        headers:{ "Content-Type":"application/json", ...tokenHeader() },
        body: JSON.stringify(body),
      })
      if (!res.ok) throw new Error(await res.text() || `HTTP ${res.status}`)
      const updated:MenuItemDTO = await res.json()
      setMenuItems(prev => prev.map(mi => mi.id===updated.id ? updated : mi))
      setIsEditing(false)
      setEditItem(null)
      toast({ title:"Saved", description:`"${updated.name}" updated.` })
    } catch(e:any) { toast({ title:"Error", description:e.message, variant:"destructive" }) }
  }

  /* ----------------------------------------------- DELETE */
  const remove = async (id:string,name:string) => {
    if (!confirm(`Delete "${name}"?`)) return
    try {
      const res = await fetch(`${API_URL}/api/vendors/vendorDelete/menuitems/${id}`, {
        method:"DELETE", headers: tokenHeader()
      })
      if (!res.ok) throw new Error(await res.text() || `HTTP ${res.status}`)
      setMenuItems(prev => prev.filter(mi => mi.id!==id))
      toast({ title:"Deleted", description:`"${name}" removed.` })
    } catch(e:any) { toast({ title:"Error", description:e.message, variant:"destructive" }) }
  }

  /* ------------------------------------------------ UI */
  if (isLoading) return <p className="p-8">Loading menu…</p>

  return (
    <div className="container mx-auto py-8">
      {/* header + add-dialog */}
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Menu Management</h1>
        <Dialog open={isAdding} onOpenChange={setIsAdding}>
          <DialogTrigger asChild>
            <Button variant="outline" className="gap-2">
              <Plus size={18}/>Add item
            </Button>
          </DialogTrigger>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Add new item</DialogTitle>
              <DialogDescription>Create a menu entry.</DialogDescription>
            </DialogHeader>
            <div className="space-y-4 py-4">
              <Label>Name</Label>
              <Input value={newItem.name} onChange={e=>setNewItem({...newItem,name:e.target.value})}/>
              <Label>Description</Label>
              <Textarea value={newItem.description} onChange={e=>setNewItem({...newItem,description:e.target.value})}/>
              <Label>Price ($)</Label>
              <Input type="number" step="0.01" value={newItem.price} onChange={e=>setNewItem({...newItem,price:e.target.value})}/>
            </div>
            <DialogFooter>
              <Button variant="outline" onClick={()=>setIsAdding(false)}>Cancel</Button>
              <Button onClick={handleAddItem}>Add</Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>
      </div>

      {/* menu grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {menuItems.map(item=>(
          <Card key={item.id}>
            <CardHeader><CardTitle className="text-lg">{item.name}</CardTitle></CardHeader>
            <CardContent className="space-y-2">
              <p className="text-sm text-muted-foreground">{item.description}</p>
              <p className="font-medium">${item.price.toFixed(2)}</p>
            </CardContent>
            <CardFooter className="justify-end gap-2">
              {/* edit dialog */}
              <Dialog open={isEditing && editItem?.id === item.id} onOpenChange={(open) => {
                if (!open) {
                  setIsEditing(false)
                  setEditItem(null)
                }
              }}>
                <Button 
                  size="icon" 
                  variant="outline" 
                  onClick={() => handleEditClick(item)}
                >
                  <Edit size={16}/>
                </Button>
                <DialogContent>
                  <DialogHeader>
                    <DialogTitle>Edit item</DialogTitle>
                  </DialogHeader>
                  <div className="space-y-4 py-4">
                    <Label>Name</Label>
                    <Input 
                      value={editItem?.name || ''} 
                      onChange={e => editItem && setEditItem({...editItem, name:e.target.value})}
                    />
                    <Label>Description</Label>
                    <Textarea 
                      value={editItem?.description || ''} 
                      onChange={e => editItem && setEditItem({...editItem, description:e.target.value})}
                    />
                    <Label>Price ($)</Label>
                    <Input 
                      type="number" 
                      step="0.01" 
                      value={editItem?.price || 0} 
                      onChange={e => editItem && setEditItem({...editItem, price:parseFloat(e.target.value)})}
                    />
                  </div>
                  <DialogFooter>
                    <Button variant="outline" onClick={() => {
                      setIsEditing(false)
                      setEditItem(null)
                    }}>
                      Cancel
                    </Button>
                    <Button onClick={handleSaveEdit}>Save</Button>
                  </DialogFooter>
                </DialogContent>
              </Dialog>
              
              {/* delete button */}
              <Button 
                size="icon" 
                variant="ghost" 
                className="text-red-600 hover:text-red-800" 
                onClick={()=>remove(item.id,item.name)}
              >
                <Trash2 size={16}/>
              </Button>
            </CardFooter>
          </Card>
        ))}
      </div>
    </div>
  )
}