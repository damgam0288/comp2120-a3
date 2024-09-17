## Requirements:
- **Make maps** Custom "map" files can be loaded into the game.
- **Make NPCs/Enemies** NPCs are loaded from a file; key-value pairs used to provide parameters for
  JSON file.
- **Game control** A single *game config file* sets the maps to load, the order of map progression,
  which *NPC/enemy files* to load and which map they will go into. The engine looks for this
  *game config file* in order to run the game.

## Potential problems:
- Designer puts too many NPCs to fit on the map itself.
- Illegal/out of bounds (x,y) position for NPCs
- Missing / bad parameters
- Game config file is missing
- NPC/Enemy/Map file that the game config references is missing

## Approach
- [ ] IN PROGRESS... Allow loading of individual map files
- [ ] Implement game config file to set level progression
- [ ] Allow loading NPCs/enemies from files
