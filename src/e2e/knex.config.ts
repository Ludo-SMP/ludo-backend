import type { Knex } from "knex";

const { DB_HOST, DB_USER, DB_PASSWORD, DB_NAME } = process.env;

export const config: { [key: string]: Knex.Config } = {
  test: {
    client: "mysql2",
    connection: {
      host: DB_HOST,
      user: DB_USER,
      password: DB_PASSWORD,
      database: DB_NAME,
    },
    pool: {
      min: 1,
      max: 30,
    },
    acquireConnectionTimeout: 5000,
    compileSqlOnError: true,
    debug: true,
    log: {
      debug: (message: string) => {
        console.log(message);
      },
      warn: (message: string) => {
        console.log(message);
      },
      error: (message: string) => {
        console.log(message);
      },
    },
  },
};
