-- ============================================================
-- OIBSIP Java ATM Interface — Database Schema
-- SQL Server
-- ============================================================

-- Create the database (run this first, only once)
-- CREATE DATABASE ATM_DB;
-- GO
-- USE ATM_DB;
-- GO

-- ============================================================
-- Table: Accounts
-- Stores each customer's account details and current balance
-- ============================================================
CREATE TABLE Accounts (
    AccountId   VARCHAR(20)   PRIMARY KEY,
    UserId      VARCHAR(50)   NOT NULL UNIQUE,
    Pin         VARCHAR(4)    NOT NULL,
    HolderName  VARCHAR(100)  NOT NULL,
    Balance     DECIMAL(12,2) NOT NULL DEFAULT 0
);
GO

-- ============================================================
-- Table: Transactions
-- Stores a full log of every deposit, withdrawal, and transfer
-- ============================================================
CREATE TABLE Transactions (
    Id            INT IDENTITY(1,1) PRIMARY KEY,
    AccountId     VARCHAR(20)   NOT NULL,
    Type          VARCHAR(20)   NOT NULL,       -- Deposit / Withdraw / Transfer In / Transfer Out
    Amount        DECIMAL(12,2) NOT NULL,
    BalanceAfter  DECIMAL(12,2) NOT NULL,
    Details       VARCHAR(200)  NULL,
    Timestamp     DATETIME      NOT NULL DEFAULT GETDATE(),

    CONSTRAINT FK_Transactions_Accounts
        FOREIGN KEY (AccountId) REFERENCES Accounts(AccountId)
);
GO

-- ============================================================
-- Sample / Demo Data (optional — matches the demo credentials
-- used in Main.java)
-- ============================================================
-- INSERT INTO Accounts (AccountId, UserId, Pin, HolderName, Balance)
-- VALUES
--     ('ACC1001', 'asad594',   '1234', 'Muhammad Asad', 5000.00),
--     ('ACC1002', 'sara_khan', '5678', 'Sara Khan',     3000.00);
-- GO

-- ============================================================
-- SQL Login used by the Java application (JDBC connection)
-- ============================================================
-- CREATE LOGIN atm_user WITH PASSWORD = 'Atm@12345';
-- GO
-- USE ATM_DB;
-- GO
-- CREATE USER atm_user FOR LOGIN atm_user;
-- ALTER ROLE db_owner ADD MEMBER atm_user;
-- GO