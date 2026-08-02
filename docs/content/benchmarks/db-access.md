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

```console
mvn --projects yosql-benchmarks/yosql-benchmarks-vs-jdbi --also-make \
  --activate-profiles benchmarks verify
```

## Against an ORM

There is none. `yosql-benchmarks-vs-ebean`, `-vs-jooq` and `-vs-jpa` are empty modules, reserved
rather than written.

For an ORM the comparison is harder to make honestly than it looks: read the same entity twice and
Hibernate answers the second from its identity map, so it wins by a distance; turn that off and you
are measuring a Hibernate nobody deploys. Whichever you choose, the number is an argument about the
configuration. The [comparison of alternatives](../../community/alternatives/) says where an ORM is
the better tool without pretending to a measurement.
