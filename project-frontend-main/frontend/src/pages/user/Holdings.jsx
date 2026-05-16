import React, { useCallback, useEffect, useState } from "react";
import { useAuth } from "@/context/AuthContext";
import { api, fmtINR, fmtGrams } from "@/lib/api";
import { Card, PageHeader, Button, Badge, EmptyState } from "@/components/ui-kit";
import { Link } from "react-router-dom";
import { Coins, MapPin, ArrowLeftRight, Boxes } from "lucide-react";

export default function Holdings() {
    const { user } = useAuth();
    const [holdings, setHoldings] = useState([]);
    const [loading, setLoading] = useState(true);

    const load = useCallback(async () => {
        const { data } = await api.get(`/users/${user.user_id}/holdings`);
        setHoldings(data);
        setLoading(false);
    }, [user]);

    useEffect(() => {
        load();
    }, [load]);

    return (
        <div data-testid="holdings-page">
            <PageHeader
                eyebrow="Portfolio"
                title="Your Gold Holdings"
                subtitle="Vault-grade custody · 999.9 purity"
                actions={
                    <Link to="/app/trade">
                        <Button variant="primary" size="lg" data-testid="buy-more-gold-btn">
                            <Coins className="w-4 h-4" /> Buy More Gold
                        </Button>
                    </Link>
                }
            />

            {loading ? (
                <div className="text-sm text-muted-foreground">Loading…</div>
            ) : holdings.length === 0 ? (
                <EmptyState
                    icon={Boxes}
                    title="No holdings yet"
                    description="Start your gold journey by buying virtual gold from any of our trusted vendors."
                    action={
                        <Link to="/app/trade">
                            <Button variant="primary">Buy your first gram</Button>
                        </Link>
                    }
                />
            ) : (
                <div
                    className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 stagger"
                    data-testid="holdings-grid"
                >
                    {holdings.map((h) => (
                        <Card
                            key={h.holding_id}
                            data-testid={`holding-card-${h.holding_id}`}
                        >
                            <div className="flex items-start justify-between">
                                <div>
                                    <div className="text-[11px] uppercase tracking-[0.2em] text-muted-foreground">
                                        Holding #{h.holding_id}
                                    </div>
                                    <div className="text-lg font-semibold mt-1">
                                        {h.vendor_name}
                                    </div>
                                </div>
                                <Badge tone="accent">24K · Vault</Badge>
                            </div>
                            <div className="mt-5 grid grid-cols-2 gap-4">
                                <div>
                                    <div className="text-xs text-muted-foreground">
                                        Quantity
                                    </div>
                                    <div className="mono text-2xl font-bold neon-gold mt-0.5">
                                        {fmtGrams(h.quantity)}
                                    </div>
                                </div>
                                <div>
                                    <div className="text-xs text-muted-foreground">
                                        Value @ {fmtINR(h.current_gold_price)}/g
                                    </div>
                                    <div className="mono text-2xl font-bold neon-cyan mt-0.5">
                                        {fmtINR(h.value)}
                                    </div>
                                </div>
                            </div>
                            <div className="mt-5 flex items-start gap-2 text-xs text-muted-foreground border-t border-border pt-4">
                                <MapPin className="w-3.5 h-3.5 mt-0.5 text-accent shrink-0" />
                                <span>
                                    {h.branch_address.street},{" "}
                                    {h.branch_address.city},{" "}
                                    {h.branch_address.state} —{" "}
                                    {h.branch_address.postal_code}
                                </span>
                            </div>
                            <div className="mt-4 flex gap-2">
                                <Link to="/app/trade" className="flex-1">
                                    <Button
                                        variant="outline"
                                        size="sm"
                                        className="w-full"
                                        data-testid={`sell-holding-${h.holding_id}`}
                                    >
                                        <ArrowLeftRight className="w-3.5 h-3.5" />{" "}
                                        Sell
                                    </Button>
                                </Link>
                                <Link to="/app/physical" className="flex-1">
                                    <Button
                                        variant="accent"
                                        size="sm"
                                        className="w-full"
                                        data-testid={`convert-holding-${h.holding_id}`}
                                    >
                                        Convert
                                    </Button>
                                </Link>
                            </div>
                        </Card>
                    ))}
                </div>
            )}
        </div>
    );
}
