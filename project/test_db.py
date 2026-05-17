import mysql.connector
import sys

try:
    conn = mysql.connector.connect(
        host="localhost",
        user="root",
        password="root@123",
        database="digitalgoldwallet"
    )
    cursor = conn.cursor()
    cursor.execute("SELECT transaction_id, transaction_type, branch_id FROM transaction_history;")
    for row in cursor.fetchall():
        print(row)
    conn.close()
except Exception as e:
    print(e)
    sys.exit(1)
