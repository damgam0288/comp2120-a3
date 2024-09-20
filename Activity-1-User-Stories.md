# User stories for Movement
As a player, I want to move around and explore the game map to discover new challenges and areas.<br>
As a player, I want a keyboard-only interface that lets me move the character using simple key presses, without needing to use the mouse.<br>

As a designer, I want to define which objects block the player’s movement, such as allowing the player to move through *items* but not through *walls*.<br>

# User stories for Maze/Map/Level
As a player, I want a game map with multiple interactive elements to keep the game engaging.<br>
As a player, I want to explore more than one game map to experience different environments with new challenges in each map.<br>

As a designer, I want to provide a text/JSON file that the system can read to generate the game map.<br>
As a designer, I want the freedom to place walls, items, enemies etc. in the game map wherever I choose.<br>
As a designer, I want to set varying sizes of each game map.<br>
As a designer, I want to modify the game map’s appearance by changing the symbols used for the floor, walls, NPCs, and other elements.<br>
As a designer, I want to customize all map settings using a simple configuration file in the game directory.<br>

As a game tester, I want an efficient way to test multiple game maps to ensure all features work as expected.<br>

# User stories for Enemies
As a player, I want to encounter enemies on the map so that I can participate in battles and make progress in the game.<br>
As a player, I want to face enemies that block my path, forcing me to either fight them or find alternative routes, so that I can increase the strategic depth of my movements.<br>
As a player, I want to take damage from enemies during battles, so that combat is more challenging.<br>
As a player, I want to see the enemy's health, so that I can plan my battle strategy and decide my actions better.<br>
As a player, I want to only move to the next map after defeating all enemies on the current map, so that the game has a clear goal.<br>

As a designer, I want to define the generation points of enemies in the game configuration file, so that enemies will appear in fixed positions and cause strategic difficulty in certain parts of the map.<br>

# User stories for Additional Feature 1: Play/Pause

As a player, I want to be able to pause the game, so that I can leave the computer and know my progress will not change.
As a player, I want to be able to unpause the game, so that when the game is in a paused state I can continue playing where I left off.

As a designer, I want the paused game state to change, to clearly indicate when the game is paused and expecting a particular input to continue.

# User stories for Additional Feature 2: Character Levels

As a player, I want to be able to level up my character, so that I can play the game and progess the way that I want to.
As a player, I want to be able to achieve all in-game achievements, so that I can feel a sense of full completion and mastery of the game.

As a designer, I want to add extra motives to the game, so that the player has a choice to their progression.

# User stories for Additional Feature 3: Inventory
As a player, I want to be able to choose what items in my invenotry I want to equip, so that I can interact with enemies the way that I want.
As a player, I want to be able to store items in an inventory, so that I can use items later on in the game.

As a designer, I want the player's inventory to be capped, so that it creates more strategy around what items the player chooses to pick up.
As a designer, I want the items in the inventory to be useful and different in some way, so that there is a reason for the player to choose which items to use and pick up throughout the game.
