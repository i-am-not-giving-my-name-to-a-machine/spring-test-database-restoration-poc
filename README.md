# Getting Started

This project shows how to use `@Transactional` tests _with_ `@Commit`,
but without the need for `@DirtiesContext` to start over again in order
to just wipe the test database.

The annotation `@ReuseDatabase` can be used to, well, reuse a database _from a previous test_.
