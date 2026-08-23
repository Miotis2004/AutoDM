# Project Conventions and Code Quality Baseline

## Architecture Layering
- **Controllers:** Controllers must be thin and solely handle HTTP request/response serialization, validation, and delegation.
- **Services:** Services contain business logic, application state coordination, and domain workflows. Game logic should reside here or in dedicated engines/services, out of controllers.
- **Repositories:** Repositories handle data access, persistence, and querying.
- **Domain Models:** Domain models represent the core entities and state.

## Naming Conventions
- Prevent collisions with common JDK or framework types (e.g. `java.util.List`, `java.util.Map`, `java.lang.System`, `java.lang.Thread`, etc.) by using descriptive prefixes or suffixes for domain entities where necessary, although generally a unique domain concept like `Campaign` or `PlayerCharacter` is safe.
- Prefer clear, intention-revealing names for methods and classes.

## General
- No game logic inside Controllers.
- All dependencies on persistence should flow through Repositories.
