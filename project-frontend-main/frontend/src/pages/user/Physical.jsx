import React, { useCallback, useEffect, useState } from "react";
import { useAuth } from "@/context/AuthContext";
import { api, fmtINR, fmtINR2, fmtGrams, fmtDateTime } from "@/lib/api";
import { Card, PageHeader, Button, Field, Input, Select, Badge } from "@/components/ui-kit";
import { toast } from "sonner";
import { cn } from "@/lib/utils";
import { Truck, ShieldCheck, MapPin, Boxes, Package } from "lucide-react";

export default function Physical() {
    const { user } = useAuth();
    const [tab, setTab] = useState("buy");
    const [vendors, setVendors] = useState([]);
    const [holdings, setHoldings] = useState([]);
    const [addresses, setAddresses] = useState([]);
    const [deliveries, setDeliveries] = useState([]);
    const [balance, setBalance] = useState(0);

    const load = useCallback(async () => {
        const [v, h, a, d, dash] = await Promise.all([
            api.get("/vendors"),
            api.get(`/users/${user.user_id}/holdings`),
            api.get(`/users/${user.user_id}/addresses`),
            api.get(`/users/${user.user_id}/physical-gold`),
            api.get(`/users/${user.user_id}/dashboard`),
        ]);
        setVendors(v.data);
        setHoldings(h.data);
        setAddresses(a.data);
        setDeliveries(d.data);
        setBalance(dash.data.balance || 0);
    }, [user]);

    useEffect(() => {
        load();
    }, [load]);

    return (
        <div data-testid="physical-page">
            <PageHeader
                eyebrow="Vault Operations"
                title="Physical Gold"
                subtitle="999.9 Purity coins · Insured doorstep delivery"
            />

            <div className="inline-flex p-1 rounded-lg border border-border bg-background/40 mb-6">
                <TabBtn
                    active={tab === "buy"}
                    onClick={() => setTab("buy")}
                    label="Buy Physical"
                    testId="physical-buy-tab"
                />
                <TabBtn
                    active={tab === "convert"}
                    onClick={() => setTab("convert")}
                    label="Convert Virtual → Physical"
                    testId="physical-convert-tab"
                />
                <TabBtn
                    active={tab === "deliveries"}
                    onClick={() => setTab("deliveries")}
                    label={`Deliveries (${deliveries.length})`}
                    testId="physical-deliveries-tab"
                />
            </div>

            {tab === "buy" && (
                <BuyPhysical
                    vendors={vendors}
                    addresses={addresses}
                    userId={user.user_id}
                    balance={balance}
                    onDone={load}
                />
            )}
            {tab === "convert" && (
                <ConvertPhysical
                    holdings={holdings}
                    addresses={addresses}
                    userId={user.user_id}
                    onDone={load}
                />
            )}
            {tab === "deliveries" && (
                <DeliveryList deliveries={deliveries} />
            )}
        </div>
    );
}

function TabBtn({ active, onClick, label, testId }) {
    return (
        <button
            onClick={onClick}
            data-testid={testId}
            className={cn(
                "px-4 py-2 rounded-md text-sm font-medium transition",
                active
                    ? "bg-primary/15 text-primary ring-1 ring-primary/30"
                    : "text-muted-foreground hover:text-foreground",
            )}
        >
            {label}
        </button>
    );
}

function BuyPhysical({ vendors, addresses, userId, balance, onDone }) {
    const [vendorId, setVendorId] = useState("");
    const [qty, setQty] = useState("");
    const [addressId, setAddressId] = useState("");
    const [busy, setBusy] = useState(false);

    const vendor = vendors.find((v) => String(v.vendor_id) === vendorId);
    const total = vendor && qty ? vendor.current_gold_price * parseFloat(qty) : 0;
    const isInsufficient = total > balance;

    const submit = async (e) => {
        e.preventDefault();
        if (!vendorId || !addressId || !qty)
            return toast.error("Fill all fields");
        if (isInsufficient)
            return toast.error("Insufficient wallet balance");
        try {
            setBusy(true);
            await api.post("/physical-gold/buy", {
                user_id: userId,
                vendor_id: parseInt(vendorId),
                delivery_address_id: parseInt(addressId),
                quantity: parseFloat(qty),
            });
            toast.success("Physical gold order placed!");
            setQty("");
            onDone();
        } catch (err) {
            toast.error(err.response?.data?.detail || "Order failed");
        } finally {
            setBusy(false);
        }
    };

    return (
        <form onSubmit={submit} className="grid grid-cols-1 gap-4 lg:grid-cols-3" data-testid="buy-physical-form">
            <Card className="lg:col-span-2 space-y-5">
                <Field label="Vendor">
                    <Select
                        value={vendorId}
                        onChange={(e) => setVendorId(e.target.value)}
                        data-testid="buy-physical-vendor"
                    >
                        <option value="">Select a refiner…</option>
                        {vendors.map((v) => (
                            <option key={v.vendor_id} value={v.vendor_id}>
                                {`${v.vendor_name} · ${fmtINR2(v.current_gold_price)}/g`}
                            </option>
                        ))}
                    </Select>
                </Field>
                <Field label="Quantity (grams)" hint="Free insured delivery on orders ≥ 10g">
                    <Input
                        type="number"
                        step="0.1"
                        min="0.1"
                        value={qty}
                        onChange={(e) => setQty(e.target.value)}
                        className="mono text-lg"
                        data-testid="buy-physical-qty"
                    />
                    <div className="mt-2 flex gap-2 flex-wrap">
                        {[1, 5, 10, 20, 50].map((g) => (
                            <button
                                key={g}
                                type="button"
                                onClick={() => setQty(String(g))}
                                className="mono rounded-md border border-border px-3 py-1 text-xs hover:border-primary hover:text-primary"
                            >
                                {g}g
                            </button>
                        ))}
                    </div>
                </Field>
                <Field label="Delivery address">
                    <Select
                        value={addressId}
                        onChange={(e) => setAddressId(e.target.value)}
                        data-testid="buy-physical-address"
                    >
                        <option value="">Select address…</option>
                        {addresses.map((a) => (
                            <option key={a.address_id} value={a.address_id}>
                                {`${a.street}, ${a.city}`}
                            </option>
                        ))}
                    </Select>
                </Field>
            </Card>

            <Card className="self-start lg:sticky lg:top-6" data-testid="buy-physical-summary">
                <div className="text-[11px] uppercase tracking-[0.2em] text-muted-foreground">
                    Order Summary
                </div>
                <div className="mt-4 space-y-3 text-sm">
                    <Row label="Vendor" value={vendor?.vendor_name || "—"} />
                    <Row label="Quantity" value={qty ? fmtGrams(qty) : "—"} mono />
                    <Row
                        label="Rate"
                        value={vendor ? `${fmtINR2(vendor.current_gold_price)}/g` : "—"}
                        mono
                    />
                    <div className="border-t border-border pt-3 flex items-baseline justify-between">
                        <span className="text-muted-foreground">Total</span>
                        <span className="mono text-2xl font-bold neon-gold">
                            {fmtINR(total)}
                        </span>
                    </div>
                </div>
                <div className="mt-5 grid gap-1.5 text-xs text-muted-foreground">
                    <div className="flex items-center gap-1.5">
                        <Truck className="w-3.5 h-3.5 text-accent" /> Free insured delivery
                    </div>
                    <div className="flex items-center gap-1.5">
                        <ShieldCheck className="w-3.5 h-3.5 text-accent" /> 999.9 purity certificate
                    </div>
                </div>
                <Button
                    type="submit"
                    variant="accent"
                    size="lg"
                    disabled={busy || !vendor || !qty || !addressId || isInsufficient}
                    className={cn(
                        "w-full mt-5",
                        isInsufficient && "opacity-50 cursor-not-allowed"
                    )}
                    data-testid="buy-physical-submit"
                >
                    {busy ? "Processing…" : "Place Order"}
                </Button>
                {isInsufficient && (
                    <div className="text-destructive text-xs text-center mt-2 font-medium">
                        Insufficient Wallet Balance (Available: {fmtINR(balance)})
                    </div>
                )}
            </Card>
        </form>
    );
}

function ConvertPhysical({ holdings, addresses, userId, onDone }) {
    const [holdingId, setHoldingId] = useState("");
    const [qty, setQty] = useState("");
    const [addressId, setAddressId] = useState("");
    const [busy, setBusy] = useState(false);

    const holding = holdings.find((h) => String(h.holding_id) === holdingId);
    const tooMuch = holding && qty && parseFloat(qty) > holding.quantity;

    const submit = async (e) => {
        e.preventDefault();
        if (!holdingId || !addressId || !qty)
            return toast.error("Fill all fields");
        if (tooMuch) return toast.error("Quantity exceeds holding");
        try {
            setBusy(true);
            await api.post("/physical-gold/convert", {
                user_id: userId,
                holding_id: parseInt(holdingId),
                delivery_address_id: parseInt(addressId),
                quantity: parseFloat(qty),
            });
            toast.success("Conversion successful! Delivery scheduled.");
            setQty("");
            onDone();
        } catch (err) {
            toast.error(err.response?.data?.detail || "Convert failed");
        } finally {
            setBusy(false);
        }
    };

    return (
        <form onSubmit={submit} className="grid grid-cols-1 gap-4 lg:grid-cols-3" data-testid="convert-physical-form">
            <Card className="lg:col-span-2 space-y-5">
                <Field label="Holding to convert">
                    <Select
                        value={holdingId}
                        onChange={(e) => setHoldingId(e.target.value)}
                        data-testid="convert-holding-select"
                    >
                        <option value="">Select holding…</option>
                        {holdings.map((h) => (
                            <option key={h.holding_id} value={h.holding_id}>
                                {`#${h.holding_id} · ${h.vendor_name} · ${fmtGrams(h.quantity)}`}
                            </option>
                        ))}
                    </Select>
                </Field>
                <Field label="Quantity (grams)">
                    <Input
                        type="number"
                        step="0.1"
                        min="0.1"
                        value={qty}
                        onChange={(e) => setQty(e.target.value)}
                        disabled={!holding}
                        className="mono text-lg"
                        data-testid="convert-qty-input"
                    />
                    {tooMuch && (
                        <div className="text-xs text-destructive mt-1">
                            Max convertible: {fmtGrams(holding.quantity)}
                        </div>
                    )}
                </Field>
                <Field label="Delivery address">
                    <Select
                        value={addressId}
                        onChange={(e) => setAddressId(e.target.value)}
                        data-testid="convert-address-select"
                    >
                        <option value="">Select address…</option>
                        {addresses.map((a) => (
                            <option key={a.address_id} value={a.address_id}>
                                {`${a.street}, ${a.city}`}
                            </option>
                        ))}
                    </Select>
                </Field>
            </Card>

            <Card className="self-start lg:sticky lg:top-6" data-testid="convert-summary">
                <div className="text-[11px] uppercase tracking-[0.2em] text-muted-foreground">
                    Conversion Summary
                </div>
                <div className="mt-4 space-y-3 text-sm">
                    <Row
                        label="From holding"
                        value={holding ? `#${holding.holding_id}` : "—"}
                    />
                    <Row
                        label="Quantity"
                        value={qty ? fmtGrams(qty) : "—"}
                        mono
                    />
                    <Row
                        label="Remaining"
                        value={
                            holding && qty
                                ? fmtGrams(
                                      Math.max(
                                          holding.quantity - parseFloat(qty || 0),
                                          0,
                                      ),
                                  )
                                : "—"
                        }
                        mono
                    />
                </div>
                <Button
                    type="submit"
                    variant="accent"
                    size="lg"
                    disabled={busy || !holding || !qty || tooMuch || !addressId}
                    className="w-full mt-5"
                    data-testid="convert-submit-button"
                >
                    {busy ? "Processing…" : "Convert & Ship"}
                </Button>
            </Card>
        </form>
    );
}

function DeliveryList({ deliveries }) {
    if (deliveries.length === 0) {
        return (
            <Card className="text-center py-10" data-testid="no-deliveries">
                <Package className="w-10 h-10 mx-auto text-muted-foreground mb-3" />
                <div className="text-sm text-muted-foreground">
                    No physical gold orders yet.
                </div>
            </Card>
        );
    }
    return (
        <div
            className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 stagger"
            data-testid="deliveries-grid"
        >
            {deliveries.map((d) => (
                <Card key={d.transaction_id}>
                    <div className="flex items-start justify-between">
                        <div>
                            <div className="text-[11px] uppercase tracking-[0.2em] text-muted-foreground">
                                Order #{d.transaction_id}
                            </div>
                            <div className="mono text-xl font-bold neon-gold mt-1">
                                {fmtGrams(d.quantity)}
                            </div>
                            <div className="text-xs text-muted-foreground">
                                {d.vendor_name}
                            </div>
                        </div>
                        <Badge tone="success">In Transit</Badge>
                    </div>
                    <div className="mt-4 border-t border-border pt-3 text-xs text-muted-foreground flex gap-2">
                        <MapPin className="w-3.5 h-3.5 mt-0.5 text-accent shrink-0" />
                        <span>
                            {d.delivery_address.street},{" "}
                            {d.delivery_address.city},{" "}
                            {d.delivery_address.state} —{" "}
                            {d.delivery_address.postal_code}
                        </span>
                    </div>
                    <div className="mt-2 text-xs text-muted-foreground mono">
                        Placed {fmtDateTime(d.created_at)}
                    </div>
                </Card>
            ))}
        </div>
    );
}

function Row({ label, value, mono }) {
    return (
        <div className="flex items-center justify-between">
            <span className="text-muted-foreground">{label}</span>
            <span className={mono ? "mono" : ""}>{value}</span>
        </div>
    );
}
