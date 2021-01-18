# Shopping API

This is a toy API for simple purchasing system.

I built this to experiment with Hexagonal Architecture, Spring and Kotlin.

## What it can do?

Imagine you are building are purchase requesting system where employee can request for a budget from managers.


Here the specs for V0.1
- Employee can generate a purchase request, with items and send it to their manager.
- Employee can see only their purchase request.
- Manager can see every purchase request sent to them.
- Manager can either approve, reject or send back the purchase request to the employee for future negotiation.

Simple enough I guess

So here are APIs.

- POST /purchase_request --> Create a purchase request
- GET /purchase_requests --> List all purchase request I can view
- GET /purchase_request/{id} --> Get the detail of purchase request
- POST /purchase_request/{id}/approve --> Approve the purchase request
- POST /purchase_request/{id}/reject --> Reject the purchase request with reason (Manager only)
- POST /purchase_request/{id}/negotiate --> Negotiate the purchase request with comment (Manager only)

For the sake of simplicity: Let start by seeding 3 users
- Mark Zuckerburg as a manager
- Chris Likit as an employee
- James Harden as an employee

Let see how it goes