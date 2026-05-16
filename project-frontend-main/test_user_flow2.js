const BASE_URL = "http://localhost:8080/api";

async function printResult(step, response) {
    if (response.ok) {
        console.log(`✅ ${step} - SUCCESS`);
        return await response.json().catch(() => ({}));
    } else {
        console.log(`❌ ${step} - FAILED (${response.status})`);
        const text = await response.text();
        console.log(text);
        return null;
    }
}

async function run() {
    // 1. Register
    const email = `testuser_${Math.floor(Math.random() * 9000) + 1000}@example.com`;
    const registerData = {
        name: "E2E Test User",
        email: email,
        password: "password123",
        street: "Street",
        city: "City",
        state: "State",
        postalCode: "12345",
        country: "Testland"
    };
    
    let res = await fetch(`${BASE_URL}/user/auth/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(registerData)
    });
    
    let data = await printResult("Register User", res);
    if (!data) return;
    
    const token = data.token;
    const userId = data.user_id;
    const headers = {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
    };

    // 2. Add Funds
    const fundsData = { user_id: userId, amount: 500000.0, payment_method: "UPI" };
    res = await fetch(`${BASE_URL}/wallet/topup`, {
        method: 'POST',
        headers,
        body: JSON.stringify(fundsData)
    });
    await printResult("Add Funds", res);

    // 4. Buy Virtual Gold from Vendor 2
    const buyVgData = { user_id: userId, vendor_id: 2, quantity: 5.0 }; // 5 grams
    res = await fetch(`${BASE_URL}/virtual-gold/buy`, {
        method: 'POST',
        headers,
        body: JSON.stringify(buyVgData)
    });
    let holding = await printResult("Buy Virtual Gold", res); console.log(holding);

    // 5. Sell Virtual Gold
    if (holding && holding.holding_id) {
        const sellVgData = { user_id: userId, holding_id: holding.holding_id, quantity: 1.0 }; // Sell 1 gram
        res = await fetch(`${BASE_URL}/virtual-gold/sell`, {
            method: 'POST',
            headers,
            body: JSON.stringify(sellVgData)
        });
        await printResult("Sell Virtual Gold", res);

        // 6. Convert to physical
        const convertData = { user_id: userId, holding_id: holding.holding_id, delivery_address_id: 1, quantity: 1.0 }; // Convert 1 gram
        res = await fetch(`${BASE_URL}/physical-gold/convert`, {
            method: 'POST',
            headers,
            body: JSON.stringify(convertData)
        });
        await printResult("Convert Virtual Gold to Physical", res);
    }

    // 7. Get Payments
    res = await fetch(`${BASE_URL}/users/${userId}/payments`, { headers });
    const payments = await printResult("Get Payments", res);
    console.log("Payments length:", payments ? payments.length : 0);

    // 8. Get Transactions
    res = await fetch(`${BASE_URL}/users/${userId}/transactions`, { headers });
    const transactions = await printResult("Get Transactions", res);
    console.log("Transactions length:", transactions ? transactions.length : 0);
}

run();
