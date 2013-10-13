This is my entry for the 2013 Entelect R100K challenge: a programming competition where each contestant had to write an AI bot for a game that resembles the classic NES game Battle City. I reached the quarter-final round, where I was eliminated by the guy who then won the competition.

This project can:

 * Connect to a host to play the game as defined by the competition rules.
 * Visualize a real-time local game between two bots.
 * Simulate games to test and compare bots.

Using this project for anything else might lead to insanity, or something much worse like  a quantum vacuum metastability event. You have been warned.

Dependencies
------------

 * [Java 7 JDK]
 * [Maven]
 * Windows to run some batch scripts.

Installation
------------

```sh
git clone 
compile.bat
```

Run
---

 * To configure the competition client, see \src\main\java\za\co\entelect\competition\App.java. To run the competition client:
```sh
start.bat http://GameServerIP:GameServerPort
```
 * To configure the game visualizer, see \src\main\java\za\co\entelect\competition\GUI.java. To run the game visualizer:
```sh
startGUI.bat
``` 
 * To configure the simulator, see \src\main\java\za\co\entelect\competition\Simulator.java. To run the simulator:
```sh
startSimulator.bat
```

Project layout details
----------------------

\assets: Graphics and maps.

\src\main\java\za\co\entelect\challenge: The network interface code generated from WSDL.

\src\main\java\za\co\entelect\competition: My project code.

\src\main\java\za\co\entelect\competition\bots: My project bot zoo.

Description of my bots
-----------------------

**Brute:** Stupid and dependable. It targets enemy tanks and then the enemy base if the tanks are destroyed. It takes the shortest path to the target and fires as soon as there is a chance to hit. The only thing that saves it from complete uselessness is it incorporates bullets and their expected future positions into its pathfinding algorithm, so it will dodge bullets or if it can't dodge them. 

**BruteV2:** The bot I submitted in the end. It's an improvement of **Brute**. It is a lot better because it contains map-specific instructions, a few bug fixes and optimizations  . It's very fast because it's rule-based, about 1 millisecond to plan a move.

**MCTS:** Uses Monte Carlo tree search (MCTS) with UCT. It is completely useless on normal maps, and unbeatable on tiny maps (like 15x15). I tried to incorporate it into my Brute bot for close quarters battles but it was too short-sighted. For example it would fire dangerous shots at friendly targets that are far away if gets stuck.

**Minimax:** A decent bot, better than MCTS. Uses complex but fast heuristic (300 LOC, algorithmic complexity is O(sqrt(n)) with n = the number of blocks/tiles on the map). It can search 8 plies in about 25 milliseconds. But the bot is quite short sighted, lazy (let's do action NONE on this turn, the enemy base isn't going anywhere! A decaying heuristic caused problems of its own) and prone to driving like a South-African taxi (cut off other vehicles, then tell them to get out of the way and refuse to move until they do).

**MinimaxFixedDepth4:** The **Minimax:** bot fixed to a 8 ply search. A good balance between speed and performance.

License
----

MIT


  [Java 7 JDK]: http://www.oracle.com/technetwork/java/javase/downloads/index.html
  [Maven]: https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html