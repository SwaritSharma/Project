import React, { useCallback, useEffect, useState, useMemo } from "react";
import { useAuth } from "@/context/AuthContext";
import { api, fmtGrams, fmtINR } from "@/lib/api";
import { Card, StatCard, PageHeader, Button, Modal, EmptyState, Badge, Input } from "@/components/ui-kit";
import AddBranchDialog from "@/components/vendor/AddBranchDialog";
import AddGoldDialog from "@/components/vendor/AddGoldDialog";
import { toast } from "sonner";
import { Building2, Plus, MapPin, Coins, Trash2, Store } from "lucide-react";

export default function VendorBranches() {
    const { user } = useAuth();
    const [branches, setBranches] = useState([]);
    const [addOpen, setAddOpen] = useState(false);
    const [addGoldOpen, setAddGoldOpen] = useState(false);
    const [rate, setRate] = useState(0);



    const [q, setQ] = useState("");
    const [sortOrder, setSortOrder] = useState("newest");
    const [page, setPage] = useState(1);
    const PAGE_SIZE = 10;

    const load = useCallback(async () => {
        const [b, d] = await Promise.all([
            api.get(`/vendors/${user.vendor_id}/branches`),
            api.get(`/vendors/${user.vendor_id}/dashboard`),
        ]);
        setBranches(b.data);
        setRate(d.data.current_gold_price);
    }, [user]);

    useEffect(() => {
        load();
    }, [load]);

    const filteredBranches = useMemo(() => {
        let res = [...branches];
        if (q) {
            const term = q.toLowerCase();
            res = res.filter(
                (b) =>
                    b.address.city.toLowerCase().includes(term) ||
                    b.address.state.toLowerCase().includes(term) ||
                    b.address.street.toLowerCase().includes(term) ||
                    String(b.branch_id).includes(term)
            );
        }
        
        if (sortOrder === "inv-high") {
            res.sort((a, b) => b.quantity - a.quantity);
        } else if (sortOrder === "inv-low") {
            res.sort((a, b) => a.quantity - b.quantity);
        } else {
            res.sort((a, b) => b.branch_id - a.branch_id);
        }
        return res;
    }, [branches, q, sortOrder]);

    useEffect(() => {
        setPage(1);
    }, [q, sortOrder]);

    const paginatedBranches = useMemo(() => {
        const start = (page - 1) * PAGE_SIZE;
        return filteredBranches.slice(start, start + PAGE_SIZE);
    }, [filteredBranches, page]);

    const totalPages = Math.ceil(filteredBranches.length / PAGE_SIZE);

    const totalInventory = branches.reduce((s, b) => s + b.quantity, 0);
    const totalValue = totalInventory * rate;

    return (
        <div data-testid="vendor-branches-page">
            <PageHeader
                eyebrow="Network"
                title="Branch Locations"
                subtitle="Manage vault inventory across cities"
                actions={
                    <div className="flex gap-2">
                        <Button
                            variant="secondary"
                            size="lg"
                            onClick={() => setAddGoldOpen(true)}
                            data-testid="add-gold-btn"
                        >
                            <Coins className="w-4 h-4" /> Add Gold
                        </Button>
                        <Button
                            variant="accent"
                            size="lg"
                            onClick={() => setAddOpen(true)}
                            data-testid="add-branch-btn"
                        >
                            <Plus className="w-4 h-4" /> Add Branch
                        </Button>
                    </div>
                }
            />

            <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-6 stagger">
                <StatCard
                    label="Total Branches"
                    value={String(branches.length)}
                    icon={Building2}
                    accent="primary"
                    hint="Active vault locations"
                    testId="branches-stat-count"
                />
                <StatCard
                    label="Combined Inventory"
                    value={fmtGrams(totalInventory)}
                    icon={Coins}
                    accent="accent"
                    hint="Across all branches"
                    testId="branches-stat-inventory"
                />
                <StatCard
                    label="Inventory Value"
                    value={fmtINR(totalValue)}
                    icon={Store}
                    accent="primary"
                    hint={`@ ${fmtINR(rate)}/g`}
                    testId="branches-stat-value"
                />
            </div>

            {branches.length > 0 && (
                <div className="flex flex-col sm:flex-row gap-4 mb-6">
                    <div className="relative flex-1">
                        <Input
                            placeholder="Search by city, state, address, or ID…"
                            value={q}
                            onChange={(e) => setQ(e.target.value)}
                            className="w-full sm:max-w-xs"
                        />
                    </div>
                    <select
                        className="flex h-10 w-full sm:w-auto items-center justify-between rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
                        value={sortOrder}
                        onChange={(e) => setSortOrder(e.target.value)}
                    >
                        <option value="newest">Newest First</option>
                        <option value="inv-high">Inventory: High to Low</option>
                        <option value="inv-low">Inventory: Low to High</option>
                    </select>
                </div>
            )}

            {branches.length === 0 ? (
                <EmptyState
                    icon={Building2}
                    title="No branches yet"
                    description="Add your first vault location to start fulfilling orders."
                    action={
                        <Button
                            variant="accent"
                            onClick={() => setAddOpen(true)}
                            data-testid="empty-add-branch-btn"
                        >
                            <Plus className="w-4 h-4" /> Add your first branch
                        </Button>
                    }
                />
            ) : (
                <Card className="p-0 overflow-hidden" data-testid="branches-table-wrap">
                    <table className="w-full text-sm">
                        <thead className="bg-secondary/30 text-[11px] uppercase tracking-[0.15em] text-muted-foreground">
                            <tr>
                                <th className="text-left font-medium px-5 py-3">Branch</th>
                                <th className="text-left font-medium px-5 py-3">City</th>
                                <th className="text-left font-medium px-5 py-3">Address</th>
                                <th className="text-right font-medium px-5 py-3">Inventory</th>
                                <th className="text-right font-medium px-5 py-3">Value</th>
                            </tr>
                        </thead>
                        <tbody data-testid="branches-table-body">
                            {paginatedBranches.map((b) => (
                                <tr
                                    key={b.branch_id}
                                    className="border-t border-border hover:bg-secondary/30"
                                    data-testid={`branch-row-${b.branch_id}`}
                                >
                                    <td className="px-5 py-3">
                                        <div className="flex items-center gap-2.5">
                                            <div className="w-8 h-8 grid place-items-center rounded-md bg-accent/10 text-accent ring-1 ring-accent/30">
                                                <Building2 className="w-4 h-4" />
                                            </div>
                                            <div>
                                                <div className="mono text-xs text-muted-foreground">
                                                    #{b.branch_id}
                                                </div>
                                                <div className="font-medium">Branch</div>
                                            </div>
                                        </div>
                                    </td>
                                    <td className="px-5 py-3">
                                        <div className="font-medium">{b.address.city}</div>
                                        <div className="text-xs text-muted-foreground">
                                            {b.address.state}, {b.address.country}
                                        </div>
                                    </td>
                                    <td className="px-5 py-3 text-muted-foreground text-xs max-w-[280px]">
                                        <div className="flex items-start gap-1.5">
                                            <MapPin className="w-3 h-3 text-accent mt-0.5 shrink-0" />
                                            <span>
                                                {b.address.street} — {b.address.postal_code}
                                            </span>
                                        </div>
                                    </td>
                                    <td className="px-5 py-3 text-right mono font-semibold neon-gold">
                                        {fmtGrams(b.quantity)}
                                    </td>
                                    <td className="px-5 py-3 text-right mono">
                                        {fmtINR(b.quantity * rate)}
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                        <tfoot className="bg-secondary/40 border-t-2 border-border">
                            <tr>
                                <td colSpan={3} className="px-5 py-3 text-[11px] uppercase tracking-[0.15em] text-muted-foreground">
                                    Total
                                </td>
                                <td className="px-5 py-3 text-right mono font-bold neon-gold">
                                    {fmtGrams(totalInventory)}
                                </td>
                                <td className="px-5 py-3 text-right mono font-bold">
                                    {fmtINR(totalValue)}
                                </td>
                            </tr>
                        </tfoot>
                    </table>
                </Card>
            )}

            {totalPages > 1 && (
                <div className="flex items-center justify-between mt-6">
                    <div className="text-sm text-muted-foreground">
                        Showing page {page} of {totalPages}
                    </div>
                    <div className="flex gap-2">
                        <button
                            disabled={page === 1}
                            onClick={() => setPage(p => p - 1)}
                            className="px-3 py-1.5 rounded-md text-sm border border-border bg-secondary/50 disabled:opacity-50 disabled:cursor-not-allowed hover:bg-secondary transition"
                        >
                            Previous
                        </button>
                        <button
                            disabled={page === totalPages}
                            onClick={() => setPage(p => p + 1)}
                            className="px-3 py-1.5 rounded-md text-sm border border-border bg-secondary/50 disabled:opacity-50 disabled:cursor-not-allowed hover:bg-secondary transition"
                        >
                            Next
                        </button>
                    </div>
                </div>
            )}

            <AddBranchDialog
                open={addOpen}
                onOpenChange={setAddOpen}
                vendorId={user.vendor_id}
                onDone={load}
            />

            <AddGoldDialog
                open={addGoldOpen}
                onOpenChange={setAddGoldOpen}
                vendorId={user.vendor_id}
                onDone={load}
            />
        </div>
    );
}
