-- SELECT COLUMN_NAME
-- FROM INFORMATION_SCHEMA.COLUMNS
-- WHERE TABLE_NAME = 'users';


-- SELECT name AS index_name,
--        type_desc AS index_type,
--        is_unique,
--        is_primary_key,
--        is_unique_constraint
-- FROM sys.indexes
-- WHERE object_id = OBJECT_ID('users');

-- ALTER TABLE users ADD CONSTRAINT unique_username UNIQUE (username);
-- ALTER TABLE employees
-- ALTER COLUMN employeeId BIGINT;


-- ALTER TABLE inventory
-- DROP CONSTRAINT UKtmn2wfhwhvtw3xjjtk5r823br;

-- ALTER TABLE inventory
-- ADD CONSTRAINT UC_ProductName UNIQUE (product_name);

-- ALTER TABLE inventory
-- ADD CONSTRAINT check_price_positive CHECK (price >= 0);


-- DELETE FROM Inventory
-- WHERE inventory_id = 4;

-- SELECT * 
-- FROM information_schema.table_constraints 
-- WHERE table_name = 'inventory' AND constraint_type = 'UNIQUE';

-- SELECT * FROM users;
-- SELECT * FROM employees;
-- SELECT * FROM branches;
-- SELECT * FROM inventory;
-- SELECT * FROM customers;


-- SELECT branch
-- FROM employees
-- WHERE username = 'ori1';

-- DELETE FROM employees;
-- DELETE FROM branches;
-- DELETE FROM inventory;
-- DELETE FROM users;

