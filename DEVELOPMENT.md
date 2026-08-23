# AutoDM Development Roadmap

This document outlines the step-by-step development process for the AutoDM project.

### PROJECT-01: Repository foundation and multi-root structure
**Objective:** Create the top-level repository layout with client/ and server/ roots and shared documentation, without adding project-name nesting.

**Policy:**
- **Read Only:** False
- **Require Build:** False
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] A client/ directory exists for the Angular application.
- [ ] A server/ directory exists for the Spring Boot application.
- [ ] A top-level README describes the project, technology stack, and how to run the frontend and backend locally.
- [ ] No additional project-name nesting exists inside client/ or server/.

### PROJECT-02: Server Maven wrapper and Spring Boot skeleton
**Objective:** Scaffold the Spring Boot server with a Maven Wrapper, Java 21 toolchain, and Spring Web plus Spring Data JPA dependencies.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [x] The Maven Wrapper files (mvnw, mvnw.cmd, and .mvn wrapper) are present in server/.
- [x] pom.xml declares Java 21 and Spring Boot parent with spring-boot-starter-web and spring-boot-starter-data-jpa.
- [x] A Spring Boot main application class exists and the project compiles with the Maven Wrapper.
- [x] The server starts and shuts down cleanly with no database configured yet.

### PROJECT-03: Angular 22 client scaffold
**Objective:** Create the Angular 22 application in client/ with TypeScript, routing, and forms enabled.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [x] The Angular application is created with Angular CLI version 22.
- [x] package.json pins Angular 22 core packages.
- [x] Routing and reactive forms modules are enabled.
- [x] The application builds successfully with the Angular production build.

### PROJECT-04: SQLite persistence configuration strategy
**Objective:** Configure Spring Boot to use a local SQLite database via Spring Data JPA with a maintainable schema approach.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [x] A SQLite JDBC driver dependency is declared in pom.xml.
- [x] application.properties configures a SQLite datasource URL pointing to a local file.
- [x] JPA ddl-auto and dialect settings are configured for SQLite.
- [x] The server starts and can open the SQLite database file.

### PROJECT-05: Cross-platform local storage path handling
**Objective:** Make the SQLite database path application-relative and configurable for Windows, macOS, and Linux.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [x] The database path is read from a configuration property rather than a hard-coded absolute path.
- [x] The default path resolves relative to an application or user home directory.
- [x] No Windows-only absolute paths are hard-coded in the backend.
- [x] The path can be overridden via configuration.

### PROJECT-06: Project conventions and code quality baseline
**Objective:** Establish naming, layering, and code-quality conventions for controllers, services, repositories, and domain models.

**Policy:**
- **Read Only:** True
- **Require Build:** False
- **Require Repository Change:** False

**Acceptance Criteria:**
- [x] Conventions document the separation between controllers, services, repositories, and domain models.
- [x] Conventions prohibit application-level Java declarations that collide with common JDK framework types.
- [x] Conventions require thin controllers and game logic kept out of controllers.
- [x] The conventions are recorded in the repository and referenced by later tasks.

### PROJECT-07: Campaign domain model and repository
**Objective:** Implement the Campaign entity with title, description, status, dates, notes, and isolated state, plus its JPA repository.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [x] The Campaign entity stores title, description, status, creation date, last-played date, and notes.
- [x] A JPA repository interface for Campaign exists.
- [x] Campaigns can be created, read, updated, and deleted through the repository.
- [x] The server compiles with the campaign persistence layer.

### PROJECT-08: Player character domain model and repository
**Objective:** Implement the PlayerCharacter entity with name, ancestry, class, level, background, alignment, and core combat stats.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [x] The PlayerCharacter entity stores name, ancestry, class, level, background, alignment, hit points, maximum hit points, armor class, and movement.
- [x] The entity supports ability scores, saving throws, skills, and proficiency information.
- [x] A JPA repository interface for PlayerCharacter exists and is owned by a campaign.
- [x] Multiple characters can be associated with a single campaign.

### PROJECT-09: Character resources model
**Objective:** Model persistent character resources including health, temporary health, limited-use abilities, spell/power resources, ammunition, consumables, currency, conditions, and death state.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] Character resources are stored as persistent, campaign-scoped records rather than transient fields only.
- [ ] Temporary health, limited-use abilities, resources, ammunition, consumables, and currency are represented.
- [ ] A death or unconscious state flag is supported.
- [ ] Resource changes can be persisted and reloaded across sessions.

### PROJECT-10: World model for locations, regions, settlements, and POIs
**Objective:** Implement world entities for locations, regions, settlements, buildings/points of interest, descriptions, discovered state, and travel relationships.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] World entities represent locations, regions, settlements, and points of interest.
- [ ] Locations store descriptions and discovered/undiscovered state.
- [ ] Travel relationships between locations are representable.
- [ ] A current party location reference is supported per campaign.

### PROJECT-11: NPC domain model and repository
**Objective:** Implement the NPC entity with name, description, role, disposition, faction, location, alive state, relationship to party, notes, and combat statistics.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] The NPC entity stores name, description, role, disposition, faction, current location, and active/inactive state.
- [ ] NPC relationship toward the party and notes are stored.
- [ ] Optional combat statistics are supported for relevant NPCs.
- [ ] NPC state persists across sessions within a campaign.

### PROJECT-12: Faction domain model and repository
**Objective:** Implement the Faction entity with name, description, disposition, relationships, reputation, and notes.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] The Faction entity stores name, description, disposition, reputation or standing, and notes.
- [ ] Faction-to-faction relationships are representable.
- [ ] Factions are scoped to a campaign.
- [ ] Faction state can be updated and persisted.

### PROJECT-13: Quest and objective domain model and repository
**Objective:** Implement Quest and Objective entities with objectives, completion state, rewards, giver, related locations, and notes.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] The Quest entity supports active, completed, and failed states.
- [ ] Quests contain one or more objectives with per-objective completion tracking.
- [ ] Quest rewards, quest giver, related locations, and notes are stored.
- [ ] Quest state persists and can be queried per campaign.

### PROJECT-14: Item and inventory domain model and repository
**Objective:** Implement Item and inventory ownership entities with categories, quantity, ownership, equipped state, descriptions, value, and transfers.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] Items support categories such as weapons, armor, consumables, quest items, and miscellaneous.
- [ ] Items store quantity, value, equipped state, and description.
- [ ] Inventory ownership ties items to characters or a campaign.
- [ ] Item transfers between owners are representable.

### PROJECT-15: Creature and enemy template domain model and repository
**Objective:** Implement reusable creature/enemy template entities with health, defense, attack and damage information, initiative, and behavior notes.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] The creature template entity stores name, description, health, defense, attack, damage, initiative modifier, and behavior notes.
- [ ] Templates are reusable and campaign-scoped.
- [ ] Campaigns can instantiate enemies from templates.
- [ ] Creature templates can be created and listed.

### PROJECT-16: Session domain model and repository
**Objective:** Implement the Session entity linking to a campaign and recording start/end times and event references.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] The Session entity records campaign reference, start time, and optional end time.
- [ ] Sessions can be started, resumed, and ended.
- [ ] Session history can be queried per campaign.
- [ ] Session state persists across application restarts.

### PROJECT-17: Event and history domain model and repository
**Objective:** Implement the CampaignEvent entity for persistent recording of significant campaign events.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] The CampaignEvent entity records event type, timestamp, campaign reference, and a text or structured description.
- [ ] Event types cover session start, location entry, discovery, combat, damage, item acquisition, quest changes, relationship changes, and session end.
- [ ] Events can be listed and inspected per campaign.
- [ ] Event data persists across sessions.

### PROJECT-18: Encounter, combatant, and condition domain models and repositories
**Objective:** Implement encounter, combatant, and condition entities supporting turn-based combat and status effects.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] The encounter entity references a campaign, scene, and location and tracks status.
- [ ] Combatant entities represent player and enemy participants with hit points and defeated state.
- [ ] Turn ordering and current turn are tracked.
- [ ] Condition entities store name, description, duration, source, and active state.

### PROJECT-19: Dice and random resolution service
**Objective:** Build a backend dice service supporting d4-d20, percentile, multiple dice, and modifiers with backend-generated randomness.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] The service supports d4, d6, d8, d10, d12, d20, percentile, multiple dice, and modifiers.
- [ ] Randomness is generated on the backend, not from the browser.
- [ ] Roll results are returned with a breakdown of individual dice and totals.
- [ ] The service compiles and exposes a clear API for game logic.

### PROJECT-20: Ability and skill resolution service
**Objective:** Build a service that resolves ability/skill checks using a character statistic, modifier, generated roll, and difficulty.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] The service computes a total from a character statistic and modifier plus a generated roll.
- [ ] The service compares the total against a difficulty and returns success or failure.
- [ ] Results are consumable by Dungeon Master logic.
- [ ] The service compiles and is usable by the resolution layer.

### PROJECT-21: Conditions and status effects service
**Objective:** Build a service to apply, track, and expire reusable conditions and status effects.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] The service supports conditions such as poisoned, stunned, frightened, unconscious, and restrained.
- [ ] Conditions carry name, description, duration, source, and active state.
- [ ] The service can activate, deactivate, and expire conditions by duration.
- [ ] The service compiles and integrates with combat and character state.

### PROJECT-22: Rest and recovery service
**Objective:** Build a service for short and long rest operations that restore health, clear selected conditions, and restore selected resources.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] The service supports short and long rest operations.
- [ ] Rest restores health and clears selected temporary conditions.
- [ ] Rest restores selected limited-use resources.
- [ ] Rest advances campaign or session state and persists changes.

### PROJECT-23: Turn-based combat and encounter engine
**Objective:** Implement the turn-based encounter engine managing initiative, turn order, current turn, and encounter completion.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] The engine builds initiative and turn order from combatants.
- [ ] The engine tracks the current turn and advances turns.
- [ ] Defeated combatants are excluded from turn order.
- [ ] Encounter completion is detectable when one side is fully defeated.

### PROJECT-24: Deterministic enemy behavior engine
**Objective:** Implement a modular enemy behavior engine that selects valid targets, performs available attacks, applies damage, and skips invalid actions.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] The engine selects a valid living target for enemy actions.
- [ ] The engine performs an available attack and applies damage.
- [ ] The engine skips defeated or invalid targets.
- [ ] The behavior engine is modular so richer AI can later replace it.

### PROJECT-25: Encounter generation rules
**Objective:** Implement basic automated encounter generation considering location, campaign state, difficulty, party strength, and available enemy definitions.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] Encounter generation can create encounters manually and via basic automated rules.
- [ ] Generation considers party level or strength and available enemy definitions.
- [ ] Difficulty can influence generated encounter composition.
- [ ] Generated encounters can be instantiated into the encounter engine.

### PROJECT-26: Attack and damage resolution
**Objective:** Implement attack resolution and damage application including defense, damage types, and defeated transitions.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] Attack resolution combines a roll, attack bonus, and target defense.
- [ ] Damage is applied to target hit points and can defeat combatants.
- [ ] Defeated combatants are marked and removed from active turns.
- [ ] Attack and damage results are available to the encounter engine and event system.

### PROJECT-27: Dungeon Master engine abstraction
**Objective:** Define a DM engine interface and a deterministic implementation behind a clear abstraction for future LLM providers.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] A DM engine interface abstracts action handling and response generation.
- [ ] A deterministic implementation drives the initial engine.
- [ ] The abstraction allows a future local or remote LLM provider to be added without rewriting the app.
- [ ] The engine presents scene info, validates actions, resolves mechanics, applies state changes, and records events.

### PROJECT-28: Scene management
**Objective:** Implement scene management with title, narrative, current location, involved characters, available actions, encounter reference, and status.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] A scene holds title, narrative, current location, and involved characters/NPCs.
- [ ] A scene references an active encounter when one exists.
- [ ] The DM engine can advance between scenes.
- [ ] Scene state persists with the campaign.

### PROJECT-29: Narrative templates and output
**Objective:** Implement narrative templates that generate structured narrative responses from game state and events.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] Narrative templates produce responses from structured game state.
- [ ] Output can be categorized for DM narration, player actions, dice results, combat events, and system events.
- [ ] Templates are data- or code-driven and extensible.
- [ ] Narrative output is consumable by the frontend game log.

### PROJECT-30: Player action input and validation
**Objective:** Define structured player action types with optional free-text description and validate them against current game state.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] Structured action types cover investigate, talk, travel, attack, use item, rest, search, interact, and skill actions.
- [ ] Actions carry an optional free-text description.
- [ ] Invalid or impossible actions are rejected with clear errors.
- [ ] The action model allows richer natural-language interpretation to be added later.

### PROJECT-31: Game event system integration
**Objective:** Wire the event system so engine actions record significant campaign events during a session.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] Engine actions record events such as session start, location entry, discovery, combat, damage, item acquisition, quest changes, relationship changes, and session end.
- [ ] Events are persisted per campaign.
- [ ] The event system is usable by the DM engine and services.
- [ ] Events can be inspected later through the API.

### PROJECT-32: Campaign service and REST controller
**Objective:** Implement the campaign service and controller for create, edit, archive/delete, and active selection.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] REST endpoints support creating, editing, archiving, deleting, and selecting campaigns.
- [ ] The controller is thin and delegates to the campaign service.
- [ ] Campaigns expose title, description, status, dates, and notes via DTOs.
- [ ] The server compiles and endpoints respond.

### PROJECT-33: Character service and REST controller
**Objective:** Implement the character service and controller for managing player characters and their resources within a campaign.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] REST endpoints manage player characters and character resources.
- [ ] Characters are scoped to a campaign.
- [ ] Resource updates persist across requests.
- [ ] The server compiles and endpoints respond.

### PROJECT-34: World and location service and REST controller
**Objective:** Implement the world service and controller for managing locations, regions, settlements, POIs, travel, and party location.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] REST endpoints manage world entities and travel relationships.
- [ ] Discovered state and current party location are manageable.
- [ ] Locations are usable by game-session logic.
- [ ] The server compiles and endpoints respond.

### PROJECT-35: NPC service and REST controller
**Objective:** Implement the NPC service and controller for managing NPCs and their relationships within a campaign.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] REST endpoints manage NPC records and relationship values.
- [ ] NPC state persists across sessions.
- [ ] Relationship changes can be applied and persisted.
- [ ] The server compiles and endpoints respond.

### PROJECT-36: Faction service and REST controller
**Objective:** Implement the faction service and controller for managing factions, relationships, and reputation.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] REST endpoints manage factions, relationships, and standing.
- [ ] Reputation changes can be applied and persisted.
- [ ] Factions are scoped to a campaign.
- [ ] The server compiles and endpoints respond.

### PROJECT-37: Quest service and REST controller
**Objective:** Implement the quest service and controller for managing quests, objectives, rewards, and quest state.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] REST endpoints create quests, complete or fail objectives and quests.
- [ ] Quest state is visible during play and persists.
- [ ] Quests reference givers and related locations.
- [ ] The server compiles and endpoints respond.

### PROJECT-38: Item and inventory service and REST controller
**Objective:** Implement the item service and controller for managing items, inventory ownership, equipped state, and transfers.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] REST endpoints manage items, ownership, equipped state, and transfers.
- [ ] Quantities are validated and cannot go negative where inappropriate.
- [ ] Inventory state persists per campaign.
- [ ] The server compiles and endpoints respond.

### PROJECT-39: Session service and REST controller
**Objective:** Implement the session service and controller for starting, resuming, ending sessions, and viewing history.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] REST endpoints start, resume, and end sessions.
- [ ] Session history is queryable per campaign.
- [ ] Sessions record important events and decisions.
- [ ] The server compiles and endpoints respond.

### PROJECT-40: Dungeon Master engine controller and action endpoint
**Objective:** Expose the DM engine through a REST controller with an action endpoint that accepts player actions and returns structured responses.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] A REST endpoint accepts player actions for the current campaign and scene.
- [ ] The endpoint returns scene info, narrative, dice results, and state changes.
- [ ] The endpoint triggers encounters, completes objectives, discovers locations, and updates relationships as appropriate.
- [ ] The server compiles and the endpoint responds.

### PROJECT-41: Backend validation of requests and game actions
**Objective:** Add DTO validation rejecting invalid identifiers, negative quantities, malformed actions, and invalid entity references.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] DTO validation rejects invalid identifiers and malformed game actions.
- [ ] Impossible negative quantities are rejected where inappropriate.
- [ ] Invalid references between campaign entities are rejected.
- [ ] Invalid encounter transitions are rejected.

### PROJECT-42: API error handling
**Objective:** Implement consistent API error responses for validation and runtime failures.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] Validation failures return structured error responses with a status code and message.
- [ ] Invalid game actions return a clear error response.
- [ ] Unexpected backend errors return a consistent error shape.
- [ ] The server compiles and error responses are observable.

### PROJECT-43: Local deployment and CORS configuration
**Objective:** Configure the backend for local deployment including CORS so the Angular dev server can communicate with it.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] CORS is configured to allow the local Angular dev server.
- [ ] The backend runs as a standalone local process.
- [ ] The backend exposes the REST API on a stable local port.
- [ ] The server compiles and accepts cross-origin requests from the frontend.

### PROJECT-44: Safe database initialization on startup
**Objective:** Ensure the application initializes a new local SQLite database safely when none exists without destroying existing data.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] A new SQLite database is created safely when none exists.
- [ ] Existing campaign data is not destroyed during normal startup.
- [ ] Schema creation runs idempotently.
- [ ] The server starts cleanly with an empty and a pre-populated database.

### PROJECT-45: Angular app shell, routing, and navigation
**Objective:** Build the Angular app shell with navigation, page titles, and logical routes for major application areas.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] Navigation and clear page titles are present.
- [ ] Routes exist for dashboard, campaigns, play, characters, quests, world, NPCs, encounters, history, and settings.
- [ ] The app shell renders a navigation bar.
- [ ] The application builds successfully.

### PROJECT-46: Angular backend communication services
**Objective:** Implement Angular services for REST communication with the backend and central state management.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] HTTP services wrap backend endpoints for campaigns, characters, world, NPCs, quests, items, sessions, and the DM engine.
- [ ] Authoritative campaign state is not duplicated across scattered components.
- [ ] Services expose observable APIs for components.
- [ ] The application builds successfully.

### PROJECT-47: Campaign dashboard screen
**Objective:** Build the dashboard showing active campaign, current location, active characters, quests, encounter, summary, and recent events.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] The dashboard displays the active campaign, current location, active characters, and current quests.
- [ ] The dashboard shows the active encounter and recent events.
- [ ] The dashboard loads data from backend services.
- [ ] The application builds successfully.

### PROJECT-48: Campaign management UI
**Objective:** Build campaign management screens for creating, editing, archiving, deleting, and selecting campaigns.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] Campaign creation and editing forms are provided.
- [ ] Archive/delete actions include destructive-action confirmations.
- [ ] Active campaign selection is supported.
- [ ] The application builds successfully.

### PROJECT-49: World, NPC, quest, faction, item, and creature management screens
**Objective:** Build management screens for characters, NPCs, locations, quests, factions, inventory/items, creature templates, and campaign history.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] Screens exist for characters, NPCs, locations, quests, factions, items, and creature templates.
- [ ] Screens support create, edit, and list operations with validation feedback.
- [ ] Empty states are shown when no records exist.
- [ ] The application builds successfully.

### PROJECT-50: Gameplay screen
**Objective:** Build the primary play screen with DM narrative, action input, party summary, scene/location, relevant NPCs, encounter status, and recent rolls.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] The play screen shows the DM narrative and an action input area.
- [ ] The play screen shows party summary, current scene/location, and relevant NPCs.
- [ ] The play screen shows encounter status and recent roll/result information.
- [ ] The narrative log distinguishes DM narration, player actions, dice results, combat events, and system events.
- [ ] The application builds successfully.

### PROJECT-51: Session management UI
**Objective:** Build UI for starting, resuming, and ending sessions and viewing session history.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] The UI can start a session and resume an existing campaign.
- [ ] The UI can end a session.
- [ ] Session history is viewable.
- [ ] The application builds successfully.

### PROJECT-52: History and event inspection screen
**Objective:** Build a screen for inspecting prior campaign events and session history.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] The history screen lists campaign events with type and timestamp.
- [ ] Events can be filtered or viewed per campaign.
- [ ] The screen loads data from backend services.
- [ ] The application builds successfully.

### PROJECT-53: Encounter and combat UI
**Objective:** Build UI to start, progress, and complete turn-based encounters, showing turn order, combatants, and combat events.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] The UI can start an encounter and progress turns.
- [ ] Turn order, current turn, and combatant health are shown.
- [ ] Combat events and dice results are displayed.
- [ ] Encounter completion is shown.
- [ ] The application builds successfully.

### PROJECT-54: Frontend error handling
**Objective:** Make the frontend visibly handle API unavailable, invalid operations, failed saves, invalid actions, and encounter errors.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] API-unavailable failures are shown to the user.
- [ ] Invalid user operations and failed saves surface clear messages.
- [ ] Invalid game actions and encounter errors are displayed.
- [ ] Backend failures are not silently ignored.
- [ ] The application builds successfully.

### PROJECT-55: Loading, empty states, and confirmations
**Objective:** Add loading indicators, empty states, and confirmations for destructive actions across the UI.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] Loading indicators appear during backend requests.
- [ ] Empty states are shown for lists with no data.
- [ ] Destructive actions require confirmation.
- [ ] Forms provide readable validation feedback.
- [ ] The application builds successfully.

### PROJECT-56: Settings screen
**Objective:** Build a settings screen for local configuration such as storage path and display preferences.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] The settings screen exposes configurable local options.
- [ ] Settings are presented in readable forms.
- [ ] The application builds successfully.
- [ ] Settings do not require internet connectivity.

### PROJECT-57: End-to-end campaign creation workflow integration
**Objective:** Integrate frontend and backend to support a complete campaign creation workflow with character and world setup.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] A user can create a campaign through the UI.
- [ ] Player characters can be created and persisted for the campaign.
- [ ] World, NPC, quest, and item data can be created and persisted.
- [ ] The full workflow completes end to end through the local frontend and backend.

### PROJECT-58: Playable Dungeon Master session loop integration
**Objective:** Integrate the DM engine, dice, mechanics, and narrative so a full playable session loop runs from the UI.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] A user can start a session and submit player actions through the UI.
- [ ] The backend generates game responses and advances state.
- [ ] Dice and ability checks resolve through backend logic.
- [ ] At least one complete turn-based encounter can be run from start to finish.
- [ ] Narrative output is displayed in the game log.

### PROJECT-59: Persistence correctness and save-resume
**Objective:** Verify that campaign state persists across restarts and that a saved campaign can be resumed from persisted state.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] Campaign, character, world, NPC, quest, and item data survive application restart.
- [ ] A user can close and reopen the application, select a campaign, and continue from persisted state.
- [ ] Session and event data persist.
- [ ] SQLite remains the authoritative persistence store.

### PROJECT-60: Build readiness and manual acceptance preparation
**Objective:** Ensure the project builds cleanly, initializes its database safely, and is ready for human manual acceptance.

**Policy:**
- **Read Only:** False
- **Require Build:** True
- **Require Repository Change:** True

**Acceptance Criteria:**
- [ ] The server builds successfully with the Maven Wrapper.
- [ ] The Angular client builds successfully in production mode.
- [ ] The application initializes its local SQLite database safely on a clean start.
- [ ] The application runs locally with frontend and backend communicating.
- [ ] Documentation describes how to run the application and the manual acceptance checklist.
