# Performance characteristics

| Map                           | size | get, ns/op | put, ns/op | put GC alloc.rate, B/op |
|-------------------------------|-----:|-----------:|-----------:|------------------------:|
| TypedLinkedMutableMap         |    5 |     10.200 |     32.284 |                      27 |
| HashMap                       |    5 |     17.960 |     59.152 |                      60 |
| Object2ObjectOpenHashMap      |    5 |     18.532 |     76.600 |                      77 |
| TypedLinkedMutableSyncMap     |    5 |     26.404 |     33.596 |                      27 |
| ConcurrentHashMap             |    5 |     17.696 |     96.044 |                      64 |
| Object2ObjectOpenHashSyncMap  |    5 |     33.793 |     88.491 |                      85 |
|                               |      |            |            |                         |
| TypedLinkedMutableMap         |   54 |     53.611 |     71.444 |                      22 |
| HashMap                       |   54 |     18.777 |     67.870 |                      49 |
| Object2ObjectOpenHashMap      |   54 |     20.944 |     75.925 |                      37 |
| TypedLinkedMutableSyncMap     |   54 |     64.001 |     73.796 |                      22 |
| ConcurrentHashMap             |   54 |     19.387 |    196.722 |                      58 |
| Object2ObjectOpenHashSyncMap  |   54 |     36.277 |     84.537 |                      38 |
|                               |      |            |            |
| TypedLinkedMutableMap         |  180 |    146.572 |    166.583 |                      22 |
| HashMap                       |  180 |     20.311 |     58.244 |                      41 |
| Object2ObjectOpenHashMap      |  180 |     24.775 |     68.227 |                      23 |
| TypedLinkedMutableSyncMap     |  180 |    160.616 |    168.050 |                      22 |
| ConcurrentHashMap             |  180 |     20.600 |    157.194 |                      47 |
| Object2ObjectOpenHashSyncMap  |  180 |     40.550 |     69.922 |                      23 |
