import { eq } from "drizzle-orm";
import { category, position, stack, stackCategory } from "../drizzle/schema";
import { CATEGORIES } from "../fixtures/category-fixture";
import { POSITIONS } from "../fixtures/position-fixture";
import { STACKS, STACK_CATEGORIES } from "../fixtures/stack-fixture";
import { utcNow } from "../helpers/datetime-helper";
import { Tx } from "../types/base-types";
import { db } from "./db-client";

export async function setUpForStudy() {
  try {
    await db.transaction(async (tx) => {
      await Promise.allSettled([
        _setupDefaultCategories(tx),
        _setUpDefaultPositions(tx),
        _setUpDefaultStacks(tx),
      ]);
    });
  } catch (e) {
    console.error("`setUpForStudy` failed");
    console.error(e);
  }
}

async function _setupDefaultCategories(tx: Tx) {
  for (const data of CATEGORIES) {
    const d = {
      ...data,
      createdDateTime: utcNow(),
      updatedDateTime: utcNow(),
    };
    const exists = await tx
      .select()
      .from(category)
      .where(eq(category.categoryId, d.categoryId));
    if (!exists.length) {
      await tx.insert(category).values(d);
    }
  }
}

async function _setUpDefaultPositions(tx: Tx) {
  for (const data of POSITIONS) {
    const d = {
      ...data,
      createdDateTime: utcNow(),
      updatedDateTime: utcNow(),
    };
    const exists = await tx
      .select()
      .from(position)
      .where(eq(position.positionId, d.positionId));
    if (!exists.length) {
      await tx.insert(position).values(d);
    }
  }
}

async function _setUpDefaultStacks(tx: Tx) {
  await _setUpDefaultStackCategories(tx);
  for (const data of STACKS) {
    const d = {
      ...data,
      createdDateTime: utcNow(),
      updatedDateTime: utcNow(),
    };
    const exists = await tx
      .select()
      .from(stack)
      .where(eq(stack.stackId, d.stackId));
    if (!exists.length) {
      await tx.insert(stack).values(d);
    }
  }
}

async function _setUpDefaultStackCategories(tx: Tx) {
  for (const data of STACK_CATEGORIES) {
    const d = {
      ...data,
      createdDateTime: utcNow(),
      updatedDateTime: utcNow(),
    };
    const exists = await tx
      .select()
      .from(stackCategory)
      .where(eq(stackCategory.stackCategoryId, d.stackCategoryId));
    if (!exists.length) {
      await tx.insert(stackCategory).values(d);
    }
  }
}
