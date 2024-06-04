import knex from "knex";
import { config } from "../knex.config";

const environment = process.env.NODE_ENV || "development";
export const db = knex(config[environment]);
