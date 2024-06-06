import dotenv from "dotenv";
import { MySql2Database, drizzle } from "drizzle-orm/mysql2";
import mysql from "mysql2/promise";

dotenv.config();
const { DB_NAME, DB_PORT, DB_HOST, DB_USER, DB_PASSWORD } = process.env;
export type DbConn = MySql2Database<Record<string, never>>;

const pool = mysql.createPool({
  host: DB_HOST,
  user: DB_USER,
  password: DB_PASSWORD,
  database: DB_NAME,
  // uri: process.env.DB_URI,
  // pool: {
  //   min: 1,
  //   max: 50,
  // },
  // idleTimeout: 60_000,
  // waitForConnections: true,
  // connectionLimit: 50,
  // queueLimit: 50,
  // enableKeepAlive: true,
  // connectTimeout: 10_000,
  // debug: true,
});

export const db = drizzle(pool);
