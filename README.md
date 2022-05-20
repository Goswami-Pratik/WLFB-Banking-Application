# WLFB-Banking-Application

Wilfred Brown Banking application that deals with concurrency control &amp; implements networking protocols

Design and develop the WLFB Bank Application (WLFB), where each user has
a client which will communicate to a bank server. The application should be a multi-threaded clientserver system where each client can simultaneously add, subtract and transfer money to and from
their account. Both the client and server will be developed in a command-line interface (CLI). The
server will hold the client bank accounts and execute the operations as per the client's instructions.
Additionally, a custom logger feature will be developed that will allow me to debug and demonstrate
how the server operates.

## Requirements

1. Create a multi-threaded client-server application that uses locking, which demonstrates how
issues in network computing such as concurrency, are dealt with in a multi-client scenario.
2. Each client will start at 1000 units (an arbitrary and artificial currency) in their account.
3. The account balance can go below zero.
4. Implement add (amount) operation - that will add the specified amount to their account.
5. Implement subtract (amount) operation - that will subtract the specified amount from their
account.
6. Implement transfer (senderID, receiverID, amount) operation - that will transfer the
specified money from sender to receiver account.

### Additional Features

7. Implement an exit operation – that will terminate the client-server connection and stop the
client.
8. Simple log files system that writes the server operations to a log file (in real-time) – which
makes debugging much simpler.
9. The clients will be automatically assigned to make the application scalable.
10. Implement client input validation to ensure no exceptions are thrown such as validating if
the account exists before operating.

## [License](https://opensource.org/licenses/MIT)

By contributing, you agree that your contributions will be licensed under the MIT license.
