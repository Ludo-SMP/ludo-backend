import { MySqlTransaction } from "drizzle-orm/mysql-core";
import {
  MySql2PreparedQueryHKT,
  MySql2QueryResultHKT,
} from "drizzle-orm/mysql2";
import { ExtractTablesWithRelations } from "drizzle-orm/relations";

export type Tx = MySqlTransaction<
  MySql2QueryResultHKT,
  MySql2PreparedQueryHKT,
  Record<string, never>,
  ExtractTablesWithRelations<Record<string, never>>
>;

export type BaseResponse<T> = {
  status: number;
  message: string;
  data: T;
};

export interface BaseDateTime {
  createdDateTime: string;
  updatedDateTime: string;
}
