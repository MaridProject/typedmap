# typedmap

| Map                           | size | get, ns/op |
|-------------------------------|-----:|-----------:|
| TypedLinkedMutableMap         |    5 |     10.200 |
| HashMap                       |    5 |     17.960 |
| Object2ObjectOpenHashMap      |    5 |     18.532 |
| TypedLinkedMutableSyncMap     |    5 |     26.404 |
| ConcurrentHashMap             |    5 |     17.696 |
| Object2ObjectOpenHashSyncMap  |    5 |     33.793 |
|                               |      |            |
| TypedLinkedMutableMap         |   54 |     53.611 |
| HashMap                       |   54 |     18.777 |
| Object2ObjectOpenHashMap      |   54 |     20.944 |
| TypedLinkedMutableSyncMap     |   54 |     64.001 |
| ConcurrentHashMap             |   54 |     19.387 |
| Object2ObjectOpenHashSyncMap  |   54 |     36.277 |
|                               |      |            |
| TypedLinkedMutableMap         |  180 |    146.572 |
| HashMap                       |  180 |     20.311 |
| Object2ObjectOpenHashMap      |  180 |     24.775 |
| TypedLinkedMutableSyncMap     |  180 |    160.616 |
| ConcurrentHashMap             |  180 |     20.600 |
| Object2ObjectOpenHashSyncMap  |  180 |     40.550 |
