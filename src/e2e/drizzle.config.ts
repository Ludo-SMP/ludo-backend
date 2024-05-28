import { defineConfig } from "drizzle-kit";

// console.log(process.env);
export default defineConfig({
  schema: "./__generated__/schema/*",
  out: "./drizzle",
  dialect: "mysql",
  dbCredentials: {
    url: process.env.DB_URL as string,
  },
  introspect: {
    casing: "camel",
  },
});
