# SWEN20003 Semester 1, 2026
# Project 1
# Shadow Aliens

## Running Instructions
Download and extract the zip file for this project
Open IntelliJ IDEA
Click Open and select the extracted folder
Select ShadowAliens.java and click the green Run button

## Assumptions

* The maximum number of projectiles and explosions is 1000 each. (according to what Josh said in Post 98 on Ed)

## AI Statement
I have not used any AI tools or technologies to prepare this assessment. 

## Code References

* I did not use any code references 

## Design Report

### OOP
Encapsulation: PlayerShip.java lines 6-65 use Encapsulation. 
This involves the PlayerShip class only. By defining PlayerShip, I have grouped all attributes of the PlayerShip class together with all the methods that manipulate them into a single class.

Delegation: ShadowAliens.java lines 102-126 use Delegation.
The classes involved are: ShadowAliens, Lives, Wave, Score, PlayerShip, EnemyShip, Projectile, and Explosion.
The ShadowAliens class delegates its drawing responsibility to the other classes because the drawBattleScreen() method calls the respective draw methods of each class, for example, lives.drawLives(), wave.drawWave() etc.

Polymorphism: ShadowAliens.java lines 98-100 use Polymorphism.
The classes involved are ShadowAliens, Ship, PlayerShip, and EnemyShip. 
ShadowAliens has a private helper method drawShip(Ship ship) which calls ship.drawShip() through a Ship reference.
When passed a PlayerShip object, PlayerShip.drawShip() is called, and when passed an EnemyShip object, EnemyShip.drawShip() is called.


### Design choice
I have separated draw() and update() functionality for the classes mentioned below. Each class has a draw() method and an update() method, and update() does not call draw() internally.
The classes involved are ShadowAliens, PlayerShip, EnemyShip, Projectile, and Explosion. 
ShadowAliens calls all draw methods together in drawBattleScreen(), and all update methods separately in its update() method.

An alternative would be to call draw() inside update() for every class, so updating and rendering happen in one update call for each entity, which was my original design. This approach would have meant ShadowAliens only needs to implement its update() method, which calls each class's update method, which internally handles its own drawing. So ShadowAliens doesn't have to deal with drawing logic explicitly through a drawBattleScreen() method. 

However, the implemented design choice is more maintainable because it allows the pause screen to reuse drawBattleScreen() directly without running any game logic. 
Freezing the game simply means calling drawBattleScreen() without calling any entity update methods. 

If drawing was embedded inside update() for every class, pausing the game would mean adding a paused flag inside every class. Each update() method would need to check the  flag and skip movement and game logic if paused (but draw() will be called regardless of paused flag). This tedious approach would mean writing pause checks for every class rather than handling the check in one place(i.e in ShadowAliens). Separating drawing and updating makes the pause feature simple to implement and makes the code easier to extend for additional screens in Project 2.

## Design Report References

* I did not use any external references 
