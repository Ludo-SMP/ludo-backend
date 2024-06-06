import { afterAll, beforeAll } from "@jest/globals";
import { setUpForStudy } from "./config/setup";

beforeAll(async () => {
  await setUpForStudy();
});

afterAll(async () => {});
