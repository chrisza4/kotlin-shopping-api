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

## Design Decision

### Entity creation is not allowed in non-domain layer

When user create a purchase request, they must send some data to the Web API. Question: Should API level have ability to create a PurchaseRequest entity itself?

There are two ways to handle this.

- We can set a rule where communication between API layer and Domain layer must be done via an arbitrary data class. The benefit is that now API layer don't have to understand what underlying entity is. It will be simply a data interface. We decouple the API layer and domain layer. The downside: Extra overhead for any entity change. eg. simply adding field in PurchaseRequest require us to change both entity and data classes,
- We can allow API layer to initiate an entity, and send it to domain layer as an input. It will be easier to add, rename, modify entity. But now we coupled the API and domain. So any domain logic change can unintentionally possibly affect API layer.

Interesting decision to make. For this project, I will go with the first route. I believe the first route conform more to the spirit of hexagonal architecture.

PS. A possible reason why the traditional Rails-style can be more productive for starting new project. In Rails-style, you simply create a model in controller and if model and controller will change together 80% of the times, Rails will be more productive.

## Integration test in Domain Use Cases

If we are assuming that Domain layer will be consumed by multiple input/output stream, which is the real spirit of hexagonal architecture, then it makes sense to do the integration test in domain layer.

## Where to put logic of whether user can approved PR

There are three possible places
1. User.CanApproved(): Boolean
2. PurchaseRequestEntity
3. PurcahseReqeustUseCase

Three of them seems to all be valid, so let see what are we saying when we put in each of these place
- If I create a `User.CanApprove()` method, then I basically say to everyone that this logic should be shared to every domain who have access to User. EG. The logic of approval is valid for both PurchaseRequest system and, for example, Budgeting system. I incentivize every contributor to not reinventing any approval rights validating logic.
- If I put the `if` inside `PurchaseRequestEntity`, then I basically say that I reject any possibility of having approver as a non-manager of any kind.
- If I put the `if` inside `PurchaseRequestUseCase`, then I basically say that maybe in another set of use cases, it is acceptable to have a non-manager approver.

The first one seems to be invalid and premature. The second and third one seems to be equally valid, given that this is just a toy project.

I choose the second.

In real-life scenario, once you start to choose between option 2 and option 3, you have a question to ask the domain expert.

PS. The value of this exercise is the thought process, not the implementation itself.

## Note to self

Here is something I learned

### Auto-reloading dev workflow

In order to auto reload

- Add devtools dependency to gradle
- Run `gradle build --continuous` in one tab
- Run `gradle bootRun` in another tab

[Source](https://dzone.com/articles/continuous-auto-restart-with-spring-boot-devtools)

### What's left?

- [ ] Authentication - Can use mock token
- [ ] Approve, Reject and Negotiate - Might require new field in
- [ ] Easier way to seed the data - Nice to have
