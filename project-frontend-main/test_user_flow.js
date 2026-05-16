const BASE_URL = "http://localhost:8080/api";

async function printResult(step, response) {
    if (response.ok) {
        console.log(`✅ ${step} - SUCCESS`);
        return await response.json();
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
        street: "123 Main St",
        city: "Testville",
        state: "TS",
        postalCode: "12345",
        country: "Testland"
    };
    
    console.log("Registering...", email);
    let res = await fetch(`${BASE_URL}/user/auth/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(registerData)
    });
    
    let data = await printResult("Register User", res);
    if (!data) return;
    
    const token = data.token;
    const headers = {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
    };

    // 2. Get Dashboard
    res = await fetch(`${BASE_URL}/user/dashboard`, { headers });
    await printResult("Get Dashboard", res);

    // 3. Add Funds
    const fundsData = { amount: 50000.0, type: "DEPOSIT", description: "Test Deposit" };
    res = await fetch(`${BASE_URL}/user/wallet/add`, {
        method: 'POST',
        headers,
        body: JSON.stringify(fundsData)
    });
    await printResult("Add Funds", res);

    // 4. Buy Virtual Gold
    const buyVgData = { quantity: 5.0 }; // 5 grams
    res = await fetch(`${BASE_URL}/user/gold/buy`, {
        method: 'POST',
        headers,
        body: JSON.stringify(buyVgData)
    });
    await printResult("Buy Virtual Gold", res);

    // 5. Sell Virtual Gold
    const sellVgData = { quantity: 2.0 }; // Sell 2 grams
    res = await fetch(`${BASE_URL}/user/gold/sell`, {
        method: 'POST',
        headers,
        body: JSON.stringify(sellVgData)
    });
    await printResult("Sell Virtual Gold", res);

    // 6. Buy Physical Gold
    const buyPgData = { quantity: 1.0, deliveryAddress: "123 Main St" }; // Buy 1 gram physical
    res = await fetch(`${BASE_URL}/user/physical-gold/buy`, {
        method: 'POST',
        headers,
        body: JSON.stringify(buyPgData)
    });
    await printResult("Buy Physical Gold", res);

    // 7. Get Transaction History
    res = await fetch(`${BASE_URL}/user/transactions`, { headers });
    await printResult("Get Transaction History", res);

    // 8. Get Virtual Gold Holdings
    res = await fetch(`${BASE_URL}/user/gold/holdings`, { headers });
    await printResult("Get Virtual Gold Holdings", res);

    // 9. Get Physical Gold Orders
    res = await fetch(`${BASE_URL}/user/physical-gold/orders`, { headers });
    await printResult("Get Physical Gold Orders", res);

    // 10. Update Profile Address
    const profileData = {
        name: "E2E Test User Updated",
        street: "456 New St",
        city: "Newville",
        state: "NS",
        postalCode: "54321",
        country: "Newland"
    };
    res = await fetch(`${BASE_URL}/user/profile`, {
        method: 'PUT',
        headers,
        body: JSON.stringify(profileData)
    });
    await printResult("Update Profile Address", res);
}

run();
