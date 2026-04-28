# AGENTS.md

## Purpose
- This file guides coding agents working in `pos-plus`.
- Follow these rules unless the user explicitly asks for a different approach.
- Keep changes small, consistent with the current architecture, and easy to review.

## Repository Snapshot
- Build tool: Gradle Wrapper (`gradlew`, `gradlew.bat`).
- Language: Java 25 toolchain.
- Framework: Spring Boot 4.0.6.
- Dependency management: `io.spring.dependency-management` plugin.
- Main app class: `src/main/java/com/soft/pos_plus/PosPlusApplication.java`.
- Test framework: JUnit 5 (via `spring-boot-starter-test`).
- Security test support: `spring-security-test`.
- Data access: Spring Data JPA + SQL Server driver.
- Lombok is used in DTOs/entities for boilerplate.

## Current Project Layout
- `src/main/java/com/soft/pos_plus/api`: REST controllers, API response wrapper, global exception handling.
- `src/main/java/com/soft/pos_plus/application`: DTOs, mappers, services, app-level exceptions.
- `src/main/java/com/soft/pos_plus/domain`: domain entities, repository ports, service contracts.
- `src/main/java/com/soft/pos_plus/infrastructure`: JPA entities, adapters, persistence impl, security config.
- `src/main/resources/application.properties`: runtime configuration.
- `src/test/java`: tests.
- `src/test/resources/application.properties`: test-time config overrides.

## Cursor/Copilot Rules Status
- `.cursor/rules/`: not present.
- `.cursorrules`: not present.
- `.github/copilot-instructions.md`: not present.
- Therefore, there are no additional Cursor/Copilot instruction files to merge.

## Build, Run, Lint, and Test Commands

### Preferred Wrapper
- Linux/macOS: `./gradlew <task>`
- Windows (PowerShell/CMD): `./gradlew.bat <task>`

### Core Commands
- Build everything: `./gradlew.bat build`
- Clean build outputs: `./gradlew.bat clean`
- Compile only: `./gradlew.bat classes testClasses`
- Run all tests: `./gradlew.bat test`
- Run verification lifecycle: `./gradlew.bat check`
- Run app locally: `./gradlew.bat bootRun`
- Build executable jar: `./gradlew.bat bootJar`
- Build OCI image: `./gradlew.bat bootBuildImage`

### Single Test Execution (Important)
- Single test class:
  - `./gradlew.bat test --tests "com.soft.pos_plus.PosPlusApplicationTests"`
- Single test method:
  - `./gradlew.bat test --tests "com.soft.pos_plus.PosPlusApplicationTests.projectCompilesAndRunsTests"`
- Pattern-based subset:
  - `./gradlew.bat test --tests "com.soft.pos_plus.*"`

### Fast Validation Loop for Agents
- Before finishing code changes, run:
  - `./gradlew.bat test`
- For risky refactors or dependency/config edits, run:
  - `./gradlew.bat clean build`

### Lint/Formatting Status
- There is currently no dedicated linter/formatter Gradle task configured (no Checkstyle/Spotless/PMD task found).
- Treat Java compiler warnings, test results, and code review rules in this file as the quality gate.
- If adding a linter later, wire it into `check` so agents can run a single verification command.

## Architecture and Boundaries
- Keep a layered, ports-and-adapters flow:
  - API -> Application -> Domain ports -> Infrastructure adapters.
- Domain should not depend on Spring or infrastructure classes.
- Application services orchestrate use cases, validation, and domain port calls.
- Infrastructure implements domain repository interfaces and external integrations.
- Controllers should remain thin: parse input, call service, return response.
- Keep mappers explicit and centralized (`application.mappers`, `infrastructure.persistence.mapper`).

## Java Code Style Guidelines

### Imports
- Prefer explicit imports; avoid wildcard imports.
- Group imports in this order with one blank line between groups:
  - `java.*` / `javax.*` / `jakarta.*`
  - third-party (Spring, Lombok, etc.)
  - `com.soft.pos_plus.*`
- Remove unused imports.
- Keep static imports minimal and only when readability improves.

### Formatting and Structure
- Use 4 spaces for indentation (no tabs in newly edited files).
- Keep one top-level public type per file.
- Put annotations directly above target declarations.
- Use braces for all control blocks, even single-line blocks.
- Keep methods focused; extract private helpers when logic grows.
- Prefer early returns to reduce nesting.
- Keep line length reasonable (target <= 120 chars when practical).

### Types and Nullability
- Prefer concrete domain types over `Object`.
- Use `UUID` for entity identifiers (current project convention).
- Use `BigDecimal` for monetary values; never use floating-point for price.
- Use `Optional<T>` for repository lookup results; do not use Optional for fields.
- For nullable request fields in partial updates, use wrapper types (e.g., `Boolean` vs `boolean`).
- Avoid raw types and unchecked casts.

### Naming Conventions
- Packages: lowercase dot-separated.
- Classes/interfaces: `PascalCase`.
- Methods/fields/variables: `camelCase`.
- Constants: `UPPER_SNAKE_CASE`.
- REST endpoints: plural, resource-based, versioned when already versioned (current: `/api/v1/products`).
- Repository interface names should reflect domain ports (`ProductRepository`).
- Implementation suffixes are acceptable for adapters (`*RepositoryImpl`).
- New interfaces should avoid unnecessary `I` prefix; prefer role-based names.

### DTOs, Entities, and Mapping
- Keep request/response DTOs in `application.dtos`.
- Keep persistence entities in `infrastructure.entities` only.
- Do not expose persistence entities from API responses.
- Use mapper classes for all transformations; do not duplicate mapping logic in controllers/services.
- When adding fields, update:
  - request/response DTOs
  - domain model
  - entity model
  - both mapper layers
  - tests

### Error Handling
- Use domain/application-specific runtime exceptions for expected business errors:
  - `BadRequestException`, `NotFoundException`, `ConflictException`.
- Throw exceptions with actionable, client-safe messages.
- Centralize HTTP mapping in `GlobalExceptionHandler`.
- Do not leak stack traces or internal DB details in API messages.
- Map validation failures to 400 responses.
- Keep a catch-all 500 handler for unexpected exceptions.

### Validation Rules
- Validate required fields in application service layer (current convention).
- Reject invalid numeric values explicitly (e.g., price <= 0, stock < 0).
- Guard uniqueness constraints in service before persistence when feasible.
- Keep validation messages consistent and deterministic.

### Spring and Dependency Injection
- Prefer constructor injection (`@RequiredArgsConstructor`) over field injection.
- Keep `@Service`, `@Repository`, `@Component` usage aligned with layer responsibilities.
- Keep configuration classes under `infrastructure.config`.
- Keep controllers free of persistence calls.

### Transactions and Persistence
- Add `@Transactional` when introducing multi-step write operations requiring atomicity.
- Keep repository interfaces in domain and JPA details in infrastructure.
- Prefer Spring Data query methods for simple predicates.
- Ensure entity column constraints align with business constraints.

### Logging
- Add structured logs for key lifecycle events and failures when needed.
- Never log secrets (DB password, tokens, credentials).
- Keep log messages concise and useful for operations.

## Testing Guidelines
- Place tests under `src/test/java` mirroring production package structure.
- Name test classes `*Test` or `*Tests`.
- Test method names should describe behavior and expected outcome.
- Favor focused unit tests for service logic and mapper behavior.
- Add integration tests for controller/repository wiring when behavior crosses layers.
- For bug fixes, add or update a test that would fail before the fix.
- Keep tests deterministic and independent.

## Configuration and Secrets
- Do not hardcode credentials in new code or docs.
- Use environment variables or profile-specific properties for secrets.
- Keep test configuration isolated from production databases.
- If changing `application.properties`, document required env vars in README or PR notes.

## Agent Workflow Checklist
- Read related files across all impacted layers before editing.
- Implement minimal, cohesive changes that satisfy the request.
- Run targeted tests first, then full `test` when feasible.
- If behavior changes, update tests in the same change.
- Keep public API contracts backward compatible unless requested otherwise.
- Leave codebase in a buildable, testable state.

## Suggested Future Enhancements (Optional)
- Add Spotless or Checkstyle and connect to `check`.
- Add JaCoCo coverage reporting and minimum thresholds.
- Add test slices (`@WebMvcTest`, `@DataJpaTest`) as test suite grows.
