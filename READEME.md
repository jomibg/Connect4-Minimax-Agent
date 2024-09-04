# Connect 4 Game

This code implements a Connect 4 board game built on top of a GUI created by Chris Clarke. The game can be played between two people or between a person and an automated playing agent. The implementation provides three different agent types: a random move agent, an agent that uses the minimax algorithm with a custom utility function and a maximum depth constraint, and an agent that utilizes the minimax algorithm with alpha-beta pruning, allowing for less restrictive depth constraints.

## Compiling and Running the Game

To compile the game:
- Navigate to the folder where you've downloaded the game and run the command `javac Connect4.java` to compile the game interface.
- Run the command `java Connect4` to start the game.

## Navigating the GUI and Setting Game Modes

Once the game is started, the GUI will display an empty game board with buttons above each column used for playing the game. Above the board, there is a main menu where you can restart or exit the game (options displayed in a dropdown menu upon clicking the *File* menu item), change the color of the agent starting the game (menu item *Options*), and change the type of each player to a Random move agent, Minimax agent, Minimax agent with \(\alpha-\beta\) pruning, or Human player (options displayed in the dropdown menu upon clicking the *Player* menu item). There is an additional menu item, *Info*, where you can check the number of moves played by each agent, the total number of moves, and the time taken for the last move (timed only for automated playing agents).

## Simulating Games

To verify the performance of the implemented playing agents, you can compile the file *GameSimulator.java* by executing the command `javac GameSimulator.java` and run the game with the command `java GameSimulator`. After this, you will be prompted to specify the types of automated agents and a folder where the results of the simulation will be saved. The results are saved in a file named `simulation_results_F_{1st_agent_type}_S_{2nd_agent_type}.txt`, where *1st_agent_type* and *2nd_agent_type* represent encoded agent types. This encoding is explained in the initial prompt displayed once the game is started.