---
title: Database Access
date: 2026-08-02
menu:
  main:
    parent: Benchmarks
categories:
  - Benchmarks
tags:
  - database
  - access
---

How long a generated repository takes to run a statement, measured across every logging
configuration `YoSQL` can generate.

What is being measured is the overhead `YoSQL` adds on top of JDBC — not how fast your database is.
The numbers are dominated by the database in any real application, which is rather the point: there
is nothing between your query and the driver except the code you can read in
`target/generated-sources`.

## The scenarios

Each configuration runs the same set, so the numbers are comparable across them:

Eleven of them, declared as the `Read`, `Write` and `Call` interfaces in
`yosql-benchmarks-dao`, so that an implementation either covers all of them or does not compile.

### Reading

- `readSingleEntityByPrimaryKey`: Read a single entity using its primary key.
- `readOneToManyRelation`: Reads the many part of a one-to-many relation.
- `readManyToOneRelation`: Reads the one part of a many-to-one relation.
- `readMultipleEntities`: Read multiple entities in one go.
- `readMultipleEntitiesBasedOnCondition`: Read multiple entities and filter them inside the database.

### Writing

- `writeSingleEntity`: Writes a new entity into the database.
- `writeMultipleEntities`: Writes several entities as one batch.
- `updateSingleEntity`: Update every column of one entity.
- `updateOneToManyRelation`: Update the one-to-many relationship part of an entity.
- `deleteSingleEntityByPrimaryKey`: Delete a single entity using its primary key.

### Calling stored procedures

- `callStoredProcedure`: Call a single stored procedure.

## The published results

[The results](https://jmh.morethan.io/?sources=https://yosql.projects.metio.wtf/benchmarks/results/yosql-benchmarks-dao-baseline.json,https://yosql.projects.metio.wtf/benchmarks/results/yosql-benchmarks-dao-CURRENT.json)
are in **microseconds**, against an in-process H2, with every logging implementation configured for
maximum output so the cost of each can be read against the no-op baseline.

Read them with their provenance in mind. They were measured on a shared GitHub Actions runner, one
fork, three warmup and five measurement iterations — enough to see the shape, not enough to separate
two implementations that are close. Several have a confidence interval wider than the score itself.

**If you are making a decision, run them on your own hardware with your own statements.** That is
not a disclaimer; it is the only way a number like this means anything for your project.

```console
mvn --projects yosql-benchmarks/yosql-benchmarks-dao --also-make \
  --activate-profiles benchmarks verify
```

The run writes `target/benchmark/yosql-benchmarks-dao.json`, which
[jmh.morethan.io](https://jmh.morethan.io/) will render against the published baseline.

## Against JDBI

`yosql-benchmarks-vs-jdbi` runs the eleven scenarios twice — once through generated repositories,
once through [JDBI](https://jdbi.org/) — in **one** JMH run.

That matters more than it sounds. Numbers from two runs on two machines are not comparable at all;
numbers from one run share the JVM, the warmup, the schema and the hardware, so what is left is the
difference between the two libraries. Both take a connection per call and give it back, both read
rows into `Map<String, Object>`, and both send the database the same SQL — the statements come from
this module's `.sql` files, so neither side can be measured running a query the other did not.

Each implementation gets its own in-memory database, because the write scenarios insert and delete
and a shared one would make each side's numbers depend on how often the other had already run.

Both implementations are in the repository. If you suspect one was written to lose, read it — that
is the answer a chart cannot give you.

### What it measures

µs per operation, lower is better. JDK 25, H2 in process, one fork, three warmup and five
measurement iterations, on an otherwise idle 16-core machine.

| Scenario | `YoSQL` | JDBI | Difference |
| --- | --- | --- | --- |
| `callStoredProcedure` | 7.07 ± 0.44 | 17.63 ± 5.81 | +10.56 |
| `readManyToOneRelation` | 7.85 ± 0.68 | 18.62 ± 2.52 | +10.77 |
| `readMultipleEntitiesBasedOnCondition` | 7.87 ± 0.67 | 18.17 ± 3.98 | +10.30 |
| `deleteSingleEntityByPrimaryKey` | 7.87 ± 1.28 | 17.85 ± 7.13 | +9.98 |
| `readOneToManyRelation` | 8.00 ± 0.74 | 17.40 ± 1.10 | +9.40 |
| `readSingleEntityByPrimaryKey` | 8.03 ± 1.19 | 17.62 ± 2.32 | +9.59 |
| `updateOneToManyRelation` | 8.06 ± 1.14 | 18.57 ± 4.09 | +10.51 |
| `readMultipleEntities` | 8.07 ± 0.79 | 16.39 ± 1.50 | +8.32 |
| `writeSingleEntity` | 12.15 ± 1.36 | 21.54 ± 4.81 | +9.39 |
| `updateSingleEntity` | 13.60 ± 2.05 | 22.11 ± 5.96 | +8.52 |
| `writeMultipleEntities` | 56.47 ± 12.96 | 60.72 ± 6.61 | +4.25 |

### What it means, and what it does not

The ratios run from 1.08× to 2.49×, and quoting any of them would be misleading. Read the last
column instead: the difference is about **9.6 µs on every scenario**, whatever that scenario does.
A fixed cost per call, not a proportional one — JDBI builds a `Handle`, with its configuration and
its mapper registry, every time you ask for one. Generated code has no such step because it has
nothing to configure.

`writeMultipleEntities` is the one that proves it. It does roughly 50 µs of real database work, and
there the two are **indistinguishable**: +4.25 µs with error bars that overlap. Once actual work
dominates, the constant disappears into it.

So the honest claim is narrow: **JDBI's per-call machinery costs around 10 µs, and `YoSQL` has no
per-call machinery.** Whether that matters to you is arithmetic. Against an in-process H2, where a
query costs 8 µs, it doubles your time. Against a database on the other side of a socket, where a
query costs hundreds of microseconds or milliseconds, 10 µs is a rounding error you will never
measure — which is the same reason this page opens by saying it measures what the layer costs and
not what your application will do.

Two caveats worth keeping in view. JDBI's error bars are wide — ±5.81 µs on one scenario — so no
single row settles anything; it is the same result appearing in all eleven that carries it. And this
is one fork on one machine.

```console
mvn --projects yosql-benchmarks/yosql-benchmarks-vs-jdbi \
  --activate-profiles benchmarks verify
```

Leave `--also-make` off once the dependencies are installed. With it, the DAO benchmarks run too —
including the variant that logs every statement — which takes far longer and writes a very large
log.

## Against an ORM

There is none. `yosql-benchmarks-vs-ebean`, `-vs-jooq` and `-vs-jpa` are empty modules, reserved
rather than written.

For an ORM the comparison is harder to make honestly than it looks: read the same entity twice and
Hibernate answers the second from its identity map, so it wins by a distance; turn that off and you
are measuring a Hibernate nobody deploys. Whichever you choose, the number is an argument about the
configuration. The [comparison of alternatives](../../community/alternatives/) says where an ORM is
the better tool without pretending to a measurement.
