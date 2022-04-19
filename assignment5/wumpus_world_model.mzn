% Wumpus World
%Elements = {Ok=0, Archer=1, Stench=2, Breeze=3, Glitter=4, Pit=5, Wumpus=6};
%input matrix 4x4
int: size;
set of int: DIM = 1..size;
array[DIM, DIM] of int: cave;
output ["Cave: \n", show2d(cave)];
% as result we want a 0/1 matrix that encodes
% points on a path in the cave.
% 1 means the point is on the path and 0 means it's not.
array[DIM, DIM] of var 0..1: path;

%Archer starts from path[4,1]
%The Game ends when the Archer reaches the gold
var int: gold_row;
var int: gold_col;
constraint forall(i in DIM, j in DIM where cave[i, j]=4)
(
  gold_row=i /\ gold_col=j
);
%So we fix start room and end room
constraint path[size,1]=1 /\ path[gold_row, gold_col]=1;

%Pits can not be in the path
constraint forall(i in DIM, j in DIM where cave[i, j] = 5)(
  path[i, j] = 0
);
%Also the room with the Wumpus can not be in the path
constraint forall(i in DIM, j in DIM where cave[i, j] = 6)(
  path[i, j] = 0
);

%check the room that can be reached
constraint forall(i in DIM, j in DIM where cave[i, j]=0 \/ cave[i,j]=2 \/ cave[i,j]=3)
(
  path[i, j] = 1
);

output ["\n Path: \n", show2d(path)];

var int: pathCost = sum(i in DIM, j in DIM where path[i, j] = 1)(1);
output ["\n Performance Measure: \(1000-pathCost)."];
solve minimize pathCost;