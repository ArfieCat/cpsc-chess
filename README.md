# :chess_pawn: CPSC Program Similar to Chess

## What will the application do?

**CPSC** (CPSC Program Similar to Chess) allows users to play a variant of chess with fog of war.
In other words, a player can only see squares that a piece of their colour can move to on the next turn.

## Who will use it?

Anyone with some free time and a friend to play with can try **CPSC**, especially those with an interest in chess or
other strategic games.

## Why is this project of interest to you?

Chess has recently undergone an unprecedented rise in popularity, and I wanted to try putting a unique spin on the
classic format. Additionally, from a programming standpoint, I believe that chess is a sufficiently complex game which
will be challenging but rewarding to implement.

---

# :bust_in_silhouette: User Stories

As a user, I want to be able to...

- [x] add a move to a list of prior moves.
- [x] move pieces on the board according to chess rules.
- [x] play against another user on the same device.
- [x] view the current state of the board.
- [x] save a game that is currently in progress.
- [x] load a previously saved game and continue playing.
- [ ] export a game to a PGN-like(?) format.

---

# :mortar_board: Mechanics

1. A square is only visible if a piece of the current player's colour can move to it.
2. Players are not informed of check. The king may move into or be left in check.
3. There is no checkmate. The game ends when a player's king is captured.