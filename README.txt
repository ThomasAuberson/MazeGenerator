Maze Generator
Version: 0.31
Author: Thomas Auberson

----------------------

ABOUT: Java Maze Generator randomly generates square mazes. 
Mazes can also be manually created and edited.

----------------------

HOW TO RUN:
1. You will need to install the latest version of Java Runtime Environment (JRE).
   This can be found on the Oracle website.

2. No installation is necessary. Simply run the JAR file FlashCardz.jar

----------------------

TILE LEGEND:
* Black - Wall (Wall)
* Grey - Temp (Temporary)
* White - Open (open)
* Blue - Start Point(Open)
* Red - End Point (Open)
* Pink - Path (Open)

----------------------

FILE:
* New - Loads a fresh new maze mao
* Open - Open an existing saved maze map
* Save - Save the current maze map to a file
* Map Size - Change the size (in tiles) of new mazes
* Undo - Indefiniteley undo changes to maze map

Controls:	
* Generate Random Maze - Fully generates a new random maze
* Generate Main Path - Generates a main path
* Generate Sub Path - Generates a sub path to a random location
* Temp Tiles to Wall - Changes all current Temp tiles to Wall tiles
* Path Tiles to Open - Changes all current Path tiles to Open tiles
* Center Maze Position - Centers the maze map in the display window

MOUSE TOOLS:
* Wall/Open Tool - Left Click to place Wall tiles
		 - Right Click to place Open tiles
* Path/Temp Tool - Left Click to place Path tiles
		 - Right Click to place Temp tiles
* Start/End Tool - Left Click to place new Start tile. 
			This will replace existing one with an open tile
		 - Right Click to place new End tile. 
			This will replace existing one with an open tile
* Sub-Path Tool  - Left Click to place a subpath starting at this tile

----------------------

SCROLLING CONTROLS:

* Drag maze with mouse to change its position
* Rotate mouse wheel to zoom in/out