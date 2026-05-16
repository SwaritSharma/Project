-- Table for Addresses
CREATE TABLE addresses (
                           address_id INT PRIMARY KEY AUTO_INCREMENT,
                           street VARCHAR(255) NOT NULL,
                           city VARCHAR(100) NOT NULL,
                           state VARCHAR(100) NOT NULL,
                           postal_code VARCHAR(20),
                           country VARCHAR(100) NOT NULL
);

-- Table for Users
CREATE TABLE users (
                       user_id INT PRIMARY KEY AUTO_INCREMENT,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       name VARCHAR(100) NOT NULL,
                       address_id INT,
                       balance DECIMAL(18, 2) DEFAULT 0.0,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       FOREIGN KEY (address_id) REFERENCES addresses(address_id),
                       password VARCHAR(255)
);

-- Table for Gold Vendors
CREATE TABLE vendors (
                         vendor_id INT PRIMARY KEY AUTO_INCREMENT,
                         vendor_name VARCHAR(100) UNIQUE NOT NULL,
                         description TEXT,
                         contact_person_name VARCHAR(100),
                         contact_email VARCHAR(100),
                         contact_phone VARCHAR(20),
                         website_url VARCHAR(255),
                         total_gold_quantity DECIMAL(18, 2) NOT NULL DEFAULT 0.0,
                         current_gold_price DECIMAL(18, 2) NOT NULL DEFAULT 5700.00,
                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                         password VARCHAR(255)
);

-- Table for Vendor Branches
CREATE TABLE vendor_branches (
                                 branch_id INT PRIMARY KEY AUTO_INCREMENT,
                                 vendor_id INT,
                                 address_id INT,
                                 quantity DECIMAL(18, 2) NOT NULL DEFAULT 0.0,
                                 created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                 FOREIGN KEY (vendor_id) REFERENCES vendors(vendor_id),
                                 FOREIGN KEY (address_id) REFERENCES addresses(address_id)
);

-- Table for Virtual Gold Holdings
CREATE TABLE virtual_gold_holdings (
                                       holding_id INT PRIMARY KEY AUTO_INCREMENT,
                                       user_id INT,
                                       branch_id INT,
                                       quantity DECIMAL(18, 2) NOT NULL,
                                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                       FOREIGN KEY (user_id) REFERENCES users(user_id),
                                       FOREIGN KEY (branch_id) REFERENCES vendor_branches(branch_id)
);

-- Table for Physical Gold Transactions
CREATE TABLE physical_gold_transactions (
                                            transaction_id INT PRIMARY KEY AUTO_INCREMENT,
                                            user_id INT,
                                            branch_id INT,
                                            quantity DECIMAL(18, 2) NOT NULL,
                                            delivery_address_id INT,
                                            created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                            FOREIGN KEY (user_id) REFERENCES users(user_id),
                                            FOREIGN KEY (branch_id) REFERENCES vendor_branches(branch_id),
                                            FOREIGN KEY (delivery_address_id) REFERENCES addresses(address_id)
);

-- Table for Payments
CREATE TABLE payments (
                          payment_id INT PRIMARY KEY AUTO_INCREMENT,
                          user_id INT,
                          amount DECIMAL(18, 2) NOT NULL,
                          payment_method ENUM(
        'Credit Card',
        'Debit Card',
        'Google Pay',
        'Amazon Pay',
        'PhonePe',
        'Paytm',
        'Bank Transfer'
    ),
                          transaction_type ENUM(
        'Credited to wallet',
        'Debited from wallet'
    ),
                          payment_status ENUM(
        'Success',
        'Failed'
    ),
                          created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Table for Transaction History
CREATE TABLE transaction_history (
                                     transaction_id INT PRIMARY KEY AUTO_INCREMENT,
                                     user_id INT,
                                     branch_id INT,
                                     transaction_type ENUM(
        'Buy',
        'Sell',
        'Convert to Physical'
    ),
                                     transaction_status ENUM(
        'Success',
        'Failed'
    ),
                                     quantity DECIMAL(10, 2) NOT NULL,
                                     amount DECIMAL(18, 2) NOT NULL,
                                     created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                     FOREIGN KEY (user_id) REFERENCES users(user_id),
                                     FOREIGN KEY (branch_id) REFERENCES vendor_branches(branch_id)
);