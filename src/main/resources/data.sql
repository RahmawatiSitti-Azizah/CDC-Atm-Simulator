INSERT INTO account (account_number, account_holder_name, pin, balance)
  (SELECT '112233', 'Rahmawati Sitti Azizah', '223311', 500
  FROM DUAL
  WHERE NOT EXISTS (SELECT 1 FROM account a WHERE a.account_number = '112233')
  );
INSERT INTO account (account_number, account_holder_name, pin, balance)
  (SELECT '223311', 'Jane Doe', '123111', 1000
  FROM DUAL
  WHERE NOT EXISTS (SELECT 1 FROM account a WHERE a.account_number = '223311')
  );
INSERT INTO account (account_number, account_holder_name, pin, balance)
  (SELECT '223211', 'John Doe', '313121', 700
  FROM DUAL
  WHERE NOT EXISTS (SELECT 1 FROM account a WHERE a.account_number = '223211')
  );