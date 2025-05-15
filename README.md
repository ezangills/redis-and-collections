Task that I chose - https://github.com/Ecwid/new-job/blob/master/Redis-and-collections.md

Setting up the Redist cluster is described in [this README.md](src/main/resources/redis-cluster/README.md) and everything needed to run the cluster is under this directory: `src/main/resources/redis-cluster/`.

To run the demo of the task you just need to launch the redis cluster as described in the README linked above and then run `Main.java`

There are also tests written in `RedisMapTest.java` but they require a cluster running to work, so they have annotation `@Disabled` on them. In real life application I would write these as integration tests. To run these tests you need to launch the cluster, remove the `@Disabled` annotation and launch the tests.