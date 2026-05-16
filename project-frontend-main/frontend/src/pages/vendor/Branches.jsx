import React, { useCallback, useEffect, useState } from "react";
import { useAuth } from "@/context/AuthContext";
import { api, fmtGrams, fmtINR } from "@/lib/api";
import { Card, StatCard, PageHeader, Button, Modal, EmptyState, Badge } from "@/components/ui-kit";
import AddBranchDialog from "@/components/vendor/AddBranchDialog";
import AddGoldDialog from "@/components/vendor/AddGoldDialog";
import { toast } from "sonner";
import { Building2, Plus, MapPin, Coins, Trash2, Store } from "lucide-react";

export default function VendorBranches() {
    const { user } = useAuth();
    const [branches, setBranches] = useState([]);
    const [addOpen, setAddOpen] = useState(false);
    const [addGoldOpen, setAddGoldOpen] = useState(false);
    const [deleteTarget, setDeleteTarget] = useState(null);
    const [rate, setRate] = useState(0);

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

    const totalInventory = branches.reduce((s, b) => s + b.quantity, 0);
    const totalValue = totalInventory * rate;

    const confirmDelete = async () => {
        if (!deleteTarget) return;
        try {
            await api.delete(
                `/vendors/${user.vendor_id}/branches/${deleteTarget.branch_id}`,
            );
            toast.success(`Branch #${deleteTarget.branch_id} removed`);
            setDeleteTarget(null);
            load();
        } catch (err) {
            toast.error(err.response?.data?.detail || "Delete failed");
        }
    };

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
                                <th className="text-right font-medium px-5 py-3">Actions</th>
                            </tr>
                        </thead>
                        <tbody data-testid="branches-table-body">
                            {branches.map((b) => (
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
                                    <td className="px-5 py-3 text-right">
                                        <button
                                            onClick={() => setDeleteTarget(b)}
                                            className="w-8 h-8 grid place-items-center rounded-md text-destructive hover:bg-destructive/15"
                                            data-testid={`branch-delete-${b.branch_id}`}
                                        >
                                            <Trash2 className="w-4 h-4" />
                                        </button>
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
                                <td />
                            </tr>
                        </tfoot>
                    </table>
                </Card>
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

            <Modal
                open={!!deleteTarget}
                onClose={() => setDeleteTarget(null)}
                title={`Delete branch #${deleteTarget?.branch_id}?`}
                subtitle="This action cannot be undone"
                testId="delete-branch-dialog"
                footer={
                    <>
                        <Button
                            variant="ghost"
                            onClick={() => setDeleteTarget(null)}
                            data-testid="delete-branch-cancel"
                        >
                            Cancel
                        </Button>
                        <Button
                            variant="destructive"
                            onClick={confirmDelete}
                            data-testid="delete-branch-confirm"
                        >
                            <Trash2 className="w-4 h-4" /> Delete branch
                        </Button>
                    </>
                }
            >
                {deleteTarget && (
                    <div className="text-sm space-y-3">
                        <p>
                            This will permanently remove the{" "}
                            <Badge tone="accent">
                                {deleteTarget.address.city}
                            </Badge>{" "}
                            vault and its{" "}
                            <span className="mono font-semibold neon-gold">
                                {fmtGrams(deleteTarget.quantity)}
                            </span>{" "}
                            of inventory.
                        </p>
                        <p className="text-xs text-muted-foreground">
                            If any investor holds gold from this branch, the
                            delete will be blocked by the backend.
                        </p>
                    </div>
                )}
            </Modal>
        </div>
    );
}
