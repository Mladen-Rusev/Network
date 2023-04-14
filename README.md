
# Wallet
Java Spring Project: **Wallet**
This Java Spring project is a simple implementation of a wallet system with transactions between wallets, handling wallet creation, transaction processing, and retrieving wallet information.

## Classes
The project has the following main classes:

**WalletController**: This class handles incoming HTTP requests and delegates processing to the WalletService. 

**WalletService**: This class is responsible for handling wallet-related business logic.

**WalletServiceImpl**: This class is the implementation of the WalletService interface, providing the actual processing for wallet-related business logic.

**Network**: This singleton class represents a simplified blockchain network for managing wallets and transactions.

**Wallet**: This class represents a wallet, including its address, balance, and associated transactions.

**Transaction**: This class represents a transaction between two wallets.

**IncomingTransactionParams**: This class is a POJO that holds the incoming transaction parameters.

## Functions
The main functions provided by this project are:

**getAllWallets**: Retrieve a list of all wallets.

**getWalletByAddress**: Retrieve a wallet by its address.

**getNewWallet**: Create a new wallet.

**initTransaction**: Initialize a transaction between two wallets.

## How to Run
To run this project, follow these steps:

Ensure you have Java JDK 11 or later installed on your machine.

Install Maven if it's not already installed.

Clone the project repository:

    git clone https://github.com/yourusername/limechaintask.git

Navigate to the project directory:

    cd limechaintask
    
Project is configured to run with MySql with the following credentials, defined in **application.properties**

    spring.datasource.url=jdbc:mysql://localhost:3306/data
    spring.datasource.username=root
    spring.datasource.password=root

For the project to work you will need to change the credentials in **application.properties** and create the 'data' schema.

Build the project using Maven:

    mvn clean install

Run the project:

    mvn spring-boot:run

The server should now be running on http://localhost:8080. You can use tools like Postman or curl to send HTTP requests to the available endpoints.

## Endpoints
Here are the available API endpoints:

**GET /wallets/all**: Retrieve a list of all wallets.

**GET /wallet/{address}**: Retrieve a wallet by its address.

**GET /wallet/new**: Create a new wallet.

**POST /**: Initialize a transaction between two wallets. The request body should include the sender's address, the recipient's address, and the amount to be transferred.

Example json request body:

    {
      "address": "0x32a7C02f7a608d7aB8Dc214eb9dD5434FeEfF7B4",
      "recipient": "0x11bC02f7a608d7aB8Dc214eb9dD5434FeEfF7C6",
      "amount": 10
    }





