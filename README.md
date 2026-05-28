# SWEN20003 Semester 1, 2026
# Project 2b
# Shadow Aliens

## Running Instructions
Download and extract the zip file for this project.
Open IntelliJ IDEA.
Click Open and select the extracted folder.
Select ShadowAliens.java and click the green Run button.

## Assumptions

* None

## AI Statement
I have not used any AI tools or technologies to prepare this assessment. 

## Code References

* I did not use any code references 

## Design Report

## Extension:
Project 1 had a single enemy type that moved straight down the screen. Project 2 extended this with three types: regular, strafing, and shooting enemies, each with unique behaviour.

### Code Changes and Depth
Project 1 had no BattleScreen or GameEntity class, so all game logic was inside ShadowAliens. The single EnemyShip class handled all enemy behaviour. Ship existed as a base class for shared movement behaviour. 

Several significant and deep changes were made:
1.GameEntity was introduced as a new abstract base class above Ship, to encapsulate shared state like position, image, and bounding box (this logic was previously scattered across PlayerShip, EnemyShip, explosion etc.)
2.Ship was modified to extend GameEntity, which cascaded changes down to PlayerShip and EnemyShip. Both their constructors, getters, and setters were updated to align with the new hierarchy.
3.The existing EnemyShip class was made abstract. 3 concrete subclasses of EnemyShip, RegularEnemy, StrafingEnemy, and ShootingEnemy, were added, each overriding getScore() and/or update(). 
4.ShadowAliens was simplified to a thin entry point. BattleScreen was introduced as a dedicated class to manage gameplay logic extracted from ShadowAliens, and new methods loadWave(), checkCollisions(), isWaveComplete() were added. 

### Design Principles in Project 1
Ship, as a base class, showed some use of inheritance and abstraction, but there was no polymorphism across enemy types, as there was 1 enemy class. Multiple logics (text displays, player movement, collision checks) within ShadowAliens led to low cohesion and violated the single responsibility principle. It also made it difficult to extend, as adding a new enemy type would require modifying the main game class directly, violating the open-closed principle. No GameEntity abstraction existed, so shared behaviour, like image rendering, was repeated across all classes.

### How the Extension Was Made Harder
The existing Ship class gave a foundation to build on, and EnemyShip extended it naturally. However, the absence of GameEntity meant position attributes and image rendering had to be abstracted from scratch before the enemy hierarchy could be built; this required changes across multiple existing classes before new enemy types could even be written. The lack of BattleScreen meant that significant restructuring of ShadowAliens was necessary. Had these abstractions existed in Project 1, adding new enemy types would have satisfied the Open-Closed principle and required only new subclasses; so BattleScreen could call e.getScore(), e.update(), and e.draw() polymorphically on the enemy ArrayList without changes to its existing code.

## Outcome
Introducing GameEntity above Ship means shared behaviour is abstracted once and inherited everywhere, improving cohesion as each class has a single well-defined responsibility, and BattleScreen coordinates them without duplicating any internal behaviour.

Wave loading uses the method EnemyShip.create() to encapsulate and centralise object creation, so BattleScreen is unaware of specific enemy types entirely. This reduces coupling between BattleScreen and the enemy hierarchy as BattleScreen never references any enemy subclass directly.

Placing shoot() inside EnemyShip with a default null return and letting ShootingEnemy override means BattleScreen can call e.shoot() on every enemy without any instanceof checks (further reducing coupling), demonstrating polymorphism. Similarly, having getScore() as an abstract method inside EnemyShip means each subclass owns its score value, so BattleScreen doesn't need conditionals to check the type and grant the correct score.

The current design is highly extensible. Adding new enemy types only requires creating new subclasses of EnemyShip, overriding update() & getScore(), and then adding one case to EnemyShip.create(). BattleScreen requires no changes at all, demonstrating the open-closed principle. The one minor limitation is that EnemyShip.create() must be modified for each new type.

Overall, the hierarchy introduced in Project 2 leaves the codebase significantly more maintainable than Project 1.

## Design Report References

* I did not use any external references 
