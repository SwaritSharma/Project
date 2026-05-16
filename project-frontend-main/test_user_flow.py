import requests
import json
import time
import random

BASE_URL = "http://localhost:8080/api"

def print_result(step, response):
    if response.status_code in [200, 201]:
        print(f"✅ {step} - SUCCESS")
    else:
        print(f"❌ {step} - FAILED ({response.status_code})")
        print(response.text)

# 1. Register
email = f"testuser_{random.randint(1000, 9999)}@example.com"
register_data = {
    "name": "E2E Test User",
    "email": email,
    "password": "password123",
    "street": "123 Main St",
    "city": "Testville",
    "state": "TS",
    "postalCode": "12345",
    "country": "Testland"
}
res = requests.post(f"{BASE_URL}/user/auth/register", json=register_data)
print_result("Register User", res)
token = res.json().get("token")
headers = {"Authorization": f"Bearer {token}"}

# 2. Get Dashboard
res = requests.get(f"{BASE_URL}/user/dashboard", headers=headers)
print_result("Get Dashboard", res)

# 3. Add Funds
funds_data = {"amount": 50000.0, "type": "DEPOSIT", "description": "Test Deposit"}
res = requests.post(f"{BASE_URL}/user/wallet/add", json=funds_data, headers=headers)
print_result("Add Funds", res)

# 4. Buy Virtual Gold
buy_vg_data = {"quantity": 5.0} # 5 grams
res = requests.post(f"{BASE_URL}/user/gold/buy", json=buy_vg_data, headers=headers)
print_result("Buy Virtual Gold", res)

# 5. Sell Virtual Gold
sell_vg_data = {"quantity": 2.0} # Sell 2 grams
res = requests.post(f"{BASE_URL}/user/gold/sell", json=sell_vg_data, headers=headers)
print_result("Sell Virtual Gold", res)

# 6. Buy Physical Gold
buy_pg_data = {"quantity": 1.0, "deliveryAddress": "123 Main St"} # Buy 1 gram physical
res = requests.post(f"{BASE_URL}/user/physical-gold/buy", json=buy_pg_data, headers=headers)
print_result("Buy Physical Gold", res)

# 7. Get Transaction History
res = requests.get(f"{BASE_URL}/user/transactions", headers=headers)
print_result("Get Transaction History", res)

# 8. Get Virtual Gold Holdings
res = requests.get(f"{BASE_URL}/user/gold/holdings", headers=headers)
print_result("Get Virtual Gold Holdings", res)

# 9. Get Physical Gold Orders
res = requests.get(f"{BASE_URL}/user/physical-gold/orders", headers=headers)
print_result("Get Physical Gold Orders", res)

# 10. Update Profile Address
profile_data = {
    "name": "E2E Test User Updated",
    "street": "456 New St",
    "city": "Newville",
    "state": "NS",
    "postalCode": "54321",
    "country": "Newland"
}
res = requests.put(f"{BASE_URL}/user/profile", json=profile_data, headers=headers)
print_result("Update Profile Address", res)

