# Use cases

## As a replacement for sparsed data structures with a high probability of uninitialized fields 

Suppose we have a data class (see https://github.com/MaridProject/typedmap/blob/master/src/test/java/org/marid/typedmap/sparsed/ExampleSparsedStruct.java) of 128 fields. But only 10-15% of the fields are populated with non-null values.

So, instead of using this awful data structure you can use hashmap-like typed structure that let you code with static typing but with increased performance and reduced memory consumption.

Let's compare these approaches:

```
Benchmark                                                           Mode  Cnt        Score        Error   Units
ExampleSparsedStructBenchmark.byte16                               thrpt    5  4126117.531 ±  47377.163   ops/s
ExampleSparsedStructBenchmark.byte16:·gc.alloc.rate                thrpt    5      251.642 ±      2.538  MB/sec
ExampleSparsedStructBenchmark.byte16:·gc.alloc.rate.norm           thrpt    5       96.001 ±      0.001    B/op
ExampleSparsedStructBenchmark.byte16:·gc.churn.G1_Eden_Space       thrpt    5       93.052 ±      6.831  MB/sec
ExampleSparsedStructBenchmark.byte16:·gc.churn.G1_Eden_Space.norm  thrpt    5       35.500 ±      2.840    B/op
ExampleSparsedStructBenchmark.byte16:·gc.churn.G1_Old_Gen          thrpt    5        0.653 ±      0.009  MB/sec
ExampleSparsedStructBenchmark.byte16:·gc.churn.G1_Old_Gen.norm     thrpt    5        0.249 ±      0.005    B/op
ExampleSparsedStructBenchmark.byte16:·gc.count                     thrpt    5        5.000               counts
ExampleSparsedStructBenchmark.byte16:·gc.time                      thrpt    5       14.000                   ms
ExampleSparsedStructBenchmark.byte8                                thrpt    5  6145184.046 ± 179173.952   ops/s
ExampleSparsedStructBenchmark.byte8:·gc.alloc.rate                 thrpt    5      436.792 ±     13.154  MB/sec
ExampleSparsedStructBenchmark.byte8:·gc.alloc.rate.norm            thrpt    5      112.001 ±      0.001    B/op
ExampleSparsedStructBenchmark.byte8:·gc.churn.G1_Eden_Space        thrpt    5      292.861 ±     10.199  MB/sec
ExampleSparsedStructBenchmark.byte8:·gc.churn.G1_Eden_Space.norm   thrpt    5       75.100 ±      4.322    B/op
ExampleSparsedStructBenchmark.byte8:·gc.churn.G1_Old_Gen           thrpt    5        0.652 ±      0.010  MB/sec
ExampleSparsedStructBenchmark.byte8:·gc.churn.G1_Old_Gen.norm      thrpt    5        0.167 ±      0.007    B/op
ExampleSparsedStructBenchmark.byte8:·gc.count                      thrpt    5       10.000               counts
ExampleSparsedStructBenchmark.byte8:·gc.time                       thrpt    5       36.000                   ms
ExampleSparsedStructBenchmark.linked                               thrpt    5  6326925.220 ± 376948.722   ops/s
ExampleSparsedStructBenchmark.linked:·gc.alloc.rate                thrpt    5     1546.872 ±     97.953  MB/sec
ExampleSparsedStructBenchmark.linked:·gc.alloc.rate.norm           thrpt    5      384.001 ±      0.001    B/op
ExampleSparsedStructBenchmark.linked:·gc.churn.G1_Eden_Space       thrpt    5     1323.210 ±     11.094  MB/sec
ExampleSparsedStructBenchmark.linked:·gc.churn.G1_Eden_Space.norm  thrpt    5      328.557 ±     23.219    B/op
ExampleSparsedStructBenchmark.linked:·gc.churn.G1_Old_Gen          thrpt    5        0.655 ±      0.006  MB/sec
ExampleSparsedStructBenchmark.linked:·gc.churn.G1_Old_Gen.norm     thrpt    5        0.163 ±      0.011    B/op
ExampleSparsedStructBenchmark.linked:·gc.count                     thrpt    5       20.000               counts
ExampleSparsedStructBenchmark.linked:·gc.time                      thrpt    5      102.000                   ms
ExampleSparsedStructBenchmark.pojo                                 thrpt    5  3850679.239 ± 994075.330   ops/s
ExampleSparsedStructBenchmark.pojo:·gc.alloc.rate                  thrpt    5     1294.028 ±    343.558  MB/sec
ExampleSparsedStructBenchmark.pojo:·gc.alloc.rate.norm             thrpt    5      528.001 ±      0.001    B/op
ExampleSparsedStructBenchmark.pojo:·gc.churn.G1_Eden_Space         thrpt    5      939.611 ±   1327.491  MB/sec
ExampleSparsedStructBenchmark.pojo:·gc.churn.G1_Eden_Space.norm    thrpt    5      378.168 ±    446.745    B/op
ExampleSparsedStructBenchmark.pojo:·gc.churn.G1_Old_Gen            thrpt    5        0.654 ±      0.003  MB/sec
ExampleSparsedStructBenchmark.pojo:·gc.churn.G1_Old_Gen.norm       thrpt    5        0.268 ±      0.071    B/op
ExampleSparsedStructBenchmark.pojo:·gc.count                       thrpt    5       17.000               counts
ExampleSparsedStructBenchmark.pojo:·gc.time                        thrpt    5       78.000                   ms
```

## As a replacement for EnumMap with typed keys

## As a type-safe and memory efficient replacement for Int2ObjectMap/IntObjectMap with domain keys

# Performance characteristics

| Map                           | size | get, ns/op | put, ns/op | put GC alloc.rate, B/op | put GC time, ms |
|-------------------------------|-----:|-----------:|-----------:|------------------------:|----------------:|
| TypedLinkedMutableMap         |    5 |     10.200 |     32.284 |                      27 |             110 |  
| HashMap                       |    5 |     17.960 |     59.152 |                      60 |             136 |
| Object2ObjectOpenHashMap      |    5 |     18.532 |     76.600 |                      77 |             127 |
| TypedLinkedMutableSyncMap     |    5 |     26.404 |     33.596 |                      27 |             112 |
| ConcurrentHashMap             |    5 |     17.696 |     96.044 |                      64 |              82 |
| Object2ObjectOpenHashSyncMap  |    5 |     33.793 |     88.491 |                      85 |             123 |
|                               |      |            |            |                         |                 |
| TypedLinkedMutableMap         |   54 |     53.611 |     71.444 |                      22 |             122 |
| HashMap                       |   54 |     18.777 |     67.870 |                      49 |             101 |
| Object2ObjectOpenHashMap      |   54 |     20.944 |     75.925 |                      37 |             117 |
| TypedLinkedMutableSyncMap     |   54 |     64.001 |     73.796 |                      22 |             115 |
| ConcurrentHashMap             |   54 |     19.387 |    196.722 |                      58 |             123 |
| Object2ObjectOpenHashSyncMap  |   54 |     36.277 |     84.537 |                      38 |             181 |
|                               |      |            |            |                         |                 |
| TypedLinkedMutableMap         |  180 |    146.572 |    166.583 |                      22 |              52 |
| HashMap                       |  180 |     20.311 |     58.244 |                      41 |              91 |
| Object2ObjectOpenHashMap      |  180 |     24.775 |     68.227 |                      23 |             142 | 
| TypedLinkedMutableSyncMap     |  180 |    160.616 |    168.050 |                      22 |              54 |
| ConcurrentHashMap             |  180 |     20.600 |    157.194 |                      47 |             114 | 
| Object2ObjectOpenHashSyncMap  |  180 |     40.550 |     69.922 |                      23 |             129 |
