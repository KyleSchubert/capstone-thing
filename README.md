<h1>What</h1>
This will be a roguelike deckbuilder made in Java using libGDX

<h1>Images of parts of the game so far</h1>
  <details><summary>Menu</summary>
   
![image](https://github.com/KyleSchubert/capstone-thing/assets/51379097/dd08cae3-18ab-42e8-b5f2-6b22e3e1558c)
   </details>
   
   <details><summary>Map</summary>
   
![image](https://github.com/KyleSchubert/capstone-thing/assets/51379097/80d70c27-6eb1-41ae-a9c3-edb77b734331)
   </details>

  <details><summary>Combat</summary>

![image](https://github.com/KyleSchubert/capstone-thing/assets/51379097/e3e42a16-e4b0-4b3d-bd1f-165777436ac9)
  </details>

  <details><summary>Rewards</summary>
  
![image](https://github.com/KyleSchubert/capstone-thing/assets/51379097/12f80e6c-a681-46da-bed9-8d7b410c589c)
  </details>

  <details><summary>"Start with an item" panel</summary>
   
![image](https://github.com/KyleSchubert/capstone-thing/assets/51379097/f9aa7acf-3c20-4e2e-9405-71b1b8cb1c1c)
  </details>

  <details><summary>Shop</summary>
    
![image](https://github.com/KyleSchubert/capstone-thing/assets/51379097/02bf3c96-6d4f-41bd-b923-9c62daafee03)
  </details>

  <details><summary>Rest Area</summary>
    
![image](https://github.com/KyleSchubert/capstone-thing/assets/51379097/05e850d9-0c90-4d57-8a75-21d04d2e1615)
  </details>
  
</details>

<h1>How do I run this?</h1>
Click the release in the Releases section, download the .zip, extract the entire folder together, and run the .exe

<h1>How do I build this?</h1>
If you want to build the game, follow this: https://libgdx.com/wiki/start/import-and-running 
<br>
I am using IntelliJ IDEA.

<h1>Where are the files?</h1>

- Classes and all that: `/game/core/src/com/roguelikedeckbuilder/mygame`
- `MyGame.java`, which is in there ^ is the "main" for this
- Assets: `/game/assets`
- The desktop launcher: `/game/desktop/src/com/roguelikedeckbuilder/mygame/DesktopLauncher.java`

<h1>Requirements</h1>
* Some things aren't listed or marked here, like the UseLine or HP Bars, but were added anyways
<br>
<br>

<b>Map</b>
- [x] Randomly-generated branching map of a fixed height.
- [x] Map nodes connect to other nodes but you can only move forward toward the boss room
- [x] Only 1 boss room
- [x] Only 1 starting room
- [x] Controls for generation of map nodes
- [x] Hover over map nodes to see information on what they mean ~A key for what the map node icons mean~


<b>Map Nodes</b>
- [x] (NOTE: THIS CAN BE CHANGED WITH THE `MAX_NEXT_CONNECTIONS_PER_NODE` constant.) A node can either connect to 1 next node or 2 next nodes that are adjacent (no zig-zagging across the entire map)
- [x] x and y Positions of nodes randomized on the map
- [x] content of the nodes mostly randomized (Only 1 boss room, only 1 starting room, and then guaranteed 1 resting room before the boss room)
- [x] Can be a normal battle, elite battle, starting node, boss battle, shop, rest area, random event, treasure (The type of node determines the appearance of the node)
- [x] Can be clicked to travel to it if the previous node connects to it
- [x] Is highlighted when it can be traveled to
- [x] Lines exist to show paths between nodes
- [x] Completing the boss node creates a new map
- [x] Clicking a node starts its event
- [ ] Elite combat node starts that event
- [ ] Boss combat node starts that event
- [ ] Node depth (further along in the game) controls the type of enemies that are spawned in battle


<b>During Battles</b>
- [x] Player has by default 3 energy refreshed at the start of each turn
- [x] Player draws by default 5 cards at the start of each turn
- [x] HP persists across battles
- [x] The player can click on something to view their draw pile


<b>Not in battles</b>
- [x] Amount of temporary currency the player has is visible
- [x] The time elapsed in the current round is visible
- [x] The player can see how much health their character has
- [x] The player can click on something to view their deck


<b>Cards</b>
- [x] Player spends energy to play cards unless a card is free
- [x] Can be made to target a specific enemy or hit all enemies
- [ ] Deal damage to enemies, inflict a debuff on enemies, heal the player, do nothing, or buff the player
- [x] Go into the discard pile once used
- [x] Discard pile shuffled into draw pile once the draw pile is empty


<b>Enemies</b>
- [X] Have HP
- [X] Are of a specific type, which grants them different HP values and abilities
- [ ] Uses 1 ability when it is their turn
- [ ] The ability that they will use when it is their turn is shown above them by an icon and damage number, and with a tooltip on hover

 
<b>After Battles</b>
- [x] Player receives in-game currency to spend at shops
- [x] Player receives a choice of 3 cards to add to their deck
- [ ] If it was an elite battle, player receives a random artifact item and more in-game currency
- [ ] If it was a boss battle, player receives a choice of 3 high-tier artifact items, more in-game currency, and in-game currency that persists between runs


<b>Shops</b>
- [x] Player can pick from 8 cards to buy
- [ ] Player can pick from 3 artifacts to buy
- [x] Player can pay to remove a card from their deck
- [x] Player can pay to upgrade a card in their deck


<b>Rest area</b>
- [x] Player can rest to restore HP
- [x] Player can upgrade 1 cards in their deck
- [ ] Player can double the rewards of the next elite or boss battle but make it more difficult


<b>Main menu</b>
- [x] Start game button<br>
- [x] Exit game button
- [x] Upgrades button
- [x] Settings button
- [ ] Has a background
- [ ] Music plays
- [ ] Has a title
- [ ] Shows a version number in the bottom left


<b>Pause menu</b>
- [x] Pauses the game
- [x] Has resume button
- [x] Has settings button
- [x] Has give up button


<b>Settings menu</b>
- [x] Has a back button
- [ ] Has a music volume slider
- [ ] Has a sound effects volume slider


<b>Results menu</b>
- [x] Has main menu button
- [ ] Shows how far the player got
- [ ] Shows the time spent playing that round
- [ ] Shows how much persistent currency the player earned
- [ ] Shows if the player won or lost


<b>Upgrades menu</b>
- [x] Has a back button
- [ ] Player can spend their in-game currency that persists between runs to buy permanent upgrades for all future runs


<b>Starting the game</b>
- [x] A menu appears with options for the player to choose from
- [ ] Player receives a choice of 5 artifacts or 1 of 5 upgraded powerful cards
- [x] Player starts with some in-game currency
- [ ] Player starts with 10 cards, consisting of comparatively weak, un-upgraded defensive and offensive cards.


<b>Winning</b>
- [ ] The player receives much more persistent in-game currency

