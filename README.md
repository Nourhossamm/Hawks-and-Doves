# Hawks-and-Doves
A simple Agent based model created to simulate Hawks and Doves game
 For the Hawk-Dove Game shown in the table below :
              Hawk                 Dove
Hawk   (V/2)-C , (V/2) – C       V, 0
Dove     0, V                    V/2, V/2
*ABM of 100 players that can choose either adopting the Hawk or Dove Strategy,
considering the following cases:
1- V = 100, C = 10
2-V = 100, C ~ U (10,60)
3-V = 100, C~ N (30,5)
4- V ~ N (100,10), C~ N (30,5)

*Agents can choose their actions based on:
1- Only keep the used strategy if it yields a non-negative utility, else, switch
2- Match what the other agent it encountered in t -1 did. 
