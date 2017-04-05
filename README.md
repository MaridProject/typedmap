# Usage

## Create interfaces for key domains and static final key instances

```java
public interface MyCommonKeyDomain extends KeyDomain {
}
```

```java
public interface MyKeyDomain1 extends MyCommonKeyDomain {

    Key<MyKeyDomain1, Integer> KEY1 = new Key<>(MyKeyDomain1.class, () -> 0);
    Key<MyKeyDomain1, Long> KEY2 = new Key<>(MyKeyDomain1.class, () -> 0L);
    // ...
}
```

```java
public interface MyKeyDomain2 extends MyCommonKeyDomain {

   Key<MyKeyDomain1, BigDecimal> KEY3 = new Key<>(MyKeyDomain2.class, () -> BigDecimal.ZERO);
   // ...
}
```

## Create a typed map instance and put values

```java
TypedByte8KeyMap<MyCommonKeyDomain, Number> map = new TypedByte8KeyMap<>();
map.put(KEY1, 8); // you can put only Integer instances for KEY1
map.put(KEY3, BigDecimal.ONE);
```

# Use cases

## As a replacement for sparsed data structures with a high probability of uninitialized fields 

Suppose we have a data class (see [SparsedStruct.java](https://github.com/MaridProject/typedmap/blob/master/src/test/java/org/marid/typedmap/sparsed/SparsedStruct.java)) of 128 fields. But only 10-15% of the fields are populated with non-null values.

So, instead of using this awful data structure you can use hashmap-like typed structure that let you code with static typing but with increased performance and reduced memory consumption.

Let's compare these approaches:

```
Benchmark                                                    Mode  Cnt        Score         Error   Units
SparsedStructBenchmark.byte16                               thrpt    5  3928450.037 ±  100597.093   ops/s
SparsedStructBenchmark.byte16:·gc.alloc.rate                thrpt    5      239.424 ±       5.997  MB/sec
SparsedStructBenchmark.byte16:·gc.alloc.rate.norm           thrpt    5       96.001 ±       0.001    B/op
SparsedStructBenchmark.byte16:·gc.churn.G1_Eden_Space       thrpt    5      103.488 ±      87.770  MB/sec
SparsedStructBenchmark.byte16:·gc.churn.G1_Eden_Space.norm  thrpt    5       41.529 ±      36.146    B/op
SparsedStructBenchmark.byte16:·gc.churn.G1_Old_Gen          thrpt    5        0.651 ±       0.023  MB/sec
SparsedStructBenchmark.byte16:·gc.churn.G1_Old_Gen.norm     thrpt    5        0.261 ±       0.013    B/op
SparsedStructBenchmark.byte16:·gc.count                     thrpt    5        6.000                counts
SparsedStructBenchmark.byte16:·gc.time                      thrpt    5       24.000                    ms
SparsedStructBenchmark.byte8                                thrpt    5  5116186.063 ±  157104.708   ops/s
SparsedStructBenchmark.byte8:·gc.alloc.rate                 thrpt    5      363.745 ±      12.946  MB/sec
SparsedStructBenchmark.byte8:·gc.alloc.rate.norm            thrpt    5      112.001 ±       0.001    B/op
SparsedStructBenchmark.byte8:·gc.churn.G1_Eden_Space        thrpt    5      292.694 ±       2.673  MB/sec
SparsedStructBenchmark.byte8:·gc.churn.G1_Eden_Space.norm   thrpt    5       90.131 ±       3.831    B/op
SparsedStructBenchmark.byte8:·gc.churn.G1_Old_Gen           thrpt    5        0.653 ±       0.006  MB/sec
SparsedStructBenchmark.byte8:·gc.churn.G1_Old_Gen.norm      thrpt    5        0.201 ±       0.006    B/op
SparsedStructBenchmark.byte8:·gc.count                      thrpt    5       10.000                counts
SparsedStructBenchmark.byte8:·gc.time                       thrpt    5       39.000                    ms
SparsedStructBenchmark.linked                               thrpt    5  6298741.392 ±  234602.431   ops/s
SparsedStructBenchmark.linked:·gc.alloc.rate                thrpt    5     1536.016 ±      45.973  MB/sec
SparsedStructBenchmark.linked:·gc.alloc.rate.norm           thrpt    5      384.001 ±       0.001    B/op
SparsedStructBenchmark.linked:·gc.churn.G1_Eden_Space       thrpt    5     1317.853 ±      11.009  MB/sec
SparsedStructBenchmark.linked:·gc.churn.G1_Eden_Space.norm  thrpt    5      329.475 ±       9.633    B/op
SparsedStructBenchmark.linked:·gc.churn.G1_Old_Gen          thrpt    5        0.652 ±       0.008  MB/sec
SparsedStructBenchmark.linked:·gc.churn.G1_Old_Gen.norm     thrpt    5        0.163 ±       0.005    B/op
SparsedStructBenchmark.linked:·gc.count                     thrpt    5       20.000                counts
SparsedStructBenchmark.linked:·gc.time                      thrpt    5      103.000                    ms
SparsedStructBenchmark.pojo                                 thrpt    5  3679792.900 ± 1193894.983   ops/s
SparsedStructBenchmark.pojo:·gc.alloc.rate                  thrpt    5     1241.953 ±     409.663  MB/sec
SparsedStructBenchmark.pojo:·gc.alloc.rate.norm             thrpt    5      528.001 ±       0.001    B/op
SparsedStructBenchmark.pojo:·gc.churn.G1_Eden_Space         thrpt    5     1057.805 ±    1307.181  MB/sec
SparsedStructBenchmark.pojo:·gc.churn.G1_Eden_Space.norm    thrpt    5      443.198 ±     438.197    B/op
SparsedStructBenchmark.pojo:·gc.churn.G1_Old_Gen            thrpt    5        0.651 ±       0.013  MB/sec
SparsedStructBenchmark.pojo:·gc.churn.G1_Old_Gen.norm       thrpt    5        0.278 ±       0.103    B/op
SparsedStructBenchmark.pojo:·gc.count                       thrpt    5       18.000                counts
SparsedStructBenchmark.pojo:·gc.time                        thrpt    5       89.000                    ms
```

## As a replacement for EnumMap with typed keys

As you know, java enums have an important limitation not allowing to use typed instances. You cannot do something like that

```java
public enum MyEnum implements Interface<T> {

   KEY1<Integer>,
   KEY2<Long>
   // ...
}
```
in enums.

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
