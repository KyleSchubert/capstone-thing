<h1>Capstone thing for the one class</h1>
Roguelike deckbuilder in Java using libGDX

<h1>Requirements</h1>

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
- [ ] Can be clicked to travel to it if the previous node connects to it
- [ ] Is highlighted when it can be traveled to
- [x] Lines exist to show paths between nodes
- [ ] Completing the boss node creates a new map


<b>During Battles</b>
- [ ] Player has by default 3 energy refreshed at the start of each turn
- [ ] Player draws by default 5 cards at the start of each turn
- [ ] HP persists across battles


<b>Not in battles</b>
- [ ] Amount of temporary currency the player has is visible
- [ ] The time elapsed in the current round is visible
- [ ] The player can see how much health their character has
- [ ] The player can click on something to view their deck


<b>Cards</b>
- [ ] Player spends energy to play cards unless a card is free
- [ ] Can be made to target a specific enemy or hit all enemies
- [ ] Deal damage to enemies, inflict a debuff on enemies, heal the player, do nothing, or buff the player
- [ ] Go into the discard pile once used
- [ ] Discard pile shuffled into draw pile once the draw pile is empty


<b>Enemies</b>
- [ ] Have HP
- [ ] Are of a specific type, which grants them different HP values and abilities
- [ ] Uses 1 ability when it is their turn
- [ ] The ability that they will use when it is their turn is shown above them by an icon and damage number, and with a tooltip on hover

 
<b>After Battles</b>
- [ ] Player receives in-game currency to spend at shops
- [ ] Player receives a choice of 3 cards to add to their deck
- [ ] If it was an elite battle, player receives a random artifact item and more in-game currency
- [ ] If it was a boss battle, player receives a choice of 3 high-tier artifact items, more in-game currency, and in-game currency that persists between runs


<b>Shops</b>
- [ ] Player can pick from 8 cards to buy
- [ ] Player can pick from 3 artifacts to buy
- [ ] Player can pay to remove a card from their deck
- [ ] Player can pay to upgrade a card in their deck


<b>Rest area</b>
- [ ] Player can rest to restore HP
- [ ] Player can upgrade 1 cards in their deck
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
- [ ] Player receives a choice of 5 artifacts or 1 of 5 upgraded powerful cards
- [ ] Player starts with some in-game currency
- [ ] Player starts with 10 cards, consisting of comparatively weak, un-upgraded defensive and offensive cards.


<b>Winning</b>
- [ ] The player receives much more persistent in-game currency

