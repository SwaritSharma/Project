#!/bin/bash
set -e

echo "Registering vendor..."
REG_RES=$(curl -s -X POST http://localhost:8081/api/vendor/auth/register \
     -H "Content-Type: application/json" \
     -d '{"vendorName": "Test Vendor", "contactPhone": "1234512345", "password": "password"}')
echo $REG_RES

echo "Logging in..."
LOG_RES=$(curl -s -X POST http://localhost:8081/api/vendor/auth/login \
     -H "Content-Type: application/json" \
     -d '{"contactPhone": "1234512345", "password": "password"}')
echo $LOG_RES

TOKEN=$(echo $LOG_RES | grep -o '"token":"[^"]*' | cut -d'"' -f4)
VENDOR_ID=$(echo $LOG_RES | grep -o '"vendor_id":[^,]*' | cut -d':' -f2 | tr -d '}')

echo "Token: $TOKEN"
echo "Vendor ID: $VENDOR_ID"

if [ -z "$TOKEN" ]; then
    echo "Login failed."
    exit 1
fi

echo "Creating branch..."
curl -s -X POST http://localhost:8081/api/vendors/$VENDOR_ID/branches \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json" \
     -d '{"address": {"street": "123", "city": "City", "state": "State", "postalCode": "12345"}}'

echo "Getting branches..."
BRANCHES=$(curl -s -X GET http://localhost:8081/api/vendors/$VENDOR_ID/branches \
     -H "Authorization: Bearer $TOKEN")
echo $BRANCHES
BRANCH_ID=$(echo $BRANCHES | grep -o '"branch_id":[^,]*' | head -n 1 | cut -d':' -f2 | tr -d '}')

echo "Branch ID: $BRANCH_ID"

echo "Adding gold..."
curl -s -X POST http://localhost:8081/api/vendors/$VENDOR_ID/add-gold \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json" \
     -d "{\"branchId\": $BRANCH_ID, \"quantity\": 15.5}"

echo "Fetching transactions..."
curl -s -X GET http://localhost:8081/api/vendors/$VENDOR_ID/transactions \
     -H "Authorization: Bearer $TOKEN"
