import { db } from "../config/db-client";
import { study } from "../drizzle/schema";

export async function updateStudyEndDateTime(endDateTime: Date) {
  await db
    .update(study)
    .set({ endDateTime: endDateTime.toISOString().slice(0, 22) });
}
