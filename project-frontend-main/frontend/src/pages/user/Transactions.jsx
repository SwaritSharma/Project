import React, { useCallback, useEffect, useMemo, useState } from "react";
import { useAuth } from "@/context/AuthContext";
import { api, fmtINR, fmtGrams, fmtDateTime } from "@/lib/api";
import { Card, PageHeader, Input, Badge, EmptyState } from "@/components/ui-kit";
import { Search, Receipt, CreditCard } from "lucide-react";
import { cn } from "@/lib/utils";

export default function Transactions() {
    const { user } = useAuth();
    const [txns, setTxns] = useState([]);
    const [payments, setPayments] = useState([]);
    const [q, setQ] = useState("");
    const [tab, setTab] = useState("txns");
    const [page, setPage] = useState(1);
    const PAGE_SIZE = 10;

    const load = useCallback(async () => {
        const [t, p] = await Promise.all([
            api.get(`/users/${user.user_id}/transactions`),
            api.get(`/users/${user.user_id}/payments`),
        ]);
        setTxns(t.data);
        setPayments(p.data);
    }, [user]);

    useEffect(() => {
        load();
    }, [load]);

    const filteredTxns = useMemo(() => {
        const term = q.toLowerCase();
        if (!term) return txns;
        return txns.filter(
            (t) =>
                t.vendor_name.toLowerCase().includes(term) ||
                t.transaction_type.toLowerCase().includes(term) ||
                t.transaction_status.toLowerCase().includes(term),
        );
    }, [txns, q]);

    useEffect(() => {
        setPage(1);
    }, [tab, q]);

    const paginatedTxns = useMemo(() => {
        const start = (page - 1) * PAGE_SIZE;
        return filteredTxns.slice(start, start + PAGE_SIZE);
    }, [filteredTxns, page]);

    const paginatedPayments = useMemo(() => {
        const start = (page - 1) * PAGE_SIZE;
        return payments.slice(start, start + PAGE_SIZE);
    }, [payments, page]);

    const totalPages = tab === "txns" 
        ? Math.ceil(filteredTxns.length / PAGE_SIZE) 
        : Math.ceil(payments.length / PAGE_SIZE);

    return (
        <div data-testid="transactions-page">
            <PageHeader
                eyebrow="Ledger"
                title="Transactions & Payments"
                subtitle="Immutable audit log · Sorted by latest"
                actions={
                    <div className="relative">
                        <Search className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-muted-foreground" />
                        <Input
                            placeholder="Search vendor, type, status…"
                            value={q}
                            onChange={(e) => setQ(e.target.value)}
                            className="pl-9 w-72"
                            data-testid="transactions-search"
                        />
                    </div>
                }
            />

            <div className="inline-flex p-1 rounded-lg border border-border bg-background/40 mb-6">
                <TabBtn
                    active={tab === "txns"}
                    onClick={() => setTab("txns")}
                    icon={Receipt}
                    label={`Gold Transactions (${txns.length})`}
                    testId="txns-tab"
                />
                <TabBtn
                    active={tab === "pay"}
                    onClick={() => setTab("pay")}
                    icon={CreditCard}
                    label={`Wallet Payments (${payments.length})`}
                    testId="payments-tab"
                />
            </div>

            {tab === "txns" ? (
                filteredTxns.length === 0 ? (
                    <EmptyState
                        icon={Receipt}
                        title="No transactions found"
                        description="Adjust your search or buy some gold to get started."
                    />
                ) : (
                    <Card className="p-0 overflow-hidden">
                        <table className="w-full text-sm" data-testid="all-transactions-table">
                            <thead className="bg-secondary/40 text-[11px] uppercase tracking-[0.15em] text-muted-foreground">
                                <tr>
                                    <th className="text-left font-medium px-5 py-3">ID</th>
                                    <th className="text-left font-medium px-5 py-3">Type</th>
                                    <th className="text-left font-medium px-5 py-3">Vendor</th>
                                    <th className="text-right font-medium px-5 py-3">Qty</th>
                                    <th className="text-right font-medium px-5 py-3">Amount</th>
                                    <th className="text-center font-medium px-5 py-3">Status</th>
                                    <th className="text-right font-medium px-5 py-3">When</th>
                                </tr>
                            </thead>
                            <tbody>
                                {paginatedTxns.map((t) => (
                                    <tr key={t.transaction_id} className="border-t border-border hover:bg-secondary/30">
                                        <td className="px-5 py-3 mono text-xs text-muted-foreground">
                                            #{t.transaction_id}
                                        </td>
                                        <td className="px-5 py-3">
                                            <Badge tone={typeTone(t.transaction_type)}>
                                                {t.transaction_type}
                                            </Badge>
                                        </td>
                                        <td className="px-5 py-3">
                                            <div className="font-medium">{t.vendor_name}</div>
                                            <div className="text-xs text-muted-foreground">
                                                {t.branch_address ? `${t.branch_address.city}, ${t.branch_address.state}` : "Online"}
                                            </div>
                                        </td>
                                        <td className="px-5 py-3 text-right mono">{fmtGrams(t.quantity)}</td>
                                        <td className="px-5 py-3 text-right mono">{fmtINR(t.amount)}</td>
                                        <td className="px-5 py-3 text-center">
                                            <Badge tone={t.transaction_status === "Success" ? "success" : "destructive"}>
                                                {t.transaction_status}
                                            </Badge>
                                        </td>
                                        <td className="px-5 py-3 text-right text-xs text-muted-foreground mono">
                                            {fmtDateTime(t.created_at)}
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </Card>
                )
            ) : payments.length === 0 ? (
                <EmptyState
                    icon={CreditCard}
                    title="No payments yet"
                    description="Wallet topup history will appear here."
                />
            ) : (
                <Card className="p-0 overflow-hidden">
                    <table className="w-full text-sm" data-testid="all-payments-table">
                        <thead className="bg-secondary/40 text-[11px] uppercase tracking-[0.15em] text-muted-foreground">
                            <tr>
                                <th className="text-left font-medium px-5 py-3">ID</th>
                                <th className="text-left font-medium px-5 py-3">Direction</th>
                                <th className="text-left font-medium px-5 py-3">Method</th>
                                <th className="text-right font-medium px-5 py-3">Amount</th>
                                <th className="text-center font-medium px-5 py-3">Status</th>
                                <th className="text-right font-medium px-5 py-3">When</th>
                            </tr>
                        </thead>
                        <tbody>
                            {paginatedPayments.map((p) => {
                                const credit = p.transaction_type === "Credited to wallet";
                                return (
                                    <tr key={p.payment_id} className="border-t border-border hover:bg-secondary/30">
                                        <td className="px-5 py-3 mono text-xs text-muted-foreground">
                                            #{p.payment_id}
                                        </td>
                                        <td className="px-5 py-3">
                                            <Badge tone={credit ? "success" : "destructive"}>
                                                {credit ? "↓ Credit" : "↑ Debit"}
                                            </Badge>
                                        </td>
                                        <td className="px-5 py-3">{p.payment_method}</td>
                                        <td className="px-5 py-3 text-right mono">
                                            <span className={credit ? "text-emerald-400" : "text-foreground"}>
                                                {credit ? "+" : "−"}
                                                {fmtINR(p.amount)}
                                            </span>
                                        </td>
                                        <td className="px-5 py-3 text-center">
                                            <Badge tone={p.payment_status === "Success" ? "success" : "destructive"}>
                                                {p.payment_status}
                                            </Badge>
                                        </td>
                                        <td className="px-5 py-3 text-right text-xs text-muted-foreground mono">
                                            {fmtDateTime(p.created_at)}
                                        </td>
                                    </tr>
                                );
                            })}
                        </tbody>
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
        </div>
    );
}

function TabBtn({ active, onClick, icon: Icon, label, testId }) {
    return (
        <button
            onClick={onClick}
            data-testid={testId}
            className={cn(
                "px-4 py-2 rounded-md text-sm font-medium transition inline-flex items-center gap-2",
                active
                    ? "bg-primary/15 text-primary ring-1 ring-primary/30"
                    : "text-muted-foreground hover:text-foreground",
            )}
        >
            <Icon className="w-3.5 h-3.5" /> {label}
        </button>
    );
}

function typeTone(t) {
    if (t === "Buy") return "success";
    if (t === "Sell") return "destructive";
    return "accent";
}
