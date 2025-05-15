## Start cluster

Run following commands in bash:

```bash
redis-server 7001/redis.conf
redis-server 7002/redis.conf
redis-server 7003/redis.conf
redis-server 7004/redis.conf
redis-server 7005/redis.conf
```

If you get "The TCP backlog setting of 511 cannot be enforced because kern.ipc.somaxconn is set to the lower value of <N>".
Might need to set the setting:

```bash
sudo sysctl -w kern.ipc.somaxconn=511
```

Then run:

```bash
redis-cli --cluster create 127.0.0.1:7001 127.0.0.1:7002 127.0.0.1:7003 127.0.0.1:7004 127.0.0.1:7005 --cluster-replicas 0
```

## Helpful commands

Check the cluster is running

```bash
redis-cli -p 7001 CLUSTER INFO
```

Kill process on port

```bash
kill -9 $(lsof -t -i:7001)
```